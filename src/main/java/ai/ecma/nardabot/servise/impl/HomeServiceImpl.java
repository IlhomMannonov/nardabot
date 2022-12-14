package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.PayHistory;
import ai.ecma.nardabot.entity.PayType;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.PayStatus;
import ai.ecma.nardabot.enums.PayTypes;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.PayHistoryRepo;
import ai.ecma.nardabot.repository.ReferralRepo;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final UserRepo userRepo;
    private final CallbackQueryService callbackQueryService;
    private final LangTextService langTextService;
    private final PaymentService paymentService;
    private final BaseService baseService;
    private final ButtonService buttonService;
    private final Execute execute;
    private final PayHistoryRepo payHistoryRepo;
    private final ReferralRepo referralRepo;
    public final long HOUR = 3600 * 5000;

    @Override
    public void home(Update update) {
        User user = CommonUtils.getUser();
        String text = update.getMessage().getText();
        String games = langTextService.getTxt(user, "O'yinlar", "Games", "Игры");
        String profile = langTextService.getTxt(user, "Profil", "Profile", "Профиль");
        String payHistory = langTextService.getTxt(user, "To'lovlar tarixi", "Payment History", "История платежей");
        String deposit = langTextService.getTxt(user, "Pul Solish", "Deposit", "Депозит");
        String withdraw = langTextService.getTxt(user, "Pul chiqarish", "Withdraw", "вывод");
        String settings = langTextService.getTxt(user, "Sozlamalar", "Settings", "Настройки");

        if (Objects.equals(text, games))
            games(user);
        else if (Objects.equals(text, profile))
            profile(update, user);
        else if (Objects.equals(text, payHistory))
            payHistory(update, user);
        else if (Objects.equals(text, deposit))
            deposit(update, user);
        else if (Objects.equals(text, withdraw))
            withdraw(update, user);
        else if (Objects.equals(text, settings))
            settings(update, user);
    }

    @Override
    public void settings(Update update, User user) {
        baseService.setState(user, State.SETTINGS);
        SendMessage sendMessage = SendMessage.builder()
                .text(langTextService.getTxt(user, "Sozlamalar bo'limi", "Settings panel", "Настройки"))
                .chatId(user.getChatId())
                .replyMarkup(buttonService.getBtn(user))
                .build();
        execute.sendMessage(sendMessage);
    }

    public void games(User user) {
        baseService.setState(user, State.GAMES);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getChatId())
                .replyMarkup(buttonService.getBtn(user))
                .text(langTextService.text(user))
                .build();
        execute.sendMessage(sendMessage);
    }


    private void profile(Update update, User user) {

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        Integer rf = referralRepo.countByFromUser(user);
        SendMessage sendMessage = SendMessage.builder()
                .text(langTextService.getTxt(user,
                        "Ism: " + user.getName() + "\nTelefon: " + user.getPhone() + "\nHisob: <b>" + numberFormat.format(user.getBalance()) + " So'm</b>",
                        "Name: " + user.getName() + "\nPhone: " + user.getPhone() + "\nBalance: <b>" + numberFormat.format(user.getBalance()) + " So'm</b>" ,
                        "Имя:" + user.getName() + "\nТелефон:" + user.getPhone() + "\nБаланс: <b>" + numberFormat.format(user.getBalance()) + " So'm </b>" ))
                .chatId(user.getChatId())
                .build();
        sendMessage.enableHtml(true);
        execute.sendMessage(sendMessage);
    }

    private void payHistory(Update update, User user) {
        Boolean exists = payHistoryRepo.existsByUserId(user.getId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setChatId(user.getChatId());
        if (!exists) {
            sendMessage.setText(langTextService.getTxt(user, "Sizda tarix mavjud emas", "You have no history", "У тебя нет истории"));
        } else {
            List<PayHistory> payHistories = payHistoryRepo.findAllByUserId(user.getId());
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            baseService.setState(user, State.HISTORY);
            for (PayHistory payHistory : payHistories) {
                String sb = langTextService.getTxt(user,
                        (payHistory.getAction().equals(PayStatus.IN) ? "⤵️ Pul Soluvchi: " : "⤴️ Pul chiqaruvchi: ") + "<b>" + user.getName() + "</b>" + "\n\uD83D\uDCB3 To'lov turi: " + payHistory.getPayType().getType().name() + "\n\uD83D\uDCB5 To'lov summasi: " + "<b>" + numberFormat.format(payHistory.getAmount()) + " So'm</b>" + "\n" + "Holat: " + getStatus(user, payHistory.getStatus()) + "\n\uD83D\uDD70 Vaqt: " + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new Date(payHistory.getCreatedAt().getTime() + HOUR)) + ((payHistory.getStatus().name().equals(PayStatus.PENDING.name()) && payHistory.getAction().name().equals(PayStatus.IN.name())) ? "\nTolovni yakunlash uchun " + Constant.USERNAME + " bilan bog'laning\n" : ""),
                        (payHistory.getAction().equals(PayStatus.IN) ? "⤵️ Money came: " : "⤴️ Money came out: ") + "<b>" + user.getName() + "</b>" + "\n\uD83D\uDCB3 Payment Type: " + payHistory.getPayType().getType().name() + "\n\uD83D\uDCB5 Payment Amount: " + "<b>" + numberFormat.format(payHistory.getAmount()) + " Sum</b>" + "\n" + "Status: " + getStatus(user, payHistory.getStatus()) + "\n\uD83D\uDD70 Time: " + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(payHistory.getCreatedAt()) + ((payHistory.getStatus().name().equals(PayStatus.PENDING.name()) && payHistory.getAction().name().equals(PayStatus.IN.name())) ? "\n Contact " + Constant.USERNAME + " to complete payment\n" : ""),
                        (payHistory.getAction().equals(PayStatus.IN) ? "⤵️ Пришли деньги: " : "⤴️ Деньги вышли: ") + "<b>" + user.getName() + "</b>" + "\n\uD83D\uDCB3 Тип платежа: " + payHistory.getPayType().getType().name() + "\n\uD83D\uDCB5 Сумма платежа: " + "<b>" + numberFormat.format(payHistory.getAmount()) + " Сум</b>" + "\n" + "Статус: " + getStatus(user, payHistory.getStatus()) + "\n\uD83D\uDD70 Время: " + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(payHistory.getCreatedAt()) + ((payHistory.getStatus().name().equals(PayStatus.PENDING.name()) && payHistory.getAction().name().equals(PayStatus.IN.name())) ? "\nСвяжитесь с " + Constant.USERNAME + " для завершения платежа\n" : "")) + "\n\nOrder code: " + payHistory.getOrderCode();
                sendMessage.setText(sb);
                if (payHistory.getAction().equals(PayStatus.IN) && payHistory.getStatus().equals(PayStatus.PENDING)) {
                    sendMessage.setReplyMarkup(buttonService.getInlineBtn(user, "history:" + payHistory.getId()));
                } else {
                    sendMessage.setReplyMarkup(null);
                }
                execute.sendMessage(sendMessage);
            }
            baseService.setState(user, State.HOME);
//            sb.append("\n\n").append(langTextService.getTxt(user, "Bu yerda oxirgi 10 ta Tolovlar tarixi turadi!", "Here is the history of the last 10 payments!", "Здесь история последних 10 платежей!"));
        }

    }

    private String getStatus(User user, PayStatus status) {
        if (status.equals(PayStatus.PENDING)) {
            return langTextService.getTxt(user, "<b>⏳ Kutilmoqda</b>", "<b>⏳ Pending</b>", "<b>⏳ В ожидании</b>");
        } else if (status.equals(PayStatus.PAYED)) {
            return langTextService.getTxt(user, "<b>✅ To'langan</b>", "<b>✅ Payed</b>", "<b>✅ Оплачено</b>");
        } else if (status.equals(PayStatus.REJECT)) {
            return langTextService.getTxt(user, "<b>❌ Rad etildi</b>", "<b>❌ Reject</b>", "<b>❌ Было отказано</b>");
        }
        return null;
    }

    private void deposit(Update update, User user) {
        paymentService.deposit(update, user);
    }

    private void withdraw(Update update, User user) {
        paymentService.withdraw(update, user);
    }


}
