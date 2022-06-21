package ItsTime5.Domain.StudyMember;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Answer {

    @Id @GeneratedValue
    @Column(name = "answer_id")
    private Long id;

    private int sequence; // 순서
    private String question; // 설문 문항 [설문 문항이 변경될 것을 대비해 따로 저장]
    private String answer; //설문 답변

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id")
    private StudyMember studyMember; //설문을 작성한 스터디 유저

    public Answer( int sequence, String question, String answer, StudyMember studyMember) {
        this.sequence = sequence;
        this.question = question;
        this.answer = answer;
        this.studyMember = studyMember;
    }

}
