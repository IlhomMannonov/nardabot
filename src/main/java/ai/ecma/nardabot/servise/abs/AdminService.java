package ai.ecma.nardabot.servise.abs;

import org.springframework.ui.Model;

import java.util.UUID;

public interface AdminService {
    String getPaymentPage(Model model);

    String acceptPayment(Model model, UUID chatId);

    String rejectPayment(Model model, UUID id);

    String payedPage(Model model);
}
