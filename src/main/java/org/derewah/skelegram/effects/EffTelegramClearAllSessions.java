package org.derewah.skelegram.effects;


import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skelegram.telegram.TelegramBot;
import org.derewah.skelegram.telegram.TelegramSessions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static ch.njol.skript.Skript.registerEffect;


public class EffTelegramClearAllSessions extends AsyncEffect {

    static  {
        registerEffect(EffTelegramClearAllSessions.class,
                "clear all telegram sessions");
    }



    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {

        return true;
    }


    @Override
    protected void execute(Event event){
        for(BotSession sess : TelegramSessions.sessions.values()){
            sess.stop();
        }

    }






    public String toString(Event e, boolean debug) {
        return "telegram clear all sessions";
    }

}
