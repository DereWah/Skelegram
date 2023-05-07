package org.derewah.skelegram.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skelegram.Skelegram;

import static ch.njol.skript.Skript.registerEffect;

public class EffTelegramClearSession extends AsyncEffect {

    static  {
        registerEffect(EffTelegramClearSession.class,
                "(clear|stop) telegram session of bot %string%");
    }

    private Expression<String> exprBotUser;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBotUser = (Expression<String>) expr[0];
        return true;
    }

    @Override
    protected void execute(Event event){
        if(Skelegram.getInstance().getTelegramSessions().getBot(exprBotUser.getSingle(event)) != null){
            Skelegram.getInstance().getTelegramSessions().stopSession(exprBotUser.getSingle(event));
        }else{
            Skript.error("Could not find session "+ exprBotUser.getSingle(event)+". Did you authenticate the bot?");
        }

    }

    public String toString(Event event, boolean debug) {
        return "telegram clear session of bot "+ exprBotUser.toString(event, debug);
    }

}
