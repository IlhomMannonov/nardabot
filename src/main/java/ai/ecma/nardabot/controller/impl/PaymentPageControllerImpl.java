package ai.ecma.nardabot.controller.impl;

import ai.ecma.nardabot.controller.abs.PaymentPageController;
import ai.ecma.nardabot.feign.FreeKassaFeign;
import ai.ecma.nardabot.payload.freekassa.CreateOrderDTO;
import ai.ecma.nardabot.servise.abs.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@EnableWebMvc
public class PaymentPageControllerImpl implements PaymentPageController {
    private final AdminService adminService;
    private final FreeKassaFeign feign;

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
        return "warning";
    }

    @Override
    public String getPaymentPage(Model model) {
//        Object rub = feign.createPayment(new CreateOrderDTO(
//                "17756",
//                123,
//                1,
//                6,
//                "ilhom.mannonov.dev@gmail.com",
//                "192.168.1.104",
//                100.0,
//                "RUB"
//
//        ));
//        System.out.println(rub);
        return adminService.getPaymentPage(model);
    }

    @Override
    public String acceptPayment(Model model, UUID id) {
        return adminService.acceptPayment(model, id);
    }

    @Override
    public String rejectPayment(Model model, UUID id) {
        return adminService.rejectPayment(model, id);
    }

    @Override
    public String payedPage(Model model) {
        return adminService.payedPage(model);
    }

}
