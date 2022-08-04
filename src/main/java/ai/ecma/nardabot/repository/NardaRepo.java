package ai.ecma.nardabot.repository;

import ai.ecma.nardabot.entity.narda.Narda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NardaRepo extends JpaRepository<Narda, UUID> {
    Optional<Narda> findByUserId(UUID user_id);

    Narda getByUserId(UUID user_id);
}
