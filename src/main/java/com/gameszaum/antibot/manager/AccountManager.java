package com.gameszaum.antibot.manager;

import com.gameszaum.antibot.account.Account;
import com.gameszaum.core.other.services.Model;

import java.util.Set;

public interface AccountManager extends Model<String, Account> {

    Set<Account> all();

    void loadAccounts();

    void saveAccounts();

}
