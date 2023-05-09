package org.derewah.skelegram.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ExprTelegramNewInlineButton extends SimpleExpression<InlineKeyboardButton> {

    static {
        Skript.registerExpression(ExprTelegramNewInlineButton.class, InlineKeyboardButton.class, ExpressionType.COMBINED,
                "[a] [new] [telegram] inline [keyboard] button with text %string%",
                "[a] [new] [telegram] inline [keyboard] button with text %string% with url %string%",
                "[a] [new] [telegram] inline [keyboard] button with text %string% with callback data %string%",
                "[a] [new] [telegram] inline [keyboard] button with text %string% with switch inline %string%",
                "[a] [new] [telegram] inline [keyboard] button with text %string% with switch inline current chat %string%");
    }

    private Expression<String> text;
    private Expression<String> url;
    private Expression<String> data;
    private Expression<String> switchInline;
    private Expression<String> switchInlineCurrentChat;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean delayed, ParseResult parseResult) {
        text = (Expression<String>) expressions[0];
        if(matchedPattern == 1){
            url = (Expression<String>) expressions[1];
        }else if(matchedPattern == 2){
            data = (Expression<String>) expressions[1];
        } else if(matchedPattern == 3){
            switchInline = (Expression<String>) expressions[1];
        } else if(matchedPattern == 4){
            switchInlineCurrentChat = (Expression<String>) expressions[1];
        }
        return true;
    }

    @Override
    protected InlineKeyboardButton[] get(Event event) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        if(text != null){
            button.setText(text.getSingle(event));
        }
        if(url != null){
            button.setUrl(url.getSingle(event));
        }else if(data != null) {
            button.setCallbackData(data.getSingle(event));
        }else if(switchInline != null) {
            button.setSwitchInlineQuery(switchInline.getSingle(event));
        }else if(switchInlineCurrentChat != null){
            button.setSwitchInlineQueryCurrentChat(switchInlineCurrentChat.getSingle(event));
        }else{
            button.setCallbackData("Skelegram");
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
