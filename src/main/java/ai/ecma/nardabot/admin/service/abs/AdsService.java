package ai.ecma.nardabot.admin.service.abs;

import ai.ecma.nardabot.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface AdsService {
    void adsHome(Update update, User user);

    void addAdsText(Update update, User user);

    void addMedia(Update update, User user);

    void addButton(Update update, User user);

    void adsRouter(Update update, User user);

    void checkSending(Update update, User user);
}
