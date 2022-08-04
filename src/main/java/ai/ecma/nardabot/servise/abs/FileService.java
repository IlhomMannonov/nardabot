package ai.ecma.nardabot.servise.abs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public interface FileService {
    void getFile(HttpServletRequest request, HttpServletResponse response, UUID id);
}
