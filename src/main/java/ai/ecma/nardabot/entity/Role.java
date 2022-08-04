package ai.ecma.nardabot.entity;


import ai.ecma.nardabot.entity.asb.AbsLongNoDate;
import ai.ecma.nardabot.enums.RoleNames;
import ai.ecma.nardabot.utills.TableName;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(setterPrefix = "set")
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@SQLDelete(sql = ("update " + TableName.ROLE + " set deleted = true where id = ?"))
@Entity(name = TableName.ROLE)
public class Role extends AbsLongNoDate {

    @Enumerated(EnumType.STRING)
    private RoleNames name;


}
