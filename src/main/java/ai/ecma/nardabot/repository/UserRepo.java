package ai.ecma.nardabot.repository;

import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.Lang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByChatId(String chatId);

    User getByChatId(String chatId);

    List<User> findAllByLanguageAndPhoneIsNotNull(Lang language);

    List<User> findAllByPhoneIsNotNull();
}
