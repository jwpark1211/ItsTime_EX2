package ItsTime5.Domain.Study;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
public class Study{

    @Id @GeneratedValue
    @Column(name = "study_id")
    private Long id;

    private StudyInfo studyInfo;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<Question> questionList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private RecruitStatus status = RecruitStatus.recruit;
    private LocalDateTime postTime = LocalDateTime.now();

    public void modifyPersonLimit(int amount){
        studyInfo.modifyPersonLimit(amount);
    }

    /*연관관계 메서드*/
    public void setQuestion(Question question){
        questionList.add(question);
        question.setStudy(this);
    }
}
