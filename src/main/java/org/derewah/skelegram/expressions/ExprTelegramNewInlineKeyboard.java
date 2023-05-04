package org.derewah.skelegram.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ExprTelegramNewInlineKeyboard extends SimpleExpression<InlineKeyboardMarkup> {

    static {
        Skript.registerExpression(ExprTelegramNewInlineKeyboard.class, InlineKeyboardMarkup.class, ExpressionType.SIMPLE, "[a] [new] [telegram] inline keyboard [markup] [with button[s] %inlinebuttons%]");
    }

    private Expression<InlineKeyboardButton> exprRow;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean delayed, ParseResult parseResult) {

        if (expressions.length > 0){
            exprRow = (Expression<InlineKeyboardButton>) expressions[0];
        }
        return true;
    }

    @Override
    protected InlineKeyboardMarkup[] get(Event event) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> matrix = List.of(List.of(exprRow.getAll(event)));
        keyboard.setKeyboard(matrix);
        return new InlineKeyboardMarkup[]{keyboard};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends InlineKeyboardMarkup> getReturnType() {
        return InlineKeyboardMarkup.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "new inline button";
    }

}
