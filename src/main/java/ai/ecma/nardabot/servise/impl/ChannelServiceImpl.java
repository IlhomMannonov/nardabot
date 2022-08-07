package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.PayHistory;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.PayStatus;
import ai.ecma.nardabot.repository.PayHistoryRepo;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final BaseService baseService;
    private final UserRepo userRepo;
    private final LangTextService langTextService;
    private final ButtonService buttonService;
    private final Btn btn;
    private final Execute execute;
    private final PayHistoryRepo payHistoryRepo;

    @Override
    public void sendPayOrder(PayHistory payHistory, User user) {
        StringBuilder text = new StringBuilder();
        text.append("Yangi order").append("\n")
                .append("Foydalanuvchi:  ").append(user.getName()).append("\n")
                .append("Telefon:  ").append(user.getPhone()).append("\n")
                .append("Balance:  ").append(user.getBalance()).append("\n")
                .append("Order code:  ").append(payHistory.getOrderCode()).append("\n")
                .append("Order price:  ").append(payHistory.getAmount()).append("\n");

        SendMessage sendMessage = SendMessage.builder()
                .chatId(Constant.CHANEL_ID)
                .text(text.toString())
                .replyMarkup(getChannelButton(payHistory.getId()))
                .build();

        execute.sendMessage(sendMessage);

    }

    @Override
    public void deleteOrder(Update update) {
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            String[] split = data.split(":");
            if (Objects.equals(split[0], "channel")) {
                UUID id = UUID.fromString(split[2]);
                Optional<PayHistory> optional = payHistoryRepo.findById(id);
                if (optional.isPresent()) {
                    PayHistory payHistory = optional.get();
                    switch (split[1]) {
                        case "payed":
                            payHistory.setStatus(PayStatus.PAYED);
                            User user = payHistory.getUser();
                            user.setBalance(user.getBalance().add(payHistory.getAmount()));
                            userRepo.save(user);
                            break;
                        case "reject":
                            payHistory.setStatus(PayStatus.REJECT);
                            break;
                    }
                    payHistoryRepo.save(payHistory);
                }
                execute.deleteMessage(update.getCallbackQuery().getMessage().getMessageId(), Constant.CHANEL_ID);


            }
        }
    }

    private ReplyKeyboard getChannelButton(UUID id) {
        return btn.markup(
                btn.row(
                        btn.button("Tolash", "channel:payed:" + id),
                        btn.button("Bekor qilish", "channel:reject:" + id)
                )
        );
    }
}
