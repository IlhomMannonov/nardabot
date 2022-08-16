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

    @Query(value = "with t as (select *\n" +
            "                       from pay_history p\n" +
            "                       where deleted = false\n" +
            "                         and user_id = :user_id\n" +
            "                       limit 5) select * from t order by code ", nativeQuery = true)
    List<PayHistory> findAllByUserId(UUID user_id);

    @Query(value = "select code from pay_history p order by code desc limit 1", nativeQuery = true)
    Long getCode();

    Optional<PayHistory> findFirstByUserIdAndActionOrderByCreatedAtDesc(UUID user_id, PayStatus action);

    @Query(value = "select *\n" +
            "from pay_history p\n" +
            "where p.action = :action\n" +
            "  and p.status = :status\n" +
            "  and case\n" +
            "          when :i is true then deleted = false else true end\n" +
            "order by created_at desc", nativeQuery = true)
    List<PayHistory> findAllByActionAndStatusOrderByCreatedAtDesc(String action, String status, boolean i);


    @Query(value = "select * from pay_history p where p.user_id = :user_id order by created_at desc limit 1", nativeQuery = true)
    Optional<PayHistory> findFirstByUserIdAndDeletedOrderByCreatedAtDesc(UUID user_id);
}
