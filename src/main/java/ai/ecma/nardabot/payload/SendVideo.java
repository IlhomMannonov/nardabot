package ai.ecma.nardabot.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendVideo {
    @NotNull
    @JsonProperty("chat_id")
    private String chatId;

    private InputFile video;

    private ReplyKeyboard replyMarkup;
}
