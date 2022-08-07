package ai.ecma.nardabot.servise.abs;

import ai.ecma.nardabot.entity.PayHistory;
import ai.ecma.nardabot.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ChannelService {
    void sendPayOrder(PayHistory payHistory, User user);

    void deleteOrder(Update update);
}
