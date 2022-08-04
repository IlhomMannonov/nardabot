package ai.ecma.nardabot.entity;

import ai.ecma.nardabot.entity.asb.AbsEntityWithUUID;
import ai.ecma.nardabot.utills.TableName;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@SQLDelete(sql = ("update " + TableName.ATTACHMENT + " set deleted = true where id = ?"))
@Entity
@Table(name = TableName.ATTACHMENT)
public class Attachment extends AbsEntityWithUUID {

    private String telegramId;
}
