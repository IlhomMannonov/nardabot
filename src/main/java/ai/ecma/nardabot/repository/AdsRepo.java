package ai.ecma.nardabot.repository;

import ai.ecma.nardabot.entity.Ads;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdsRepo extends JpaRepository<Ads, UUID> {
    Ads findFirstByUserIdOrderByCreatedAtDesc(UUID user_id);
}
