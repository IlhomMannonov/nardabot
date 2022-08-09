package ai.ecma.nardabot.repository;

import ai.ecma.nardabot.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepo extends JpaRepository<Card, UUID> {
    boolean existsByUserId(UUID user_id);

    Card getByUserId(UUID user_id);

    boolean existsByNumber(String number);
}
