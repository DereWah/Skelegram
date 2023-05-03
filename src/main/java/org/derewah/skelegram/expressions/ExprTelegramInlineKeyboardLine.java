package org.derewah.skelegram.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.Arrays;
import java.util.List;

public class ExprTelegramInlineKeyboardLine extends SimpleExpression<InlineKeyboardButton[]> {

    static {
        Skript.registerExpression(ExprTelegramInlineKeyboardLine.class, InlineKeyboardButton[].class, ExpressionType.PROPERTY, "[the] %number%(st|nd|rd|th) line of [the] [inline] keyboard %inlinekeyboard%");
    }

    private Expression<Number> lineNumber;
    private Expression<InlineKeyboardMarkup> kb;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        lineNumber = (Expression<Number>) expressions[0];
        kb = (Expression<InlineKeyboardMarkup>) expressions[1];
        return true;
    }

    @Override
    public InlineKeyboardButton[][] get(final Event e) {
        final InlineKeyboardMarkup keyboard = kb.getSingle(e);
        Number n = lineNumber != null ? lineNumber.getSingle(e) : null;
        if (n.intValue() < 0){
            n = 0;
        }
        if (n != null && keyboard != null && keyboard.getKeyboard().get(n.intValue()) != null) {
            return new InlineKeyboardButton[][]{keyboard.getKeyboard().get(n.intValue()).toArray(InlineKeyboardButton[]::new)};
        }
        return new InlineKeyboardButton[0][0];
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode){
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE) {return CollectionUtils.array(InlineKeyboardMarkup[].class);}
        else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET){return CollectionUtils.array(null);}
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if((delta == null  && mode != Changer.ChangeMode.RESET && mode != Changer.ChangeMode.DELETE) || (delta.length == 0)){
            return;
        }
        List<List<InlineKeyboardButton>> keyboard = kb.getSingle(e).getKeyboard();
        Integer n = lineNumber.getSingle(e).intValue();
        if(keyboard != null){
            List<InlineKeyboardButton> line = keyboard.get(n);
            List<InlineKeyboardButton> deltaLine = Arrays.asList((InlineKeyboardButton[]) delta);
            switch (mode) {
                case SET:
                    keyboard.set(n, deltaLine);
                case RESET:
                    keyboard.set(n, null);
                case DELETE:
                    keyboard.set(n, null);
                case ADD:
                    line.addAll(deltaLine);
                    keyboard.set(n, line);
                case REMOVE:
                    line.removeAll(deltaLine);
                    keyboard.set(n, line);
            }
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends InlineKeyboardButton[]> getReturnType(){
        return InlineKeyboardButton[].class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return ("the line " + lineNumber.toString(event, b) + "of the inline keyboard " + kb.toString(event, b));
    }
}
