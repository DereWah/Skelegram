package org.derewah.skelegram.events.bukkit;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.derewah.skelegram.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BridgeTelegramUpdateMessage extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Update update;
    private final TelegramBot client;


    public BridgeTelegramUpdateMessage(Update update, TelegramBot client) {
        this.update = update;
        this.client = client;
    }

    public Update getUpdate() {
        return update;
    }

    public TelegramBot getClient() { return client;}

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
