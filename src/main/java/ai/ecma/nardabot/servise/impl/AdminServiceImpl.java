package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.PayHistory;
import ai.ecma.nardabot.enums.PayStatus;
import ai.ecma.nardabot.payload.PayDTO;
import ai.ecma.nardabot.repository.PayHistoryRepo;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepo userRepo;
    private final PayHistoryRepo payHistoryRepo;

    @Override
    public String getPaymentPage(Model model) {
        List<PayDTO> collect = payHistoryRepo.findAllByActionAndStatusOrderByCreatedAtDescc(PayStatus.OUT, PayStatus.PENDING).stream()
                .map(this::toPayDTO)
                .collect(Collectors.toList());
        model.addAttribute("data", collect);
        return "approvePayment";
    }

    @Override
    public String acceptPayment(Model model, UUID id) {
        payHistoryRepo.findById(id).ifPresent(i -> {
            i.setStatus(PayStatus.PAYED);
            payHistoryRepo.save(i);
        });
        return getPaymentPage(model);
    }

    @Override
    public String rejectPayment(Model model, UUID id) {
        payHistoryRepo.findById(id).ifPresent(i -> {
            i.setStatus(PayStatus.REJECT);
            payHistoryRepo.save(i);
        });
        return getPaymentPage(model);
    }

    @Override
    public String payedPage(Model model) {
        List<PayDTO> collect = payHistoryRepo.findAllByActionAndStatusOrderByCreatedAtDescc(PayStatus.OUT, PayStatus.PAYED).stream()
                .map(this::toPayDTO)
                .collect(Collectors.toList());
        model.addAttribute("data", collect);
        return "payed";
    }

    private PayDTO toPayDTO(PayHistory payHistory) {
        PayDTO payDTO = new PayDTO();
        payDTO.setId(payHistory.getId());
        payDTO.setUserName(payHistory.getUser().getName());
        payDTO.setChatId(payHistory.getUser().getChatId());
        payDTO.setPhone(payHistory.getUser().getPhone());
        payDTO.setOrderCode(payHistory.getOrderCode());
        payDTO.setUserBalance(payHistory.getUserBalance());
        payDTO.setWithdraw(payHistory.getAmount());
        payDTO.setTime(payHistory.getCreatedAt());
        payDTO.setCard(payHistory.getCard() == null ? "" : payHistory.getCard().getNumber());
        return payDTO;
    }
}
