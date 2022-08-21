package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.Referral;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.Lang;
import ai.ecma.nardabot.enums.RoleNames;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.ReferralRepo;
import ai.ecma.nardabot.repository.RoleRepo;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
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
    private final LangTextService langTextService;
    private final ChannelService channelService;
    private final SettingsService settingsService;
    private final ReferralRepo referralRepo;

    public void getUpdate(Update update) {


        //BU METHOD TELEGRAM YUBORGAN UPDATE DAN CHAT ID NI OLIB ATRIBUTEGA JOYLAYDI
        //logig
        if (update.hasMessage()) {
            if (filter(update)) {
//                lang(update);
                referral(update.getMessage());
                backService.back(update);
                messageRouter(update);
            }
        } else if (update.hasCallbackQuery()) {
            if (filter(update))
                callBackRouter(update);

        }
        channelService.deleteOrder(update);


    }

    private void lang(Update update) {
        if (update.getMessage().hasText()) {
            User user = CommonUtils.getUser();
            switch (update.getMessage().getText()) {
                case "/languz":
                    user.setLanguage(Lang.UZ);
                    break;
                case "/langen":
                    user.setLanguage(Lang.EN);
                    break;
                case "/langru":
                    user.setLanguage(Lang.RU);
                    break;
            }
        }
    }

    private void callBackRouter(Update update) {
        User user = CommonUtils.getUser();
        State value = State.values()[user.getState().ordinal()];
        switch (value) {
            case CHOICE_LANG:
                callbackQueryService.choiceLang(update);
                break;
            case EDIT_LANG:
                callbackQueryService.editLang(update,user);
            case NARDA:
                nardaService.choiceButtonsWithCallbackQuery(update);
                break;
            case HOME:
                //  historiyni ochirish homeda bolgani uchun
                callbackQueryService.deleteHistory(update);
                break;
            case SETTINGS:
                if (Objects.equals("addCard", update.getCallbackQuery().getData()))
                    settingsService.addCard(update);
                if (Objects.equals("editCard", update.getCallbackQuery().getData()))
                    settingsService.editCard(update);

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
                break;
            case SETTINGS:
                settingsService.setting(update);
                break;
            case ADD_CARD:
                settingsService.addCard(update);
                break;
            case EDIT_CARD:
                settingsService.editCard(update);
                break;

        }
    }


    private boolean filter(Update update) {
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
//        if (chatId == null) {
//            SendMessage sendMessage = new SendMessage(String.valueOf(1610694057), "wait...");
//            execute.sendMessage(sendMessage);
//        }
        //BIRINCHI KELGAN USERNI SAQLAYMIZ AGA U DATABASEDA TOPILMASA
        return saveUserIfNotFound(chatId);


    }


    private boolean saveUserIfNotFound(String chatId) {
        Optional<User> op = userRepo.findByChatId(chatId);
        if (op.isEmpty()) {
            User user = User.builder()
                    .role(roleRepo.getByName(RoleNames.ROLE_CUSTOMER))
                    .enable(true)
                    .balance(new BigDecimal("0.0"))
                    .state(State.CHOICE_LANG)
                    .build();
            user.setChatId(chatId);
            try {
                userRepo.save(user);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }
        return true;
    }

    void referral(Message message) {
        if (message.hasText() && message.getText().startsWith("/start ")) {
            String text = message.getText();
            String[] split = text.split(" ");
            if (split[1].startsWith("ref")) {
                Optional<User> op = userRepo.findByChatId(split[1].substring(3));
                if (op.isPresent()) {
                    User user = CommonUtils.getUser();
                    if (!referralRepo.existsByToUser(user) && !(user.getState().equals(State.CHOICE_LANG) || user.getState().equals(State.SEND_PHONE))){

                        Referral referral = Referral.builder()
                                .fromUser(op.get())
                                .toUser(user)
                                .build();

                        try {
                            referralRepo.save(referral);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

}
