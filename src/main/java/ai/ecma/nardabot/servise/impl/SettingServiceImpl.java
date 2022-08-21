package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.Card;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.CardRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingsService {
    private final BaseService baseService;
    private final ButtonService buttonService;
    private final Execute execute;
    private final CardRepo cardRepo;
    private final LangTextService langTextService;
    private final Btn btn;

    @Override
    public void myCard(Update update) {
        User user = CommonUtils.getUser();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId());
        sendMessage.enableHtml(true);
        if (!cardRepo.existsByUserId(user.getId())) {
            sendMessage.setText(langTextService.getTxt(user, "Sizda karta mavjud emas", "You do not have a card", "У вас нет карты"));
            sendMessage.setReplyMarkup(buttonService.getInlineBtn(user, "add"));
        } else {
            Card card = cardRepo.getByUserId(user.getId());
            sendMessage.setText(langTextService.getTxt(user,
                    "Sizning kartangiz: <b>" + card.getNumber() + "</b>",
                    "Your card is: <b>" + card.getNumber() + "</b>",
                    "Ваша карта: <b>" + card.getNumber() + "</b>"));
            sendMessage.setReplyMarkup(buttonService.getInlineBtn(user, "edit"));
        }
        execute.sendMessage(sendMessage);

    }

    @Override
    public void addCard(Update update) {
        User user = CommonUtils.getUser();
        if (update.hasMessage()) {

            SendMessage sendMessage = new SendMessage();
            boolean b = validCard(update, user, true);
            String text = update.getMessage().getText();
            if (b) {
                Card card = Card.builder()
                        .number(text)
                        .user(user)
                        .build();
                cardRepo.save(card);
                baseService.setState(user, State.SETTINGS);
                sendMessage = SendMessage.builder()
                        .text(langTextService.getTxt(user, "Karta muvoffaqiyatli qo'shildi", "Card added successfully", "Карта успешно добавлена"))
                        .chatId(user.getChatId())
                        .replyMarkup(buttonService.getBtn(user))
                        .build();
                execute.sendMessage(sendMessage);
            }


        } else {

            execute.deleteMessage(update.getCallbackQuery().getMessage().getMessageId(), user.getChatId());
            baseService.setState(user, State.ADD_CARD);
            SendMessage build = SendMessage.builder()
                    .replyMarkup(buttonService.getBtn(user))
                    .chatId(user.getChatId())
                    .text(langTextService.text(user)).build();
            execute.sendMessage(build);
        }
    }

    private boolean validCard(Update update, User user, boolean isEdit) {
        SendMessage sendMessage = new SendMessage();

        String text = update.getMessage().getText();
        text = text.replace(" ", "");
        String regex = "^(8600|9860)[0-9]{12}";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(text).matches()) {
            sendMessage = SendMessage.builder()
                    .text(langTextService.getTxt(user, "Iltimos to'g'ri karta raqamni kiriting!", "Please enter the correct card number!", "Пожалуйста, введите правильный номер карты!"))
                    .chatId(user.getChatId())
                    .build();
            execute.sendMessage(sendMessage);
            return false;
        } else {
            if (cardRepo.existsByUserId(user.getId()) && isEdit) {
                sendMessage = SendMessage.builder()
                        .text(langTextService.getTxt(user, "Sizda Karta mavjud", "You have a Card", "У вас есть карта"))
                        .chatId(user.getChatId())
                        .build();
                execute.sendMessage(sendMessage);
                return false;

            }
            if (cardRepo.existsByNumber(text)) {
                sendMessage = SendMessage.builder()
                        .text(langTextService.getTxt(user, "Bu karta raqam boshqa user tomonidan qo'shilgan", "This card number was added by another user", "Этот номер карты был добавлен другим пользователем"))
                        .chatId(user.getChatId())
                        .build();
                execute.sendMessage(sendMessage);
                return false;

            }
        }
        return true;
    }

    @Override
    public void setting(Update update) {
        if (update.hasMessage()) {
            User user = CommonUtils.getUser();
            String text = update.getMessage().getText();
            String see = langTextService.getTxt(user, "Mening Kartam", "My Card", "Моя карточка");
            String lang = langTextService.getTxt(user, "Tilni o'zgartirish", "Change language", "Изменение языка");
            if (Objects.equals(text, see)) {
                myCard(update);
            } else if (Objects.equals(lang, text)) {
                lang(update);
            }
        }
    }

    @Override
    public void lang(Update update) {
        User user = CommonUtils.getUser();
        baseService.setState(user, State.EDIT_LANG);
        execute.sendMessage(SendMessage.builder()
                .replyMarkup(buttonService.getInlineBtn(user, ""))
                .text("Please choice your language")
                .chatId(user.getChatId())
                .build());
    }

    @Override
    public void editCard(Update update) {
        User user = CommonUtils.getUser();
        baseService.setState(user, State.EDIT_CARD);
        if (update.hasCallbackQuery()) {
            execute.deleteMessage(update.getCallbackQuery().getMessage().getMessageId(), user.getChatId());
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(user.getChatId())
                    .text(langTextService.text(user))
                    .replyMarkup(buttonService.getBtn(user))
                    .build();
            execute.sendMessage(sendMessage);
        } else {
            boolean b = validCard(update, user, false);
            if (b) {
                baseService.setState(user, State.SETTINGS);
                SendMessage build = SendMessage.builder()
                        .text(langTextService.getTxt(user, "Karta muvoffaqiyatli o'zgartirildi", "Card changed successfully", "Карта успешно изменена"))
                        .replyMarkup(buttonService.getBtn(user))
                        .chatId(user.getChatId())
                        .build();

                Card card = cardRepo.getByUserId(user.getId());
                card.setNumber(update.getMessage().getText());
                cardRepo.save(card);
                execute.sendMessage(build);
            }
        }
    }
}
