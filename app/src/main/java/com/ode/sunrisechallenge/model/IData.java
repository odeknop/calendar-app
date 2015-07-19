package com.ode.sunrisechallenge.model;

import java.io.Closeable;

/**
 * Created by ode on 26/06/15.
 */
public interface IData extends Closeable {

    IAccount getAccount(long accountId);
    IAccount getAccount(String name);
    IAccount createAccount(String name);
    void deleteAccount(IAccount account);
}