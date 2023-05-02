package org.derewah.skelegram.telegram;

import org.derewah.skelegram.events.bukkit.EventTelegramMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

;

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
        if (update.hasMessage()){
            new EventTelegramMessage(update);
        }
    }
}
