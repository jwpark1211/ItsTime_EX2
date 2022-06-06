package ItsTime5.Domain.StudyMember;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Study.Study;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class StudyMember {

    @Id @GeneratedValue
    @Column(name = "studyMember_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @Enumerated(EnumType.STRING)
    private StudyMemberStatus status;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

}
