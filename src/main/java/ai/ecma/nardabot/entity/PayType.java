package ai.ecma.nardabot.entity;

import ai.ecma.nardabot.entity.asb.AbsLong;
import ai.ecma.nardabot.enums.PayTypes;
import ai.ecma.nardabot.utills.TableName;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@SQLDelete(sql = ("update " + TableName.PAY_TYPE + " set deleted = true where id = ?"))
@Entity(name = TableName.PAY_TYPE)
public class PayType extends AbsLong {

    @Enumerated(EnumType.STRING)
    private PayTypes type;

    private boolean active;
}
