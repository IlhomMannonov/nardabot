package ai.ecma.nardabot.servise.abs;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface SettingsService {

    void myCard(Update update);

    void addCard(Update update);

    void setting(Update update);

    void editCard(Update update);
}
