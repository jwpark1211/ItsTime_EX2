package ItsTime5.Domain.StudyMember;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Study.Study;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class StudyMember {

    @Id @GeneratedValue
    @Column(name = "study_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member; //스터디 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    @NotNull
    private Study study; //스터디

    @OneToMany(mappedBy = "studyMember",cascade = CascadeType.ALL)
    private List<Answer> answerList = new ArrayList<>(); //설문 답변

    @Enumerated(EnumType.STRING)
    private StudyMemberStatus status = StudyMemberStatus.submit;
    /*스터디 유저의 현재 status -> * 해당 스터디에 지원하는 순간 스터디 멤버 정보가 설문과 함께 저장되고,
    * 스터디 호스트가 지원을 수락하면 상태가 join 으로 변경된다. */

    @Enumerated(EnumType.STRING)
    private MemberGrade grade = MemberGrade.guest; //스터디의 작성자인지 여부 판별

    public StudyMember(Member member,Study study) {
        this.member = member;
        this.study = study;
    }

    //============연관관계 메서드============//
    public void setAnswer(Answer answer){
        answerList.add(answer);
        answer.setStudyMember(this);
    }
}
