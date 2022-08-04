package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.servise.abs.FileService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public void getFile(HttpServletRequest request, HttpServletResponse response, UUID id) {

    }
}
