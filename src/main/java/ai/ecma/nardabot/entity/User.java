package ai.ecma.nardabot.entity;

import ai.ecma.nardabot.entity.asb.AbsEntityWithUUID;
import ai.ecma.nardabot.enums.Lang;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.utills.TableName;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@SQLDelete(sql = ("update " + TableName.USER + " set deleted = true where id = ?"))
@Entity(name = TableName.USER)
public class User extends AbsEntityWithUUID {

    @Column(name = "chat_id", nullable = false)
    private String chatId;

    @Column(name = "phone")
    private String phone;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Lang language;

    @Enumerated(EnumType.STRING)
    private State state;

    //BU BOOLEAN USER PUL SOLGANIDAN KEYIN OYIN OYNASH UCHUN MAJBURLAYDI
    private boolean gamed = false;


    private BigDecimal balance = new BigDecimal(0);

    @Column(name = "enable")
    private boolean enable;

}
