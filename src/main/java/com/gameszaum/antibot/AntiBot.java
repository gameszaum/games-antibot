package com.gameszaum.antibot;

import com.gameszaum.antibot.checker.Checker;
import com.gameszaum.antibot.listener.Listeners;
import com.gameszaum.antibot.manager.AccountManager;
import com.gameszaum.antibot.manager.impl.AccountManagerImpl;
import com.gameszaum.antibot.message.manager.MessageManager;
import com.gameszaum.core.bungee.plugin.GamesBungee;
import com.gameszaum.core.other.database.DatabaseCredentials;
import com.gameszaum.core.other.database.mysql.MySQLService;
import com.gameszaum.core.other.database.mysql.MySQLServiceImpl;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Getter
public final class AntiBot extends GamesBungee {

    private Configuration config;
    private AccountManager accountManager;
    private MySQLService mySQLService;
    private Checker checker;
    private MessageManager messageManager;

    @Getter
    private static AntiBot instance;

    @Override
    public void load() {
        instance = this;
    }

    @Override
    public void enable() {
        loadConfig();

        mySQLService = new MySQLServiceImpl(config.getString("plugin-prefix"));
        mySQLService.createConnection(new DatabaseCredentials(config.getString("mysql.host"), config.getString("mysql.db"),
                config.getString("mysql.user"), config.getString("mysql.pass"), config.getInt("mysql.port")));
        mySQLService.executeQuery("CREATE TABLE IF NOT EXISTS `games_antibot` (`ip` VARCHAR(40), `proxy` BOOL);");

        accountManager = new AccountManagerImpl(mySQLService);
        accountManager.loadAccounts();

        checker = new Checker();
        messageManager = new MessageManager(getConfig());
        messageManager.load();

        registerListener(new Listeners());

        System.out.println("[" + config.getString("plugin-prefix") + "] enabled.");
    }

    @Override
    public void disable() {
        accountManager.saveAccounts();
        mySQLService.closeConnection();

        System.out.println("[" + config.getString("plugin-prefix") + "] disabled.");
    }

    private void loadConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream stream = getResourceAsStream("config.yml")) {
                Files.copy(stream, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}