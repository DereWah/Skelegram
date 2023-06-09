package org.derewah.skelegram.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import ch.njol.util.Pair;
import org.bukkit.event.Event;
import org.derewah.skelegram.Skelegram;
import org.derewah.skelegram.telegram.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class EffTelegramBotLogin extends AsyncEffect {

    static  {
        Skript.registerEffect(EffTelegramBotLogin.class,
                "telegram login to bot %string% with token %string%");
    }

    private Expression<String> exprBotUser;
    private Expression<String> token;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBotUser = (Expression<String>) expr[0];
        token = (Expression<String>) expr[1];
        return true;
    }

    @Override
    protected void execute(Event event){
        if (exprBotUser.getSingle(event) != null && token.getSingle(event) != null){
            if(Skelegram.getInstance().getTelegramSessions().getBot(exprBotUser.getSingle(event)) == null) {
                try {
                    TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                    TelegramBot bot = new TelegramBot();
                    bot.token = token.getSingle(event);
                    bot.username = exprBotUser.getSingle(event);
                    BotSession sess = botsApi.registerBot(bot);
                    Pair<BotSession, TelegramBot> pair = new Pair<>(sess, bot);
                    Skelegram.getInstance().getTelegramSessions().sessions.put(exprBotUser.getSingle(event), pair);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else{
                Skript.error("An active session with the telegram bot " + exprBotUser.getSingle(event) + " already exists.");
                }
        }
    }

    public String toString(Event event, boolean debug) {
        return "telegram login to bot " + exprBotUser.toString(event, debug) + "with token " + token.toString(event, debug);
    }

}
