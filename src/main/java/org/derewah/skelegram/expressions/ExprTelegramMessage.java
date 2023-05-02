package org.derewah.skelegram.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ExprTelegramMessage extends SimpleExpression<Message> {


    static {
        Skript.registerExpression(ExprTelegramMessage.class, Message.class, ExpressionType.SIMPLE, "[event-]telegram message");
    }



    @Override
    protected Message[] get(Event event) {
        if (event instanceof BridgeTelegramUpdateMessage) {
            Message message = ((BridgeTelegramUpdateMessage)event).getUpdate().getMessage();
            return new Message[]{message};
        }else{
            return new Message[0];
        }
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
    public String toString(@Nullable Event event, boolean b) {
        return "event-telegrammessage";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateMessage.class)) {
            Skript.error("You cannot use event-telegram message outside of a TelegramMessage event.");
            return false;
        }
        return true;
    }
}