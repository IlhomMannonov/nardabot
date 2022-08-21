package ai.ecma.nardabot.entity;

import ai.ecma.nardabot.entity.asb.AbsEntityWithUUID;
import ai.ecma.nardabot.entity.asb.AbsUUID;
import ai.ecma.nardabot.utills.TableName;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.jvnet.hk2.annotations.Optional;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@SQLDelete(sql = ("update " + TableName.REFERRAL + " set deleted = true where id = ?"))
@Entity(name = TableName.REFERRAL)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"to_user_id", "from_user_id"})})
public class Referral extends AbsUUID {
    @ManyToOne
    private User toUser;
    @ManyToOne
    private User fromUser;
}
