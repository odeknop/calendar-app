package com.ode.sunrisechallenge.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.model.impl.db.DBHelper;
import com.ode.sunrisechallenge.utils.NavigationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * Created by ode on 13/07/15.
 */
public class LoginFragment extends Fragment {

    com.google.api.services.calendar.Calendar service;
    private GoogleAccountCredential credential;
    IAccount account;
    ApiAsyncEventTask currentTask;

    private final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    private final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    public static final int REQUEST_AUTHORIZATION = 1001;
    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};

    public TextView mStatus;
    public ProgressBar mProgress;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if(getArguments() != null ) {
            if(getArguments().getBoolean(NavigationUtils.LOGOUT)) {
                account = null;
            }
        }
    }

    private void init() {
        credential = GoogleAccountCredential.usingOAuth2(
                getActivity(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        service = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("SunriseChallenge")
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStatus = (TextView) view.findViewById(R.id.status);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mStatus.setVisibility(View.INVISIBLE);

        view.findViewById(R.id.btn_connect_with_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importData();
            }
        });

        view.findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExportDatabaseFileTask().execute();
            }
        });
    }

    private void importData() {
        if (currentTask != null) return;
        Account account = credential.getSelectedAccount();
        if (account == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                currentTask = new ApiAsyncEventTask(this);
                currentTask.execute();
            } else {
                int count = this.account.getEventManager().getAllEvents().length;
                startActivity(NavigationUtils.navigateToMainView(getActivity(), this.account));
                Toast.makeText(getActivity(), "You have " + count + " event"+ (count > 0 ? "s" : "") + " offline", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != Activity.RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        this.account = DBHelper.getInstance().createAccount(accountName);
                        importData();
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    mProgress.setVisibility(View.INVISIBLE);
                    mStatus.setText("Account unspecified.");
                    mStatus.setVisibility(View.VISIBLE);
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != Activity.RESULT_OK) {
                    chooseAccount();
                } else {
                    importData();
                }
                break;
        }
    }

    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {
        startActivityForResult(
                credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,
                        getActivity(),
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    private class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {

        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        protected Boolean doInBackground(final String... args) {

            File dbFile = new File(Environment.getDataDirectory() + "/data/" + getActivity().getApplicationInfo().packageName + "/databases/" + DBHelper.DATABASE_NAME);
            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, dbFile.getName());

            try {
                file.createNewFile();
                this.copyFile(dbFile, file);
                return true;
            } catch (IOException e) {
                Log.e(getClass().getName(), e.getMessage(), e);
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success) {
                Toast.makeText(getActivity(), "Export successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Export failed", Toast.LENGTH_SHORT).show();
            }
        }

        void copyFile(File src, File dst) throws IOException {
            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            }
        }
    }
}
