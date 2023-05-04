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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExprTelegramInlineKeyboardRow extends SimpleExpression<InlineKeyboardButton> {

    static {
        Skript.registerExpression(ExprTelegramInlineKeyboardRow.class, InlineKeyboardButton.class, ExpressionType.COMBINED, "[the] %number%(st|nd|rd|th) row of [the] [inline] keyboard %inlinekeyboard%");
    }

    private Expression<Number> rowNumber;
    private Expression<InlineKeyboardMarkup> kb;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        rowNumber = (Expression<Number>) expressions[0];
        kb = (Expression<InlineKeyboardMarkup>) expressions[1];
        return true;
    }

    @Override
    public InlineKeyboardButton[] get(final Event e) {
        final InlineKeyboardMarkup keyboard = kb.getSingle(e);
        Integer n = rowNumber != null ? rowNumber.getSingle(e).intValue()-1 : null;
        assert n != null;
        if (n < 0){
            n = 0;
        }
        if (keyboard != null && keyboard.getKeyboard().get(n) != null) {

            return (InlineKeyboardButton[]) keyboard.getKeyboard().get(n).toArray(); //TBD FIX ClassCastError!
        }
        return new InlineKeyboardButton[0];
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode){
        switch (mode){
            case SET:
            case ADD:
            case REMOVE:
                return CollectionUtils.array(InlineKeyboardMarkup[].class);
            case DELETE:
            case RESET:
                return null;
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if((delta == null  && mode != Changer.ChangeMode.RESET && mode != Changer.ChangeMode.DELETE) || (delta.length == 0)){
            return;
        }
        List<List<InlineKeyboardButton>> keyboard = kb.getSingle(e).getKeyboard();
        Integer n = rowNumber != null ? rowNumber.getSingle(e).intValue()-1 : null;
        assert n != null;
        if(n < 0){
            n = 0;
        }else if(n >= keyboard.toArray().length){
            n = keyboard.toArray().length-1;
        }
        if(keyboard != null){
            List<InlineKeyboardButton> row = keyboard.get(n);
            List<InlineKeyboardButton> deltaLine = Arrays.asList((InlineKeyboardButton[]) delta);
            switch (mode) {
                case SET:
                    keyboard.set(n, deltaLine);
                    break;
                case RESET:
                case DELETE:
                    keyboard.set(n, null);
                    break;
                case ADD:
                    row.addAll(deltaLine);
                    keyboard.set(n, row);
                    break;
                case REMOVE:
                    row.removeAll(deltaLine);
                    keyboard.set(n, row);
                    break;
            }
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends InlineKeyboardButton> getReturnType(){
        return InlineKeyboardButton.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return ("the row " + rowNumber.toString(event, b) + "of the inline keyboard " + kb.toString(event, b));
    }
}
