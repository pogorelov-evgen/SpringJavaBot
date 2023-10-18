package com.example.SpringJavaBot.service;


import com.example.SpringJavaBot.config.BotConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    static final String HELP_TEXT ="This bot is created to demonstrate Spring capabilities.\n\n"+
            "You can execute commands from the main menu on the left or by typing a command:\n"+
            "Type /start to see a welcome message";
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", "get a welcome message"));
        botCommandList.add(new BotCommand("/mydata", "get your data stored"));
        botCommandList.add(new BotCommand("/deletedata", "delete my data"));
        botCommandList.add(new BotCommand("/help", "info fow to use this bot"));
        botCommandList.add(new BotCommand("/setting","set your preferences"));
        try{
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e){
            log.error("Error setting bot's command list: "+ e.getMessage());
        }
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

                case("/help"):
                    sendMessage(chatId, HELP_TEXT);
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