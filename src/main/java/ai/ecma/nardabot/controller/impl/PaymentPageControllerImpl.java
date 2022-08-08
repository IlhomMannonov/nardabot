package ai.ecma.nardabot.controller.impl;

import ai.ecma.nardabot.controller.abs.PaymentPageController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
@RequiredArgsConstructor
@EnableWebMvc
public class PaymentPageControllerImpl  implements PaymentPageController {
    @Override
    public String successPayment() {
        return "success";
    }

    @Override
    public String rejectPayment() {
        return "reject";
    }

    @Override
    public String warning() {
        return null;
    }
}
