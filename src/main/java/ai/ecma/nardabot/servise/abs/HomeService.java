package ai.ecma.nardabot.servise.abs;

import ai.ecma.nardabot.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface HomeService {
    void home(Update update);

    void games(User user);

    void settings(Update update, User user);
}
