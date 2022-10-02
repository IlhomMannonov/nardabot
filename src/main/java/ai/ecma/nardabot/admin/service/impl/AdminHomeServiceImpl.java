package ai.ecma.nardabot.admin.service.impl;

import ai.ecma.nardabot.admin.service.abs.AdminHomeService;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.servise.abs.BaseService;
import ai.ecma.nardabot.servise.abs.ButtonService;
import ai.ecma.nardabot.servise.abs.Execute;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonPollType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminHomeServiceImpl implements AdminHomeService {
    private final Execute execute;
    private final ButtonService buttonService;
    private final BaseService baseService;

    @Override
    public void home(Update update, User user) {
        baseService.setState(user, State.ADMIN_HOME);
        SendMessage sendMessage = SendMessage.builder()
                .text(user.getName() + " Xush kelibisiz")
                .chatId(user.getChatId())
                .replyMarkup(buttonService.getAdminButton(user))
                .build();

        execute.sendMessage(sendMessage);
    }

    @Override
    public void dashboard(Update update, User user) {

    }

    @Override
    public void ads(Update update, User user) {
        baseService.setState(user, State.ADS_HOME);
        SendMessage sendMessage = SendMessage.builder()
                .text("reklama qo'yish bo'limi")
                .chatId(user.getChatId())
                .replyMarkup(buttonService.getAdminButton(user))
                .build();
        execute.sendMessage(sendMessage);
    }

    @Override
    public void topUsers(Update update, User user) {

    }

    @Override
    public void homeRouter(Update update, User user) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                switch (text) {
                    case "Dashboard":
                        dashboard(update, user);
                        break;
                    case "Ads":
                        ads(update, user);
                        break;
                    case "Top users":
                        topUsers(update, user);
                        break;
                }
            }

        } else if (update.hasCallbackQuery()) {
            //logic
        }
    }
}
