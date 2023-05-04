package org.derewah.skelegram.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateCallbackQuery;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ExprTelegramCallbackQuery extends SimpleExpression<CallbackQuery> {

    static {
        Skript.registerExpression(ExprTelegramCallbackQuery.class, CallbackQuery.class, ExpressionType.SIMPLE, "[event-]callback query");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (!ParserInstance.get().isCurrentEvent(BridgeTelegramUpdateCallbackQuery.class) && parseResult.hasTag("event")) {
            Skript.error("You cannot use event-callback query outside of a Telegram CallbackQuery event.");
            return false;
        }
        return true;
    }

    @Override
    protected CallbackQuery[] get(Event event) {
        if (event instanceof BridgeTelegramUpdateMessage) {
            CallbackQuery query = ((BridgeTelegramUpdateMessage)event).getUpdate().getCallbackQuery();
            return new CallbackQuery[]{query};
        }
        return new CallbackQuery[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends CallbackQuery> getReturnType() {
        return CallbackQuery.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "event-callback query";
    }

}
