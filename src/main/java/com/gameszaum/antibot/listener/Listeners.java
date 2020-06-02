package com.gameszaum.antibot.listener;

import com.gameszaum.antibot.AntiBot;
import com.gameszaum.antibot.account.Account;
import com.gameszaum.antibot.exception.InvalidCheckException;
import com.gameszaum.antibot.manager.AccountManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Listeners implements Listener {

    private AntiBot antiBot;
    private AccountManager accountManager;

    public Listeners() {
        antiBot = AntiBot.getInstance();
        accountManager = antiBot.getAccountManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void login(LoginEvent event) {
        PendingConnection connection = event.getConnection();

        if (connection == null) return;
        if (connection.getVirtualHost() == null) return;

        String ip = connection.getVirtualHost().getHostName();

        if (accountManager.get(ip) != null) {
            if (accountManager.get(ip).isProxy()) {
                event.setCancelled(true);
            }
            return;
        }
        try {
            accountManager.create(new Account(ip, AntiBot.getInstance().getChecker().check(ip)));
        } catch (InvalidCheckException e) {
            e.printStackTrace();
            event.setCancelled(true);
            event.setCancelReason(TextComponent.fromLegacyText(antiBot.getMessageManager().getMsg("impossible-check")));
            return;
        }
        if (accountManager.get(ip).isProxy()) {
            event.setCancelled(true);
        }
    }

}
