package ai.ecma.nardabot.controller.abs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequestMapping(FileController.FILE)
public interface FileController {
    String FILE = "/file";
    String GET = "/{id}";

    @GetMapping(GET)
    void getFile(HttpServletRequest request, HttpServletResponse response, @PathVariable UUID id);
}
