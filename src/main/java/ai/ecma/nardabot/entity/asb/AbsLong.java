package ai.ecma.nardabot.entity.asb;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public abstract class AbsLong extends AbsDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
