package org.derewah.skelegram.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class EvtOnTelegramMessage{

    static {
        Skript.registerEvent("On Telegram Message", SimpleEvent.class, BridgeTelegramUpdateMessage.class,
                "telegram message");
        EventValues.registerEventValue(BridgeTelegramUpdateMessage.class, Message.class, new Getter<Message, BridgeTelegramUpdateMessage>(){
            @Override
            public Message get(BridgeTelegramUpdateMessage event){
                return event.getUpdate().getMessage();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(BridgeTelegramUpdateMessage.class, User.class, new Getter<User, BridgeTelegramUpdateMessage>(){
            @Override
            public User get(BridgeTelegramUpdateMessage event){
                return event.getUpdate().getMessage().getFrom();
            }
        }, EventValues.TIME_NOW);

    }
}
