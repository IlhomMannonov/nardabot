package ai.ecma.nardabot.entity.asb;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@ToString
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class AbsEntityWithUUID extends AbsUUID {
    @Column(name = "name")
    private String name;
}
