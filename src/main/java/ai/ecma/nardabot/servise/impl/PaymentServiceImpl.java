package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.Card;
import ai.ecma.nardabot.entity.PayHistory;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.PayStatus;
import ai.ecma.nardabot.enums.PayTypes;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.CardRepo;
import ai.ecma.nardabot.repository.PayHistoryRepo;
import ai.ecma.nardabot.repository.PayTypeRepo;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PayHistoryRepo payHistoryRepo;
    private final BaseService baseService;
    private final UserRepo userRepo;
    private final LangTextService langTextService;
    private final ButtonService buttonService;
    private final Execute execute;
    private final PayTypeRepo payTypeRepo;
    private final CardRepo cardRepo;

    private final ChannelService channelService;

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


    @Override
    public void withdraw(Update update, User user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId());
        if (!cardRepo.existsByUserId(user.getId())) {
            baseService.setState(user, State.HOME);
            sendMessage.setText(langTextService.getTxt(user, "Sizda Karta mavjud emas, kartangizni sozlamalar bo'limidan qo'shasiz", "You do not have a Card, add your card from the settings section", "У вас нет Карты, добавьте свою карту из раздела настроек"));
            sendMessage.setReplyMarkup(buttonService.getBtn(user));
            execute.sendMessage(sendMessage);
            return;
        }
        baseService.setState(user, State.WITHDRAW);
        sendMessage = SendMessage.builder()
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
                sendMessage.setText(langTextService.getTxt(user, "Siz <b>" + optionalPayHistory.get().getOrderCode() + "</b> raqamli tolovni amalga oshirmagansiz!\nTolovni amalga oshiring yoki bekor qiling", "You have not made a digital payment <b>" + optionalPayHistory.get().getOrderCode() + "</b>!\nMake or cancel a payment", "Вы не совершали цифровой платеж <b>" + optionalPayHistory.get().getOrderCode() + "</b>!\nСовершить или отменить платеж"));
                execute.sendMessage(sendMessage);
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
                sendMessage.setText(langTextService.getTxt(user, "Kiritilgan pul belgilangan miqdoga to'g'ri kelmadi\nMin : " + Constant.MIN_DEPOSIT, "The amount entered did not correspond to the specified amount\n Min BET: " + Constant.MIN_BET, "Введенная сумма не соответствует указанной сумме\n Min BET: " + Constant.MIN_BET));
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

            payHistoryRepo.save(payHistory);

            baseService.setState(user, State.HOME);
            sendMessage.setReplyMarkup(buttonService.getBtn(user));
            sendMessage.setText(langTextService.getTxt(user,
                    "To'lovnoma muvoffaqiyatli yaratildi! \nPayment Code = " + payHistory.getOrderCode() + "\nPulni hisobingizga tushirish uchun " + Constant.USERNAME + " ga payment codeni yuboring va tolovni amalga oshiring",
                    "Payment order created successfully! \nPayment Code = " + payHistory.getOrderCode() + "\nSend payment code to " + Constant.USERNAME + " and make payment",
                    "Платежное поручение успешно создано! \nКод платежа = " + payHistory.getOrderCode() + "\nОтправьте код платежа на " + Constant.USERNAME + " и произведите платеж"));
            execute.sendMessage(sendMessage);

            //KANAL UCHUN HABARLAR
            channelService.sendPayOrder(payHistory, user);

        }

    }

    @Override
    public void enterWithdrawSum(Update update) {
        User user = CommonUtils.getUser();
        if (update.getMessage().hasText()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(user.getChatId());
            sendMessage.enableHtml(true);

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
            } else if (minWithdraw > 0) {
                NumberFormat numberFormat = NumberFormat.getNumberInstance();
                sendMessage.setText(langTextService.getTxt(user, "Botdan eng kam pul chiqarish: " + numberFormat.format(Constant.MIN_WITHDRAW) + " so'm", "The minimum withdrawal from the bot: " + numberFormat.format(Constant.MIN_WITHDRAW) + " sum", "Минимальный вывод с бота: " + numberFormat.format(Constant.MIN_WITHDRAW) + " sum"));
                execute.sendMessage(sendMessage);
                return;
            } else if (!user.getGamed()) {
                baseService.setState(user, State.HOME);
                sendMessage.setText(langTextService.getTxt(user, "❗️Chiqarib olinayotgan pull tikilgan pullar miqdoriga to'g'ri kelmayapdi", "❗️The withdrawn money does not correspond to the amount of bet money", "❗️Выведенные деньги не соответствуют сумме ставок"));
                sendMessage.setReplyMarkup(buttonService.getBtn(user));
                execute.sendMessage(sendMessage);
                return;
            }
            user.setBalance(user.getBalance().subtract(amount));
            baseService.setState(user, State.HOME);
            Card card = cardRepo.getByUserId(user.getId());
            PayHistory payHistory = PayHistory.builder()
                    .action(PayStatus.OUT)
                    .amount(amount)
                    .userBalance(user.getBalance())
                    .card(card)
                    .payedCard(card.getNumber())
                    .orderCode(String.valueOf(payHistoryRepo.getCode() + 1))
                    .payType(payTypeRepo.getByType(PayTypes.CUSTOMER))
                    .active(true)
                    .status(PayStatus.PENDING)
                    .user(user)
                    .build();
            payHistoryRepo.save(payHistory);
            sendMessage.setText(langTextService.getTxt(user,
                    "To'lovnoma muvoffaqiyatli yaratildi! \nPayment Code = " + payHistory.getOrderCode() + "\nTo'lov tasdiqlanishi uchun 5 minutdan 40 minutgacha vaqt ketishi mumkin\n",
                    "Payment order created successfully! \nPayment Code = " + payHistory.getOrderCode() + "\nIt may take 5 to 40 minutes for the payment to be approved\n",
                    "Платежное поручение успешно создано! \nКод платежа = " + payHistory.getOrderCode() + "\nПодтверждение платежа может занять от 5 до 40 минут.\n"));
            sendMessage.setReplyMarkup(buttonService.getBtn(user));
            execute.sendMessage(sendMessage);


        }
    }

    private String getHash(String amount, String orderCode) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String text = Constant.MERCHANT_ID + ":" + amount + ":" + Constant.KASSA_KEY_2 + ":" + "RUB:" + orderCode;
        md5.update(text.getBytes());
        byte[] digest = md5.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toLowerCase();

        return "https://" +
                "pay.freekassa.ru/?m=" +
                Constant.MERCHANT_ID +
                "&oa=" + amount +
                "&o=" + orderCode +
                "&s=" + hash +
                "&currency=" + "RUB";
    }


}
