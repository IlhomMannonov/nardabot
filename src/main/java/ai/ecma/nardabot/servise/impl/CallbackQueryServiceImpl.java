package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.Lang;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class CallbackQueryServiceImpl implements CallbackQueryService {
    private final UserRepo userRepo;
    private final Execute execute;
    private final ButtonService buttonService;
    private final LangTextService langTextService;

    private final BaseService baseService;

    @Override
    public void start(Update update) {
        User user = CommonUtils.getUser();

    }

    @Override
    public void choiceLang(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        User user = CommonUtils.getUser();
        String data = update.getCallbackQuery().getData();
        if (data.equals(Lang.UZ.name()))
            user.setLanguage(Lang.UZ);
        else if (data.equals(Lang.EN.name()))
            user.setLanguage(Lang.EN);
        else if (data.equals(Lang.RU.name()))
            user.setLanguage(Lang.RU);

        //USERNI STATENI KEYINGI HOLATGA OZGARTRAMIZ
        baseService.setState(user, State.SEND_PHONE);

        //ESKi HABARNI OCHIRAMIZ
        execute.deleteMessage(callbackQuery.getMessage().getMessageId(), user.getChatId());

        //USERGA YANGI HABAR JONATAMIZ SHUNDAN SONG
        SendMessage sendMessage = SendMessage.builder()
                .replyMarkup(buttonService.getBtn(user))
                .text(langTextService.buttonText(user))
                .chatId(user.getChatId())
                .build();
        execute.sendMessage(sendMessage);
    }

}
