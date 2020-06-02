package com.gameszaum.antibot.message.manager;

import com.gameszaum.antibot.message.Message;
import lombok.NonNull;
import net.md_5.bungee.config.Configuration;

import java.util.HashSet;
import java.util.Set;

public class MessageManager {

    private Set<Message> messages;
    private Configuration config;

    public MessageManager(Configuration config) {
        messages = new HashSet<>();
        this.config = config;
    }

    public String getMsg(@NonNull String id) {
        return messages.stream().filter(message -> message.getId().equalsIgnoreCase(id)).findFirst().orElse(null).getText().replaceAll("&", "§");
    }

    public void load() {
        config.getSection("messages").getKeys().forEach(s -> messages.add(new Message(s, config.getString("messages." + s))));
    }

    public Set<Message> getMessages() {
        return messages;
    }
}
