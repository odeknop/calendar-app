package com.ode.sunrisechallenge;

import android.test.AndroidTestCase;

import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.model.IData;
import com.ode.sunrisechallenge.model.impl.db.DBHelper;

/**
 * Created by ode on 30/07/15.
 */
public class AccountTest extends AndroidTestCase {

    IData data;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        data = DBHelper.debugRemoveDBAndResetInstance();
    }

    public void testCreateUser() throws Exception {
        String name =  "odeknop@gmail.com";
        IAccount account = data.createAccount(name);
        assertNotNull(account);
        assertEquals(name, account.getAccountName());
    }

    public void testGetAccount() throws Exception {
        String name = "olidev@gmail.com";
        IAccount ac = data.createAccount(name);
        long id = ac.getId();
        assertEquals(id, 1);
        assertEquals(data.getAccount(id).getId(), id);
        assertEquals(data.getAccount(name), ac);
        assertNull(data.getAccount(100));
        String name2 = "odeknop@gmail.com";
        data.createAccount(name2);
        assertEquals(data.getAccount(2).getAccountName(), name2);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        DBHelper.debugRemoveDBAndResetInstance();
    }
}