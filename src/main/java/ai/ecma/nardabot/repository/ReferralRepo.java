package ai.ecma.nardabot.repository;

import ai.ecma.nardabot.entity.Referral;
import ai.ecma.nardabot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReferralRepo extends JpaRepository<Referral, UUID> {
    Boolean existsByFromUserAndToUser(User fromUser, User toUser);
    Boolean existsByToUser(User toUser);

    Integer countByFromUser(User fromUser);
}
