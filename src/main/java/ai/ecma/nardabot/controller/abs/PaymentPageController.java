package ai.ecma.nardabot.controller.abs;

import org.checkerframework.common.value.qual.StringVal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1" + PaymentPageController.PAYMENT)
public interface PaymentPageController {
    String PAYMENT = "/payment";
    String SUCCESS = "/success";
    String REJECT = "/reject";
    String WARNING = "/warning";

    @GetMapping(SUCCESS)
    String successPayment();

    @GetMapping(REJECT)
    String rejectPayment();

    @GetMapping(WARNING)
    String warning();

}
