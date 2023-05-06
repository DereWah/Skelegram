package org.derewah.skelegram.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.event.Event;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateCallbackQuery;
import org.derewah.skelegram.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class EvtOnTelegramCallbackQuery extends SkriptEvent {

    static {
        Skript.registerEvent("On Telegram Callback Query", EvtOnTelegramCallbackQuery.class, BridgeTelegramUpdateCallbackQuery.class,
                "[telegram] callback query [[with data] %-string%]");
        EventValues.registerEventValue(BridgeTelegramUpdateCallbackQuery.class, CallbackQuery.class, new Getter<CallbackQuery, BridgeTelegramUpdateCallbackQuery>(){
            @Override
            public CallbackQuery get(BridgeTelegramUpdateCallbackQuery event){
                return event.getUpdate().getCallbackQuery();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(BridgeTelegramUpdateCallbackQuery.class, User.class, new Getter<User, BridgeTelegramUpdateCallbackQuery>(){
            @Override
            public User get(BridgeTelegramUpdateCallbackQuery event){
                return event.getUpdate().getCallbackQuery().getFrom();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(BridgeTelegramUpdateCallbackQuery.class, Message.class, new Getter<Message, BridgeTelegramUpdateCallbackQuery>(){
            @Override
            public Message get(BridgeTelegramUpdateCallbackQuery event){
                return event.getUpdate().getCallbackQuery().getMessage();
            }
        }, EventValues.TIME_NOW);
        EventValues.registerEventValue(BridgeTelegramUpdateCallbackQuery.class, TelegramBot.class, new Getter<TelegramBot, BridgeTelegramUpdateCallbackQuery>(){
            @Override
            public TelegramBot get(BridgeTelegramUpdateCallbackQuery event){
                return event.getClient();
            }
        }, EventValues.TIME_NOW);
    }

    private Literal<String> text;
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        text = (Literal<String>) args[0];
        return true;
    }

    @Override
    public boolean check(Event e) {
        BridgeTelegramUpdateCallbackQuery event = (BridgeTelegramUpdateCallbackQuery) e;
        if(text == null){
            return true;
        }else{
            return (event.getUpdate().getCallbackQuery().getData().equals(text.getSingle()));
        }
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "on telegram callback query " + (text == null ? "generic" : "with data " + text.toString(event, debug));
    }
}
