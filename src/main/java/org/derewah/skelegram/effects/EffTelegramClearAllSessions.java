package org.derewah.skelegram.effects;


import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skelegram.Skelegram;

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

        Skelegram.getInstance().getTelegramSessions().clearAllSessions();

    }






    public String toString(Event e, boolean debug) {
        return "telegram clear all sessions";
    }

}
