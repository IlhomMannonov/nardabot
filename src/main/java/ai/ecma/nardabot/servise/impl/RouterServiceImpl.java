package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.RoleNames;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.RoleRepo;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouterServiceImpl implements RouterService {
    private final NardaService nardaService;
    private final AuthService authService;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final CallbackQueryService callbackQueryService;
    private final HomeService homeService;
    private final BaseService baseService;
    private final BackService backService;
    private final PaymentService paymentService;
    private final Execute execute;

    public void getUpdate(Update update) {
        //BU METHOD TELEGRAM YUBORGAN UPDATE DAN CHAT ID NI OLIB ATRIBUTEGA JOYLAYDI
        filter(update);
        //logig
        if (update.hasMessage()) {
            backService.back(update);
            messageRouter(update);
        } else if (update.hasCallbackQuery()) {
            callBackRouter(update);
        }
    }

    private void callBackRouter(Update update) {
        User user = CommonUtils.getUser();
        State value = State.values()[user.getState().ordinal()];
        switch (value) {
            case CHOICE_LANG:
                callbackQueryService.choiceLang(update);
                break;
            case NARDA:
                nardaService.choiceButtonsWithCallbackQuery(update);

        }

    }

    private void messageRouter(Update update) {
        User user = CommonUtils.getUser();
        if (user.getPhone() != null && Objects.equals(update.getMessage().getText(), "/start")) {
            baseService.setState(user, State.HOME);
            authService.getHome(update);
        }
        State value = State.values()[user.getState().ordinal()];
        switch (value) {
            case CHOICE_LANG:
                authService.start(update);
                break;
            case HOME:
                homeService.home(update);
                break;
            case SEND_PHONE:
                if (update.getMessage().hasContact())
                    authService.phoneNumber(update);
                break;
            case GAMES:
                nardaService.nardaRouter(update);
                break;
            case NARDA:
                nardaService.editBalanceWithMessage(update);
                break;
            case PAYMENT_ENTER_SUM:
                paymentService.enterSum(update);
                break;
            case WITHDRAW:
                paymentService.enterWithdrawSum(update);
        }
    }


    void filter(Update update) {
        HttpServletRequest httpServletRequest = CommonUtils.currentRequest();
        String chatId = null;
        try {
            chatId = String.valueOf(update.getMessage().getChatId());
        } catch (RuntimeException ignored) {
        }
        try {
            chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        } catch (RuntimeException ignored) {
        }
        httpServletRequest.setAttribute(Constant.KEY, chatId);
        if (chatId == null) {
            SendMessage sendMessage = new SendMessage(String.valueOf(1610694057), "wait...");
            execute.sendMessage(sendMessage);
        }
        //BIRINCHI KELGAN USERNI SAQLAYMIZ AGA U DATABASEDA TOPILMASA
        saveUserIfNotFound(chatId);

    }


    private void saveUserIfNotFound(String chatId) {
        Optional<User> op = userRepo.findByChatId(chatId);
        if (op.isEmpty()) {
            userRepo.save(User.builder()
                    .role(roleRepo.getByName(RoleNames.ROLE_CUSTOMER))
                    .enable(true)
                    .chatId(chatId)
                    .balance(new BigDecimal("0.0"))
                    .state(State.CHOICE_LANG)
                    .build());
        }
    }
}
