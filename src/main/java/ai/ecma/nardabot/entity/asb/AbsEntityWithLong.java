package ai.ecma.nardabot.entity.asb;


import javax.persistence.Column;

public abstract class AbsEntityWithLong extends AbsLong {
    @Column(name = "name")
    private String name;
}
