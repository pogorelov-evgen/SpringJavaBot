package com.example.SpringJavaBot.service;


import com.example.SpringJavaBot.config.BotConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String massegeText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (massegeText) {
                case ("/start"):
                           startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
                            break;
                case("/new"): sendMessage(chatId,"Sorry");
                    break;

                default: sendMessage(chatId,"Sorry, command was not recognized");

            }
        }

    }
    private void startCommandReceived(long chatId, String name){


        String answer = "Hi, " + name + ", nice to meet you!";
        log.info("Info about: " + name);

        sendMessage(chatId,answer);

    }
    private void sendMessage(long chatId, String textToSend)  {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        }
        catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }
}