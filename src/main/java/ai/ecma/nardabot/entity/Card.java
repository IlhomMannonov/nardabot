package ai.ecma.nardabot.entity;

import ai.ecma.nardabot.entity.asb.AbsUUID;
import ai.ecma.nardabot.utills.TableName;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@SQLDelete(sql = ("update " + TableName.CARD + " set deleted = true where id = ?"))
@Entity
@Table(name = TableName.CARD)
public class Card extends AbsUUID {
    private String number;

    @ManyToOne(optional = false)
    private User user;

}
