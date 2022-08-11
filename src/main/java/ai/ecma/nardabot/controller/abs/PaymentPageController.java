package ai.ecma.nardabot.controller.abs;

import org.checkerframework.common.value.qual.StringVal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RequestMapping("/api/v1" + PaymentPageController.PAYMENT)
public interface PaymentPageController {
    String PAYMENT = "/payment";
    String SUCCESS = "/success";
    String REJECT = "/reject";
    String WARNING = "/warning";
    String APPROVE_PAGE = "/approve-page";
    String ACCEPT_PAYMENT = "/accept-payment";
    String REJECT_PAYMENT = "/reject-payment";
    String PAYED_PAGE = "/payed-page";

    @GetMapping(SUCCESS)
    String successPayment();

    @GetMapping(REJECT)
    String rejectPayment();

    @GetMapping(WARNING)
    String warning();

    @GetMapping(APPROVE_PAGE)
    String getPaymentPage(Model model);

    @GetMapping(ACCEPT_PAYMENT)
    String acceptPayment(Model model, @RequestParam("id") UUID id);

    @GetMapping(REJECT_PAYMENT)
    String rejectPayment(Model model, @RequestParam("id") UUID id);

    @GetMapping(PAYED_PAGE)
    String payedPage(Model model);
}
