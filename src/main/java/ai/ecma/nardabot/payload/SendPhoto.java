package ai.ecma.nardabot.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SendPhoto {
    @NotNull
    @JsonProperty("chat_id")
    private String chatId;

    private String caption;
    @JsonProperty("reply_markup")

    private ReplyKeyboard replyMarkup;

    private String photo;
}
