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

    private StudyInfo studyInfo; //스터디에 필요한 정보들

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<Question> questionList = new ArrayList<>(); //스터디의 설문 문항들

    @Enumerated(EnumType.STRING)
    private RecruitStatus status = RecruitStatus.recruit; //모집 status
    private LocalDateTime postTime = LocalDateTime.now(); //스터디 모집 게시글 게시 시간

    public void modifyPersonLimit(int amount){
        //인원 제한 관리
        studyInfo.modifyPersonLimit(amount);
    }

    /*연관관계 메서드*/
    public void setQuestion(Question question){
        questionList.add(question);
        question.setStudy(this);
    }
}
