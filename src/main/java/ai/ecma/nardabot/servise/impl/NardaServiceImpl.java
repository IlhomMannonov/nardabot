package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.PayHistory;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.entity.narda.Narda;
import ai.ecma.nardabot.enums.NardaType;
import ai.ecma.nardabot.enums.PayStatus;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.payload.NardaState;
import ai.ecma.nardabot.payload.SendPhoto;
import ai.ecma.nardabot.repository.NardaRepo;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class NardaServiceImpl implements NardaService {
    private final UserRepo userRepo;
    private final LangTextService langTextService;
    private final BaseService baseService;
    private final NardaRepo nardaRepo;
    private final ButtonService buttonService;
    private final Execute execute;


    @Override
    public void nardaGame(Update update) {
        User user = CommonUtils.getUser();
        baseService.setState(user, State.NARDA);

        Narda narda = nardaRepo.findByUserId(user.getId()).orElse(
                Narda.builder()
                        .user(user)
                        .type(NardaType.LOWER)
                        .amount(Constant.MIN_BET)
                        .build()
        );
        nardaRepo.save(narda);
        SendPhoto sendPhoto = SendPhoto.builder()
                .photo("AgACAgIAAxkBAAIJhmJxQH1IYJTRPj4t6pA-HIMnno3bAALZuzEb7beISx0lVE7RxpMAAQEAAwIAA3kAAyQE")
                .caption(langTextService.text(user))
                .replyMarkup(buttonService.getInlineBtn(user, ""))
                .chatId(user.getChatId())
                .build();
        execute.sendPhoto(sendPhoto);
    }

    @Override
    public void nardaRouter(Update update) {
        User user = CommonUtils.getUser();
        String text = update.getMessage().getText();
        String narda = langTextService.getTxt(user, "Narda", "Backgammon", "Нарды");
        if (Objects.equals(text, narda)) {
            nardaGame(update);
        }
    }

    @Override
    public void choiceButtonsWithCallbackQuery(Update update) {
        User user = CommonUtils.getUser();
        Narda narda = nardaRepo.findByUserId(user.getId()).orElseThrow();
        String[] data = update.getCallbackQuery().getData().split(":");
        if (data.length >= 2) {
            if (Objects.equals(data[1], "edit")) {
                //BU METHOD SUMMANI KOPAYTIRISH YOKI KAMAYTIRISH UCHUN ISHLATILADI
                editAction(data[2], narda);
                nardaRepo.save(narda);
                editMessage(update, user);

            } else if (Objects.equals(data[1], "game")) {
                //BU METHOD OYIN STATUSINI TANLASH UCHUN: OVWER, LOVER, MIDDLE
                boolean isUpdate = editStatus(data[2], narda);
                nardaRepo.save(narda);
                editMessage(update, user);

            } else if (Objects.equals(data[1], "start")) {
                startGame(update);
            }
        }
    }

    @Override
    public void editBalanceWithMessage(Update update) {
        User user = CommonUtils.getUser();
        Narda narda = nardaRepo.getByUserId(user.getId());
        String text = update.getMessage().getText();
        BigDecimal amount = narda.getAmount();
        try {
            amount = new BigDecimal(text);
        } catch (Exception ignored) {
        }
        try {
            int compareTo = Constant.MAX_BET.compareTo(new BigDecimal(text));
            int compare = new BigDecimal(text).compareTo(Constant.MIN_BET);
            if (compareTo > 0 && compare > 0) {
                narda.setAmount(amount);
                nardaRepo.save(narda);
                nardaGame(update);
            }
        } catch (Exception ignore) {
        }

    }

    private boolean editStatus(String data, Narda narda) {
        switch (data) {
            case "over":
                if (Objects.equals(narda.getType(), NardaType.OVER))
                    return false;
                narda.setType(NardaType.OVER);
                break;

            case "middle":
                if (Objects.equals(narda.getType(), NardaType.MIDDLE))
                    return false;
                narda.setType(NardaType.MIDDLE);
                break;

            case "lower":
                if (Objects.equals(narda.getType(), NardaType.LOWER))
                    return false;
                narda.setType(NardaType.LOWER);
                break;

        }
        return true;
    }

    private boolean editAction(String data, Narda narda) {
        switch (data) {
            case "min":
                narda.setAmount(Constant.MIN_BET);
                return !Objects.equals(Constant.MIN_BET, narda.getAmount());
            case "max":
                narda.setAmount(Constant.MAX_BET);
                return !Objects.equals(Constant.MAX_BET, narda.getAmount());
            case "x2":
                //GAROV MIQDORI CHEKLANGAN MIQDORDAN OSHSA QAYTARAMIZ
                int compareTo = Constant.MAX_BET.compareTo(narda.getAmount().add(narda.getAmount()));
                if (compareTo > 0) {
                    narda.setAmount(narda.getAmount().add(narda.getAmount()));
                    return true;
                }
                break;
            case "x/2":
                int compare = narda.getAmount().compareTo(Constant.MIN_BET);
                if (compare > 0) {
                    narda.setAmount(narda.getAmount().divide(new BigDecimal(2)));
                    return true;
                }
                break;
        }
        return false;
    }

    private void editMessage(Update update, User user) {
        EditMessageCaption editMessageCaption = EditMessageCaption.builder()
                .caption(langTextService.text(user))
                .replyMarkup(buttonService.getInlineBtn(user, ""))
                .chatId(user.getChatId())
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .build();
        execute.editMessageCaption(editMessageCaption);
    }

    private void startGame(Update update) {
        User user = CommonUtils.getUser();
        Narda narda = nardaRepo.getByUserId(user.getId());
        NardaState nardaState = generateGame(narda.getType());
        boolean isUpdate = false;
        String text = null;
        String maglubiyat = langTextService.getTxt(user, "  \uD83D\uDE14 Omadsizlik \n taktor urinib ko'ring", "   \uD83D\uDE14 Поражение\nПожалуйста, попробуйте еще раз", "  \uD83D\uDE14 Fail\ntry the tractor");
        int compareTo = user.getBalance().compareTo(narda.getAmount());
        if (compareTo < 0) {
            text = langTextService.getTxt(user, "Hisobingizda mablag' yetarli emas", "На вашем счету недостаточно денег", "There is not enough money in your account");
        } else {
            isUpdate = true;
            text = "✅ " + langTextService.getTxt(user, "G'alaba \nYutuq: ", "Победа \nвыигрыш: ", "Victory \nWinning: ");
            if (Objects.equals(narda.getType(), NardaType.OVER)) {
                if (nardaState.getScore() > 7) {
                    BigDecimal win = narda.getAmount().multiply(Constant.NARDA_KF_OVER);
                    user.setBalance(user.getBalance().add(win));
                    text = text + win;
                } else
                    text = maglubiyat;
            } else if (Objects.equals(narda.getType(), NardaType.MIDDLE)) {
                if (nardaState.getScore() == 7) {
                    BigDecimal win = narda.getAmount().multiply(Constant.NARDA_KF_MIDDLE);
                    user.setBalance(user.getBalance().add(win));
                    text = text + win;
                } else
                    text = maglubiyat;
            } else if (Objects.equals(narda.getType(), NardaType.LOWER)) {
                if (nardaState.getScore() < 7) {
                    BigDecimal win = narda.getAmount().multiply(Constant.NARDA_KF_LOWER);
                    user.setBalance(user.getBalance().add(win));
                    text = text + win;
                } else
                    text = maglubiyat;
            }
            user.setBalance(user.getBalance().subtract(narda.getAmount()));
        }
        user.setGamed(true);

        userRepo.save(user);
        AnswerCallbackQuery answerCallbackQuery = AnswerCallbackQuery.builder()
                .text(isUpdate ? nardaState.getView() + "\n" + text + "\nscore: " + nardaState.getScore() : text)
                .showAlert(true)
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();

        try {
            execute.answerCallbackQuery(answerCallbackQuery);
            //SONGI HABARNI YANGILASH UCHUN
            editMessage(update, user);
        } catch (Exception ignore) {
        }


    }


    public NardaState generateGame(NardaType type) {
        String three = "\uD83D\uDFE2\uD83D\uDD18\uD83D\uDD18\n" +
                "\uD83D\uDD18\uD83D\uDFE2\uD83D\uDD18\n" +
                "\uD83D\uDD18\uD83D\uDD18\uD83D\uDFE2";
        String four = "\uD83D\uDD18\uD83D\uDFE2\uD83D\uDD18\n" +
                "\uD83D\uDFE2\uD83D\uDD18\uD83D\uDFE2\n" +
                "\uD83D\uDD18\uD83D\uDFE2\uD83D\uDD18";
        String fife = "\uD83D\uDFE2\uD83D\uDD18\uD83D\uDFE2\n" +
                "\uD83D\uDD18\uD83D\uDFE2\uD83D\uDD18\n" +
                "\uD83D\uDFE2\uD83D\uDD18\uD83D\uDFE2";
        String six = "\uD83D\uDFE2\uD83D\uDD18\uD83D\uDFE2\n" +
                "\uD83D\uDFE2\uD83D\uDD18\uD83D\uDFE2\n" +
                "\uD83D\uDFE2\uD83D\uDD18\uD83D\uDFE2";
        String two = "\uD83D\uDFE2\uD83D\uDD18\uD83D\uDD18\n" +
                "\uD83D\uDD18\uD83D\uDD18\uD83D\uDD18\n" +
                "\uD83D\uDD18\uD83D\uDD18\uD83D\uDFE2";
        String one = "\uD83D\uDD18\uD83D\uDD18\uD83D\uDD18\n" +
                "\uD83D\uDD18\uD83D\uDFE2\uD83D\uDD18\n" +
                "\uD83D\uDD18\uD83D\uDD18\uD83D\uDD18";

        String[] arr = {one, two, three, four, fife, six};
        int[] over = {1, 2, 3, 4, 5, 6, 1};
        int[] lower = {1, 2, 3, 4, 5, 6, 4};
        int[] middle = {1, 2, 3, 4, 5, 6, 4, 5, 6, 1};
        NardaState gameStates = new NardaState();

        int first = 0, second = 0;
        if (Objects.equals(type, NardaType.MIDDLE)) {
            //IS MIDDLE
            first = middle[new Random().nextInt(over.length)];
            second = middle[new Random().nextInt(over.length)];
        } else if (Objects.equals(type, NardaType.OVER)) {
            // IS OVER
            first = over[new Random().nextInt(over.length)];
            second = over[new Random().nextInt(over.length)];
        } else {
            //IS LOWER
            first = lower[new Random().nextInt(over.length)];
            second = lower[new Random().nextInt(over.length)];
        }
        gameStates.setView(arr[first - 1] + "\n\n" + arr[second - 1]);
        gameStates.setScore(first + second);
        return gameStates;

    }

}
