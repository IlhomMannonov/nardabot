package ai.ecma.nardabot.servise.abs;

import ai.ecma.nardabot.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackQueryService {
    void start(Update update);

    void choiceLang(Update update);

    void editLang(Update update, User user);
    void deleteHistory(Update update);
}
