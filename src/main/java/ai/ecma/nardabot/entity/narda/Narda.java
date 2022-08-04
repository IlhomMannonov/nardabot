package ai.ecma.nardabot.entity.narda;

import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.entity.asb.AbsUUID;
import ai.ecma.nardabot.enums.NardaType;
import ai.ecma.nardabot.utills.TableName;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

//BU CLASS TELEGRAMDA HABARNI OZGARISHLARINI SAQLAB YURISH UCHUN KERAK
// MASAN USER OVERNI VA 10000 SO'M PUL TIKGAN SHUNI BILISH UCHUN KERAK


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@SQLDelete(sql = ("update " + TableName.NARDA + " set deleted = true where id = ?"))
@Entity(name = TableName.NARDA)
public class Narda extends AbsUUID {

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private NardaType type;

    private BigDecimal amount = BigDecimal.ZERO;
}
