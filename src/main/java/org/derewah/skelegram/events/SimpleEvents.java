package org.derewah.skelegram.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import org.derewah.skelegram.events.bukkit.EventTelegramMessage;


public class SimpleEvents{

    static {
        Skript.registerEvent("Telegram Message", SimpleEvent.class, EventTelegramMessage.class,
                "telegram message [event]");
    }

}
