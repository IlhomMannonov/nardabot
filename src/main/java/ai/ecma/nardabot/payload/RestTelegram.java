package ai.ecma.nardabot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RestTelegram implements Serializable {
    private boolean ok;
    private Message result;
}
