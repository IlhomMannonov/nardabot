package ai.ecma.nardabot.servise.abs;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public interface Btn {

    InlineKeyboardMarkup markup(List<InlineKeyboardButton> ... rows);

    InlineKeyboardButton button(String text, String callbackData);

    List<InlineKeyboardButton> row(InlineKeyboardButton... inlineKeyboardButtons);

    ReplyKeyboardMarkup markupReplay(List<KeyboardRow> rows);

    List<KeyboardRow> rowList(KeyboardRow... keyboardRows);

    KeyboardRow row(KeyboardButton... buttons);

    KeyboardButton button(String text, boolean contact, boolean location);
}
