package ItsTime5.Domain.StudyMember;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Study.Study;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class StudyMember {

    @Id @GeneratedValue
    @Column(name = "study_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    @NotNull
    private Study study;

    @Enumerated(EnumType.STRING)
    private StudyMemberStatus status = StudyMemberStatus.submit;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade = MemberGrade.guest;

}
