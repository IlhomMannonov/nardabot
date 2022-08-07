package ai.ecma.nardabot.servise.abs;

import ai.ecma.nardabot.entity.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface ButtonService {
    InlineKeyboardMarkup getInlineBtn(User user, String id);

    ReplyKeyboardMarkup getBtn(User user);

}
