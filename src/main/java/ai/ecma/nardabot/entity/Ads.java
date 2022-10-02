package ai.ecma.nardabot.entity;


import ai.ecma.nardabot.entity.asb.AbsEntityWithUUID;
import ai.ecma.nardabot.entity.asb.AbsUUID;
import ai.ecma.nardabot.utills.TableName;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
@SQLDelete(sql = ("update " + TableName.ADS + " set deleted = true where id = ?"))
@Entity
@Table(name = TableName.ADS)
public class Ads extends AbsUUID {
    @Column(name = "caption")
    private String caption;

    private InlineKeyboardMarkup markup;

    @Column(name = "media")
    private String media;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "done")
    private boolean done;

    @Column(name = "is_photo")
    private Boolean isPhoto;

}
