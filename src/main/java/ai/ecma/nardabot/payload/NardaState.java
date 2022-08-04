package ai.ecma.nardabot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NardaState {
    private String view;
    private Integer score;
}
