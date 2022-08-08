package ai.ecma.nardabot.entity;

import ai.ecma.nardabot.entity.asb.AbsUUID;
import ai.ecma.nardabot.enums.PayStatus;
import ai.ecma.nardabot.utills.TableName;
import lombok.*;
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
@SQLDelete(sql = ("update " + TableName.PAY_HISTORY + " set deleted = true where id = ?"))
@Entity
@Table(name = TableName.PAY_HISTORY)
public class PayHistory extends AbsUUID {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PayType payType;

    private BigDecimal amount;


    //HOLATi
    @Enumerated(EnumType.STRING)
    private PayStatus status;

    //PULNI SOLGANLIGI YO'KI CHIQARGANLIGI HAQIDA
    @Enumerated(EnumType.STRING)
    private PayStatus action;

    @ManyToOne
    private Card card;

    @Column(name = "code")
    private String orderCode;

    private boolean active = false;
}
