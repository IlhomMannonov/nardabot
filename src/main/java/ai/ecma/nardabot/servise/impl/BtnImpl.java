package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.servise.abs.Btn;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BtnImpl implements Btn {

    @SafeVarargs
    @Override
    public final InlineKeyboardMarkup markup(List<InlineKeyboardButton>... rows) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>(Arrays.asList(rows));
        markup.setKeyboard(inlineButton);
        return markup;
    }

    @Override
    public InlineKeyboardButton button(String text, String callbackData) {
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(text);
        btn.setCallbackData(callbackData);
        return btn;
    }

    @Override
    public List<InlineKeyboardButton> row(InlineKeyboardButton... inlineKeyboardButtons) {
        return new ArrayList<>(Arrays.asList(inlineKeyboardButtons));
    }

    @Override
    public ReplyKeyboardMarkup markupReplay(List<KeyboardRow> rows) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setOneTimeKeyboard(true);
        markup.setResizeKeyboard(true);
        markup.setSelective(false);
        markup.setKeyboard(rows);
        markup.setOneTimeKeyboard(false);


        return markup;
    }

    @Override
    public List<KeyboardRow> rowList(KeyboardRow... keyboardRows) {
        return new ArrayList<>(Arrays.asList(keyboardRows));
    }

    @Override
    public KeyboardRow row(KeyboardButton... buttons) {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.addAll(Arrays.asList(buttons));
        return keyboardRow;
    }

    @Override
    public KeyboardButton button(String text, boolean contact, boolean location) {
        KeyboardButton button = new KeyboardButton();

        button.setText(text);
        button.setRequestContact(contact);

        button.setRequestLocation(location);
        return button;
    }
}
