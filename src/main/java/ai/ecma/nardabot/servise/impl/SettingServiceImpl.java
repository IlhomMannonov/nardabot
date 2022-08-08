package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.Card;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.repository.CardRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingsService {
    private final BaseService baseService;
    private final ButtonService buttonService;
    private final Execute execute;
    private final CardRepo cardRepo;
    private final LangTextService langTextService;

    @Override
    public void myCard(Update update) {
        User user = CommonUtils.getUser();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId());
        sendMessage.enableHtml(true);
        if (!cardRepo.existsByUserId(user.getId())) {
            sendMessage.setText(langTextService.getTxt(user, "Sizda karta mavjud emas", "You do not have a card", "У вас нет карты"));
        } else {
            Card card = cardRepo.getByUserId(user.getId());
            sendMessage.setText(langTextService.getTxt(user,
                    "Sizning kartangiz: <b>" + card.getNumber() + "</b>",
                    "Your card is: <b>" + card.getNumber() + "</b>",
                    "Ваша карта: <b>" + card.getNumber() + "</b>"));
        }
        execute.sendMessage(sendMessage);

    }

    @Override
    public void addCard(Update update) {
        User user = CommonUtils.getUser();
        SendMessage build = SendMessage.builder()
                .chatId(user.getChatId())
                .text(langTextService.getTxt(user,
                        "Tolovlarni kartanigizga tushirib olish uchun karta qoshishingiz kerak kartnigizni 16 talik raqalarini kiriting",
                        "You need to add a card to download payments to your card. Enter your 16-digit card number.",
                        "Вам нужно добавить карту, чтобы загружать платежи на вашу карту. Введите 16-значный номер карты.")).build();
        execute.sendMessage(build);
    }

    @Override
    public void setting(Update update) {
        if (update.hasMessage()) {
            User user = CommonUtils.getUser();
            String text = update.getMessage().getText();
            String see = langTextService.getTxt(user, "Mening Kartam", "My Card", "Моя карточка");
            String add = langTextService.getTxt(user, "Karta qo'shish", "Add Card", "Добавить карту");
            if (Objects.equals(text, see)) {
                myCard(update);
            } else if (Objects.equals(text, add)) {
                addCard(update);
            }
        }
    }
}
