package ItsTime5.Domain.StudyMember;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Answer {

    @Id @GeneratedValue
    @Column(name = "application_id")
    private Long id;

    private int sequence;
    private String question;
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id")
    private StudyMember studyMember;

    public Answer( int sequence, String question, String answer, StudyMember studyMember) {
        this.sequence = sequence;
        this.question = question;
        this.answer = answer;
        this.studyMember = studyMember;
    }

}
