package ai.ecma.nardabot.servise.abs;

import ai.ecma.nardabot.entity.User;

public interface LangTextService {
    String inlineText(User user);

    String buttonText(User user);

    String text(User user);

    String getTxt(User user, String uz, String en, String ru);
}
