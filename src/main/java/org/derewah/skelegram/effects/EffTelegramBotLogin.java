package org.derewah.skelegram.effects;



import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;



import ch.njol.util.Kleenean;

import org.bukkit.event.Event;

import org.derewah.skelegram.telegram.TelegramBot;
import org.derewah.skelegram.telegram.TelegramSessions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static ch.njol.skript.Skript.registerEffect;


public class EffTelegramBotLogin extends Effect {

    static  {
        registerEffect(EffTelegramBotLogin.class,
                "telegram login to bot %string% with token %string%");
    }

    private Expression<String> username;
    private Expression<String> token;







    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        username = (Expression<String>) expr[0];
        token = (Expression<String>) expr[1];
        return true;
    }


    @Override
    protected void execute(Event event){
        if (username.getSingle(event) != null && token.getSingle(event) != null){
            try {


                BotSession sess = TelegramSessions.sessions.get(username.getSingle(event));
                if (sess != null){
                    sess.stop();
                }

                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                TelegramBot bot = new TelegramBot();

                bot.token = token.getSingle(event);
                bot.username = username.getSingle(event);

                sess = botsApi.registerBot(bot);

                TelegramSessions.sessions.put(username.getSingle(event), sess);

            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }

    }






    public String toString(Event e, boolean debug) {
        return "telegram login to bot " + username.toString(e, debug) + "with token \"" + token.toString(e, debug) + "\"";
    }

}
