package com.gameszaum.antibot.manager.impl;

import com.gameszaum.antibot.account.Account;
import com.gameszaum.antibot.manager.AccountManager;
import com.gameszaum.core.other.database.mysql.MySQLService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class AccountManagerImpl implements AccountManager {

    private Set<Account> accounts;
    private MySQLService mySQLService;

    public AccountManagerImpl(MySQLService mySQLService) {
        this.mySQLService = mySQLService;
        this.accounts = new HashSet<>();
    }

    @Override
    public Set<Account> all() {
        return accounts;
    }

    @Override
    public void loadAccounts() {
        try {
            PreparedStatement ps = mySQLService.getConnection().prepareStatement("SELECT * FROM `games_antibot`;");
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                create(new Account(resultSet.getString("ip"), resultSet.getBoolean("proxy")));
            }
            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAccounts() {
        accounts.forEach(account -> {
            if (!mySQLService.contains("games_antibot", "ip", account.getIp())) {
                mySQLService.executeQuery("INSERT INTO `games_antibot` (`ip`, `proxy`) VALUES ('" + account.getIp() + "', '" + account.isProxy() + "');");
            }
        });
    }

    @Override
    public void create(Account account) {
        accounts.add(account);
    }

    @Override
    public void remove(String s) {
        accounts.remove(get(s));
    }

    @Override
    public Account get(String s) {
        return search(s).findFirst().orElse(null);
    }

    @Override
    public Stream<Account> search(String s) {
        return accounts.stream().filter(account -> account.getIp().equals(s));
    }
}
