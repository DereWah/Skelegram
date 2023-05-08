package org.derewah.skelegram.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skelegram.Skelegram;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateCallbackQuery;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.concurrent.CompletableFuture;

import static ch.njol.skript.Skript.registerEffect;

public class EffTelegramAnswerCallbackQuery extends AsyncEffect {

    static  {
        registerEffect(EffTelegramAnswerCallbackQuery.class,
                "[telegram] answer to callback query %callbackquery% with %string%",
                "[telegram] answer to callback query %callbackquery% with %string% with popup");
    }

    private Expression<CallbackQuery> exprQuery;
    private Expression<String> message;

    private boolean popup = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if(!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateCallbackQuery.class)){
            Skript.error("You're using the Answer to Callback Query effect outside of a Callback Query event.");
            return false;
        }
        exprQuery = (Expression<CallbackQuery>) expr[0];
        message = (Expression<String>) expr[1];
        popup = matchedPattern == 1;
        return true;
    }

    @Override
    protected void execute(Event event){
        if (message.getSingle(event) != null && exprQuery.getSingle(event) != null){
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(exprQuery.getSingle(event).getId());
            answerCallbackQuery.setText(message.getSingle(event));
            answerCallbackQuery.setShowAlert(popup);

            String botUser = ((BridgeTelegramUpdateCallbackQuery) event).getClient().getBotUsername();


            if (botUser != null) {
                try {
                    Skelegram.getInstance().getTelegramSessions().getBot(botUser).executeAsync(answerCallbackQuery);
                } catch (Exception e) {
                    Skript.error("Error answering callback query: " + e.getMessage());
                    Skript.error(e.toString());
                }
            } else {
                Skript.error("Could not find the bot to use.");
            }
        } else{
            Skript.error("An callback query answer can't be sent.");
        }

    }

    public String toString(Event event, boolean debug) {
        return "telegram answer with text " + message.toString(event, debug) + "to callabck query " + exprQuery.toString(event, debug);
    }

}
