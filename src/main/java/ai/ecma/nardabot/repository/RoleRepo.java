package ai.ecma.nardabot.repository;

import ai.ecma.nardabot.entity.Role;
import ai.ecma.nardabot.enums.RoleNames;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Role getByName(RoleNames name);
}
