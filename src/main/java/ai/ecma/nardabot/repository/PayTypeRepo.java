package ai.ecma.nardabot.repository;

import ai.ecma.nardabot.entity.PayType;
import ai.ecma.nardabot.enums.PayTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayTypeRepo extends JpaRepository<PayType, Long> {

    PayType getByType(PayTypes type);

}
