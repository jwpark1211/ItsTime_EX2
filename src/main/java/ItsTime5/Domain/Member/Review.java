package ItsTime5.Domain.Member;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Review {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender; //보낸 이

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Member recipient; //받은 이

    @NotNull
    private int Star;

    public Review(Member sender, Member recipient, int star) {
        this.sender = sender;
        this.recipient = recipient;
        Star = star;
    }
}
