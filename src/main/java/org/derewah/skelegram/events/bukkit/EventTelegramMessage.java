package org.derewah.skelegram.events.bukkit;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.telegram.telegrambots.meta.api.objects.Update;

public class EventTelegramMessage extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Update update;


    public EventTelegramMessage(Update update) {
        this.update = update;
    }

    public Update getUpdate() {
        return update;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
