package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final BaseService baseService;
    private final ButtonService buttonService;
    private final Execute execute;
    private final LangTextService langTextService;

    @Override
    public void start(Update update) {
        User user = CommonUtils.getUser();
        //Userga ismini berib qoyyabman
        user.setName(update.getMessage().getChat().getFirstName());

        baseService.setState(user, State.CHOICE_LANG);

        execute.sendMessage(SendMessage.builder()
                .replyMarkup(buttonService.getInlineBtn(user))
                .text("Please choice your language")
                .chatId(user.getChatId())
                .build());

    }

    @Override
    public void phoneNumber(Update update) {
        User user = CommonUtils.getUser();
        String phoneNumber = update.getMessage().getContact().getPhoneNumber();
        if (phoneNumber.charAt(0) != '+') {
            phoneNumber = "+" + phoneNumber;
        }
        user.setPhone(phoneNumber);
        baseService.setState(user, State.HOME);

        //USERGA HOME PAGENI CHIQARAMIZ
        getHome(update);
    }

    @Override
    public void getHome(Update update) {
        User user = CommonUtils.getUser();
        //USER GA HOME PAGENI YUBORYABMIZ
        SendMessage sendMessage = SendMessage.builder()
                .text(langTextService.text(user))
                .chatId(user.getChatId())
                .replyMarkup(buttonService.getBtn(user))
                .build();
        execute.sendMessage(sendMessage);
    }

}
