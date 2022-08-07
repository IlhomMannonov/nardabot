package ai.ecma.nardabot.servise.abs;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackQueryService {
    void start(Update update);

    void choiceLang(Update update);

    void deleteHistory(Update update);
}
