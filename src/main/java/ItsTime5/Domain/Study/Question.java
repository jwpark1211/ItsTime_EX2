package ItsTime5.Domain.Study;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id @GeneratedValue
    @Column(name = "question_id")
    private Long id;

    @NotNull
    private String question; //설문 문항

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study; //어떤 스터디가 이 설문을 가지는지

    public Question( String question) {
        this.question = question;
    }

}
