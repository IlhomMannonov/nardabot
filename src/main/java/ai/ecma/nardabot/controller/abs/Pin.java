package ai.ecma.nardabot.controller.abs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/pin")
public interface Pin {
    @GetMapping
    String getPin();
}
