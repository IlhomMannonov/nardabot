package ai.ecma.nardabot.component;

import ai.ecma.nardabot.entity.PayType;
import ai.ecma.nardabot.entity.Role;
import ai.ecma.nardabot.enums.PayTypes;
import ai.ecma.nardabot.enums.RoleNames;
import ai.ecma.nardabot.repository.PayTypeRepo;
import ai.ecma.nardabot.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RoleRepo roleRepo;
    private final PayTypeRepo payTypeRepo;

    @Value(value = "${spring.sql.init.mode}")
    private String value;

    @Override
    public void run(String... args) throws Exception {
        if (value.equals("always")) {
            //ROLELARNI SAQLASH
            roles();

            payTypes();

        }
    }

    private void payTypes() {
        payTypeRepo.save(PayType.builder()
                .type(PayTypes.CASH)
                .active(true)
                .build());
        payTypeRepo.save(PayType.builder()
                .type(PayTypes.PAYEER)
                .active(true)
                .build());
        payTypeRepo.save(PayType.builder()
                .type(PayTypes.WEBMONEY)
                .active(true)
                .build());
        payTypeRepo.save(PayType.builder()
                .type(PayTypes.QIWI)
                .active(true)
                .build());
        payTypeRepo.save(PayType.builder()
                .type(PayTypes.CUSTOMER)
                .active(true)
                .build());
    }

    private void roles() {
        roleRepo.save(Role.builder()
                .setName(RoleNames.ROLE_CUSTOMER)
                .build());
        roleRepo.save(Role.builder()
                .setName(RoleNames.ROLE_ADMIN)
                .build());
    }
}
