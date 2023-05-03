package org.derewah.skelegram.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateCallbackQuery;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;


public class SimpleEvents{

    static {
        Skript.registerEvent("Telegram Message", SimpleEvent.class, BridgeTelegramUpdateMessage.class,
                "telegram message [event]");
        Skript.registerEvent("Telegram Callback Query", SimpleEvent.class, BridgeTelegramUpdateCallbackQuery.class,
                "telegram callback query [event]");
    }

}
