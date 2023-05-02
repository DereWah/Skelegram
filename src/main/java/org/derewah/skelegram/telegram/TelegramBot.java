package org.derewah.skelegram.telegram;

import ch.njol.skript.Skript;
import org.bukkit.Bukkit;
import org.derewah.skelegram.Skelegram;
import org.derewah.skelegram.events.bukkit.BridgeTelegramUpdateMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


;import java.util.concurrent.Callable;

public class TelegramBot extends TelegramLongPollingBot {

    public String username;
    public String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken(){
        return token;
    }

    @Override
    public void onUpdateReceived(Update update){
        try {
            if (update.hasMessage()) {
                Bukkit.getScheduler().callSyncMethod(Skelegram.getInstance(), (Callable) () -> {
                    Bukkit.getPluginManager().callEvent(new BridgeTelegramUpdateMessage(update));
                    return null;
                });
            }
        }catch(Exception e){
            Skript.error(e.toString());
        }
    }
}