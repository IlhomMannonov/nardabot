package ai.ecma.nardabot.servise.abs;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface AuthService {
    void start(Update update);

    void phoneNumber(Update update);

    void getHome(Update update);
}
