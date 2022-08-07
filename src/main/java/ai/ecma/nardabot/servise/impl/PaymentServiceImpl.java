package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.PayHistory;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.PayStatus;
import ai.ecma.nardabot.enums.PayTypes;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.PayHistoryRepo;
import ai.ecma.nardabot.repository.PayTypeRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PayHistoryRepo payHistoryRepo;
    private final BaseService baseService;
    private final LangTextService langTextService;
    private final ButtonService buttonService;
    private final Execute execute;
    private final PayTypeRepo payTypeRepo;
    private final Timer timer;


    @Override
    public void deposit(Update update, User user) {
        baseService.setState(user, State.PAYMENT_ENTER_SUM);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(user.getChatId())
                .text(langTextService.getTxt(user,
                        "Kirgizmoqchi bo'lgan pull miqdorini yozing",
                        "Enter the amount you want to deposit",
                        "Введите сумму, которую вы хотите внести"))
                .replyMarkup(buttonService.getBtn(user))
                .build();
        execute.sendMessage(sendMessage);
    }

    private void send(User user) {
    }

    @Override

    public void withdraw(Update update, User user) {

        baseService.setState(user, State.WITHDRAW);

        SendMessage sendMessage = SendMessage.builder()
                .text(langTextService.getTxt(user, "Chiqarmoqchi bo'lgan pul miqdorini yozing", "Enter the amount you want to withdraw", "Введите сумму, которую хотите вывести"))
                .chatId(user.getChatId())
                .replyMarkup(buttonService.getBtn(user))
                .build();
        execute.sendMessage(sendMessage);
    }

    @Override
    public void enterSum(Update update) {
        User user = CommonUtils.getUser();

        if (update.getMessage().hasText()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableHtml(true);
            sendMessage.setChatId(user.getChatId());

            //AGAR ESKI TOLOV TO'LANMAGAN BO'LSA
            Optional<PayHistory> optionalPayHistory = payHistoryRepo.findFirstByUserIdAndActionOrderByCreatedAtDesc(user.getId(), PayStatus.IN);
            if (optionalPayHistory.isPresent() && optionalPayHistory.get().getStatus().name().equals(PayStatus.PENDING.name())) {
                baseService.setState(user, State.HOME);
                sendMessage.setReplyMarkup(buttonService.getBtn(user));
                sendMessage.setText(langTextService.getTxt(user, "Siz <b>" + optionalPayHistory.get().getOrderCode() + "</b> raqamli tolovni amalga oshirmagansiz!\nTo'lovni 10 daqiqa ichida amalga oshiring yoki bu tolov bekor qilinadi", "You have not made a digital payment <b>" + optionalPayHistory.get().getOrderCode() + "</b>!\nMake a payment within 10 minutes or this payment will be canceled", "Вы не совершали цифровой платеж <b>" + optionalPayHistory.get().getOrderCode() + "</b>!\nПроведите платеж в течение 10 минут, иначе этот платеж будет отменен"));
                execute.sendMessage(sendMessage);
                //AKS HOLDA

                return;
            }

            String text = update.getMessage().getText();
            BigDecimal sum = null;
            try {
                if (text.length() >= 11)
                    throw new Exception();
                sum = new BigDecimal(text);
            } catch (Exception ignore) {
                sendMessage.setText(langTextService.getTxt(user, "Iltimos to'g'ri pul miqdorini kiriting", "Please enter the correct amount", "Пожалуйста, введите правильную сумму"));
                execute.sendMessage(sendMessage);
                return;
            }
            if (Constant.MIN_BET.compareTo(sum) > 0) {
                sendMessage.setText(langTextService.getTxt(user, "Kiritilgan pul belgilangan miqdoga to'g'ri kelmadi\nMin BET: " + Constant.MIN_BET, "The amount entered did not correspond to the specified amount\n Min BET: " + Constant.MIN_BET, "Введенная сумма не соответствует указанной сумме\n Min BET: " + Constant.MIN_BET));
                execute.sendMessage(sendMessage);
                return;
            }

            Long code = payHistoryRepo.getCode();
            PayHistory payHistory = PayHistory.builder()
                    .active(true)
                    //HOZIRCHA
                    .payType(payTypeRepo.getByType(PayTypes.CUSTOMER))
                    //BU PUL SOLMOQDA
                    .action(PayStatus.IN)
                    .amount(sum)
                    .orderCode(code != null ? String.valueOf(code + 1) : "1000")
                    .status(PayStatus.PENDING)
                    .user(user)
                    .build();
            schedule(setRejectForMinute(payHistory));

            payHistoryRepo.save(payHistory);

            sendMessage.setText(langTextService.getTxt(user,
                    "To'lovnoma muvoffaqiyatli yaratildi! \nPayment Code = " + payHistory.getOrderCode() + "\nPulni hisobingizga tushirish uchun " + Constant.USERNAME + " ga payment codeni yuboring va tolovni amalga oshiring",
                    "Payment order created successfully! \nPayment Code = " + payHistory.getOrderCode() + "\nSend payment code to " + Constant.USERNAME + " and make payment",
                    "Платежное поручение успешно создано! \nКод платежа = " + payHistory.getOrderCode() + "\nОтправьте код платежа на " + Constant.USERNAME + " и произведите платеж"));
            execute.sendMessage(sendMessage);

        }

    }

    @Override
    public void enterWithdrawSum(Update update) {
        User user = CommonUtils.getUser();
        if (update.getMessage().hasText()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableHtml(true);
            sendMessage.setChatId(user.getChatId());
//            Optional<PayHistory> optionalPayHistory = payHistoryRepo.findFirstByUserIdAndActionOrderByCreatedAtDesc(user.getId(), PayStatus.OUT);
//            if (optionalPayHistory.isPresent()) {
//                sendMessage.setText(langTextService.getTxt(user, "Siz <b>" + optionalPayHistory.get().getOrderCode() + "</b> raqamli tolovni amalga oshirmagansiz!\nTo'lovni 10 daqiqa ichida amalga oshiring yoki bu tolov bekor qilinadi", "You have not made a digital payment <b>" + optionalPayHistory.get().getOrderCode() + "</b>!\nMake a payment within 10 minutes or this payment will be canceled", "Вы не совершали цифровой платеж <b>" + optionalPayHistory.get().getOrderCode() + "</b>!\nПроведите платеж в течение 10 минут, иначе этот платеж будет отменен"));
//                execute.sendMessage(sendMessage);
//                baseService.setState(user, State.HOME);
//                return;
//            }
            String text = update.getMessage().getText();
            BigDecimal amount = null;
            try {
                if (text.length() >= 11)
                    throw new Exception();
                amount = new BigDecimal(text);
            } catch (Exception ignore) {
                sendMessage.setText(langTextService.getTxt(user, "Iltimos to'g'ri pul miqdorini kiriting", "Please enter the correct amount", "Пожалуйста, введите правильную сумму"));
                execute.sendMessage(sendMessage);
                return;
            }
            int to = amount.compareTo(user.getBalance());
            int minWithdraw = Constant.MIN_WITHDRAW.compareTo(amount);
            if (to > 0) {
                baseService.setState(user, State.HOME);
                sendMessage.setText(langTextService.getTxt(user, "Hisobingizda chiqarish mablag yetarli emas", "You don't have enough funds to withdraw from your account", "У вас недостаточно средств для вывода средств со счета"));
              sendMessage.setReplyMarkup(buttonService.getBtn(user));
                execute.sendMessage(sendMessage);
                return;
            }
            if (minWithdraw > 0) {
                NumberFormat numberFormat = NumberFormat.getNumberInstance();
                sendMessage.setText(langTextService.getTxt(user, "Botdan eng kam pul chiqarish: " + numberFormat.format(Constant.MIN_WITHDRAW) + " so'm", "The minimum withdrawal from the bot: " + numberFormat.format(Constant.MIN_WITHDRAW) + " sum", "Минимальный вывод с бота: " + numberFormat.format(Constant.MIN_WITHDRAW) + " sum"));
                execute.sendMessage(sendMessage);
                return;
            }

            PayHistory payHistory = PayHistory.builder()
                    .action(PayStatus.OUT)
                    .amount(amount)
                    .orderCode(String.valueOf(payHistoryRepo.getCode() + 1))
                    .payType(payTypeRepo.getByType(PayTypes.CUSTOMER))
                    .active(true)
                    .status(PayStatus.PENDING)
                    .user(user)
                    .build();
            payHistoryRepo.save(payHistory);
            user.setBalance(user.getBalance().subtract(amount));
            sendMessage.setText(langTextService.getTxt(user,
                    "To'lovnoma muvoffaqiyatli yaratildi! \nPayment Code = " + payHistory.getOrderCode() + "\nPulni Chiqarib olish uchun " + Constant.USERNAME + " ga payment codeni yuboring",
                    "Payment order created successfully! \nPayment Code = " + payHistory.getOrderCode() + "\nSend payment code to " + Constant.USERNAME + " to Withdraw money",
                    "Платежное поручение успешно создано! \nКод платежа = " + payHistory.getOrderCode() + "\nОтправьте код платежа на " + Constant.USERNAME + ", чтобы снять деньги"));
            execute.sendMessage(sendMessage);
            baseService.setState(user, State.HOME);


        }
    }

    private void schedule(TimerTask task) {
        Date current = new Date();
        timer.schedule(task, new Date(current.getTime() + Constant.REJECT_TIME));
    }

    private TimerTask setRejectForMinute(PayHistory payHistory) {
        return new TimerTask() {
            @Override
            public void run() {
                payHistory.setStatus(PayStatus.REJECT);
                payHistoryRepo.save(payHistory);
            }
        };
    }
}
