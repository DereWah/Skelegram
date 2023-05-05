package org.derewah.skelegram.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skelegram.Skelegram;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateCallbackQuery;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ExprTelegramLastSentMessage extends SimpleExpression<Message> {

    static {
        Skript.registerExpression(ExprTelegramLastSentMessage.class, Message.class, ExpressionType.SIMPLE, "[the] last sent message [with bot %-string%]");
    }

    private Expression<String> exprBot;
    private boolean specifyBot;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprBot = (Expression<String>) expressions[0];
        specifyBot = parseResult.expr.contains(" with bot ");
        if(!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateMessage.class, BridgeTelegramUpdateCallbackQuery.class) && !specifyBot){
            Skript.error("You're using the Last Sent Telegram Message expression outside of a Telegram event. Specify the username of the bot you sent a message from.");
            return false;
        }
        return true;
    }

    @Override
    protected Message[] get(Event event) {
        if(!specifyBot){
            if(event instanceof BridgeTelegramUpdateCallbackQuery){
                return new Message[]{((BridgeTelegramUpdateCallbackQuery) event).getClient().lastSent};
            } else if (event instanceof BridgeTelegramUpdateMessage) {
                return new Message[]{((BridgeTelegramUpdateMessage) event).getClient().lastSent};
            }
        }else{
            if (Skelegram.getInstance().getTelegramSessions().getBot(exprBot.getSingle(event)) != null) {
                return new Message[]{Skelegram.getInstance().getTelegramSessions().getBot(exprBot.getSingle(event)).lastSent};
            }else{
                Skript.error("Could not find a session with bot " + exprBot.getSingle(event) + ". Did you authenticate the bot?");
            }

        }
        return new Message[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Message> getReturnType() {
        return Message.class;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "new telegram message with text " + exprBot.toString(event, debug);
    }
}
