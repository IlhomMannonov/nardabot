package ai.ecma.nardabot.controller.impl;

import ai.ecma.nardabot.controller.abs.FileController;
import ai.ecma.nardabot.servise.abs.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileControllerImpl implements FileController {

    private final FileService fileService;

    @Override
    public void getFile(HttpServletRequest request, HttpServletResponse response, UUID id) {
        fileService.getFile(request, response, id);
    }
}
