package org.derewah.skelegram.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import javassist.expr.Expr;
import org.bukkit.event.Event;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ExprTelegramNewInlineButton extends SimpleExpression<InlineKeyboardButton> {

    static {
        System.out.println("registered new inline button expression");
        Skript.registerExpression(ExprTelegramNewInlineButton.class, InlineKeyboardButton.class, ExpressionType.COMBINED,
                "[a] [new] [telegram] inline [keyboard] button",
                "[a] [new] [telegram] inline [keyboard] button with text %string% with url %string%",
                "[a] [new] [telegram] inline [keyboard] button with text %string% with callback data %string%");
    }

    private Expression<String> text;
    private Expression<String> url;
    private Expression<String> data;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean delayed, ParseResult parseResult) {
        if (expressions.length == 0){
            return true;
        }
        if (expressions.length == 1){
            Skript.error("Specify both the content of the button and its callback data/url");
            return false;
        }
        if (expressions.length == 2){
            text = (Expression<String>) expressions[0];
            if(matchedPattern == 1){
                url = (Expression<String>) expressions[1];
            }else if(matchedPattern == 2){
                data = (Expression<String>) expressions[1];
            }
            return true;
        }
        return false;
    }

    @Override
    protected InlineKeyboardButton[] get(Event event) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        if(text != null){
            button.setText(text.getSingle(event));
        }
        if(url != null){
            button.setUrl(url.getSingle(event));
        }else if(data != null){
            button.setCallbackData(data.getSingle(event));
        }
        return new InlineKeyboardButton[]{button};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends InlineKeyboardButton> getReturnType() {
        return InlineKeyboardButton.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "new inline button";
    }

}
