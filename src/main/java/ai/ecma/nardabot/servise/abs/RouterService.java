package ai.ecma.nardabot.servise.abs;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface RouterService {
    void getUpdate(Update update);

}
