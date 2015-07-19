package com.ode.sunrisechallenge.model.impl.db;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.model.IData;
import com.ode.sunrisechallenge.model.utils.Utils;
import com.ode.sunrisechallenge.model.utils.WeakSortedList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import gnu.trove.map.hash.THashMap;

/**
 * Created by ode on 26/06/15.
 */
public class DBHelper extends Tables implements IData {

    public static final String TAG = DBHelper.class.getName();
    private static Context mContext;
    private static final Object mLock = new Object();
    public static final String DATABASE_NAME = "challenge_sunrise";
    private static volatile IData mData;

    private final THashMap<Class<? extends DBObject>, WeakSortedList<? extends DBObject>> mCachedItems = new THashMap<>();

    final SQLiteDatabase database;
    volatile boolean disposed;

    public static IData getInstance() {
        if (mContext == null) {
            throw new RuntimeException("Context must be set before using the database");
        }

        IData instance = mData;

        if (instance != null) {
            return instance;
        }

        synchronized (mLock) {
            if (mData == null) {
                mData = new DBHelper(mContext);
            }
            return mData;
        }
    }

    public static void setApplicationContext(Context context) {
        mContext = context;
    }

    private DBHelper(Context context) {
        database = new OpenHelper(context, DATABASE_NAME).getWritableDatabase();
    }

    private static void closeInstance(IData data) {
        synchronized (mLock) {
            if (data == mData)
                mData = null;
        }
    }

    Object getLock() {
        return mCachedItems;
    }

    <T extends DBObject> void nl_removeItem(T remove, Class<T> tClass) {
        WeakSortedList<T> res = (WeakSortedList<T>) mCachedItems.get(tClass);
        if (res != null) res.remove(remove.getComparableValue());
    }

    <T extends DBObject> T nl_getItem(Class<T> tClass, long id) {
        WeakSortedList<T> res = (WeakSortedList<T>) mCachedItems.get(tClass);
        if (res != null) return res.get(id);
        return null;
    }

    <T extends DBObject> Transaction<T> nl_updateItem(Class<T> tClass, Cursor cursor, DBObject.IActivator<T> activator) {
        long id = cursor.getLong(0);
        T existing;
        WeakSortedList<T> res = (WeakSortedList<T>) mCachedItems.get(tClass);
        if (res != null) {
            existing = res.get(id);
            if (existing != null) {
                RowContent content = existing.setFromCursor(cursor);
                return new Transaction<>(existing, content);
            } else {
                existing = activator.newInstance(tClass, this, cursor);
                if (existing.id != id) throw new RuntimeException();
                res.add(existing);
            }
        } else {
            existing = activator.newInstance(tClass, this, cursor);
            if (existing.id != id) throw new RuntimeException();
            WeakSortedList<T> list = new WeakSortedList<>();
            list.add(existing);
            mCachedItems.put(tClass, list);
        }
        return new Transaction<>(existing);
    }

    static class Transaction<T extends DBObject> {

        public final T object;
        public final RowContent content;
        public final boolean exists;

        Transaction(T object, RowContent content) {
            this.object = object;
            this.content = content;
            exists = true;
        }

        Transaction(T obj) {
            this.object = obj;
            this.content = obj.row(false);
            exists = false;
        }

        public T commit() {
            if (exists) object.setNewRef(content);
            return object;
        }
    }

    @Override
    public IAccount getAccount(long accountId) {
        return Utils.first(_getAccounts(accountId, null));
    }

    @Override
    public IAccount getAccount(String name) {
        return Utils.first(_getAccounts(null, name));
    }

    @Override
    public void close() throws IOException {
        closeInstance(this);
        disposed = true;
        database.close();
    }

    public static void deleteDatabase(Context context) throws IOException {
        File path = context.getDatabasePath(DATABASE_NAME);
        if (path.exists() && !path.delete()) throw new IOException("Cannot delete database " + DATABASE_NAME);
    }

    private ArrayList<IAccount> _getAccounts(Long id, String name) {
        StringBuilder selection = null;
        ArrayList<String> args = null;

        if (id != null && id > 0) {
            Utils.appendString(selection, "[AccountId] = ?", " AND");
            (args = Utils.useOrCreate(args, ArrayList.class)).add(id.toString());
        }
        if (name != null) {
            selection = Utils.appendString(selection, "[AccountName] = ?", " AND");
            (args = Utils.useOrCreate(args, ArrayList.class)).add(name);
        }
        Cursor cursor = null;
        ArrayList<IAccount> accounts = new ArrayList<>();
        try {
            cursor = database.query(
                    TABLE_ACCOUNT, new String[]{"[AccountId]",
                            "[AccountName]"},
                    selection == null ? null : selection.toString(),
                    selection == null ? null : args.toArray(Utils.EMPTY_STRING_ARR),
                    null, null, null, null
            );

            DBObject.IActivator<Account> ac = new DBObject.IActivator<Account>() {
                @Override
                public Account newInstance(Class<Account> accountClass, DBHelper dbHelper, Cursor cursor) {
                    return new Account(dbHelper, cursor);
                }
            };

            while (cursor.moveToNext()) {
                if (accounts == null)
                    accounts = new ArrayList<>();
                Account account = nl_updateItem(Account.class, cursor, ac).commit();
                accounts.add(account);
            }
        } finally {
            Utils.closeQuietly(cursor);
        }

        return accounts;
    }

    @Override
    public IAccount createAccount(String name) {
        long id;
        database.beginTransaction();
        try {
            IAccount account = Utils.first(_getAccounts(null, name));
            if(account != null) return account;
            ContentValues contentValues = new ContentValues();
            contentValues.put("AccountName", name);
            id = database.insertOrThrow(TABLE_ACCOUNT, null, contentValues);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return Utils.first(_getAccounts(id, null));
    }

    @Override
    public void deleteAccount(IAccount _account) {
        Account account = (Account) _account;
        long profileId = account.row().id;

        database.beginTransaction();
        try {
            if (database.delete(TABLE_ACCOUNT, "AccountId = ?", Utils.arr(profileId)) != 1) return;
            account.setNewRef(null);
            synchronized (getLock()) {
                nl_removeItem(account, Account.class);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    private final class OpenHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;

        public OpenHelper(Context context, String databaseName) {
            super(context, databaseName, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            for (String tableSQL : TABLES)
                db.execSQL(tableSQL);

            for (String[] indexes : INDEXES)
                for (String indexSQL : indexes)
                    db.execSQL(indexSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private final static String TABLES[] = new String[]{
            "CREATE TABLE " + TABLE_ACCOUNT + " ( " +
                    "[AccountId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "[AccountName] NVARCHAR(65) COLLATE NOCASE NOT NULL " +
                    "); ",
            "CREATE TABLE " + TABLE_ACCOUNT_TYPE + " ( " +
                    "[AccountTypeId] INTEGER NOT NULL PRIMARY KEY, " +
                    "[Label] NVARCHAR(65) NOT NULL " +
                    "); ",
            "CREATE TABLE " + TABLE_EVENT + " ( " +
                    "[EventId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "[Title] NVARCHAR(65) COLLATE NOCASE NOT NULL, " +
                    "[Description] TEXT NULL, " +
                    "[Location] NVARCHAR(65) NULL, " +
                    "[StartTime] DATETIME NOT NULL, " +
                    "[EndTime] DATETIME NOT NULL, " +
                    "[AccountTypeId] INTEGER NULL, " +
                    "[AccountEventId] NVARCHAR(65) NOT NULL, " +
                    "[OwnerAccountId] INTEGER  NOT NULL, " +
                    "FOREIGN KEY(OwnerAccountId) REFERENCES " + TABLE_ACCOUNT + "(AccountId) ON " +
                    "DELETE CASCADE " +
                    ");"
    };

    /*
    Caused by: android.database.sqlite.SQLiteException: near "NOCASE": syntax error (code 1): , while compiling: CREATE TABLE [Event] ( [EventId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [Title] NVARCHAR(65) COLLATE NOCASE  NOT NULL, [Description] TEXT  NULL, [Location] NVARCHAR(65) NOCASE  NULL, [StartTime] DATETIME  NOT NULL, [EndTime] DATETIME  NOT NULL, [AccountTypeId] INTEGER NULL, [AccountEventId] NVARCHAR(65) NOT NULL, [OwnerAccountId] INTEGER  NOT NULL, FOREIGN KEY(OwnerAccountId) REFERENCES [UserAccount](AccountId) ON DELETE CASCADE );
     */

    private final static String[][] INDEXES = new String[][]{
            new String[]{
                    "CREATE UNIQUE INDEX [UserAccountIndex] ON " + TABLE_ACCOUNT + "( " +
                            "[AccountName]  DESC " +
                            ");"
            }
    };
}