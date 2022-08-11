package ai.ecma.nardabot.repository;

import ai.ecma.nardabot.entity.PayHistory;
import ai.ecma.nardabot.enums.PayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PayHistoryRepo extends JpaRepository<PayHistory, UUID> {
    Optional<PayHistory> findByUserId(UUID user_id);
    Boolean existsByUserId(UUID user_id);

    @Query(value = "select * from pay_history p where deleted=false and user_id = :user_id order by created_at desc limit 10", nativeQuery = true)
    List<PayHistory> findAllByUserId(UUID user_id);

    @Query(value = "select code from pay_history p order by code desc limit 1", nativeQuery = true)
    Long getCode();

    Optional<PayHistory> findFirstByUserIdAndActionOrderByCreatedAtDesc(UUID user_id, PayStatus action);

    @Query(value = "select *\n" +
            "from pay_history p\n" +
            "where p.action = :action :: varchar\n" +
            "  and p.status = :status :: varchar\n" +
            "order by created_at desc", nativeQuery = true)
    List<PayHistory> findAllByActionAndStatusOrderByCreatedAtDescc(PayStatus action, PayStatus status);


    @Query(value = "select * from pay_history p where p.user_id = :user_id order by created_at desc limit 1", nativeQuery = true)
    Optional<PayHistory> findFirstByUserIdAndDeletedOrderByCreatedAtDesc(UUID user_id);
}
