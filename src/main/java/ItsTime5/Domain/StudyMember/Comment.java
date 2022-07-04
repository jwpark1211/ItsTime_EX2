package ItsTime5.Domain.StudyMember;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String comment; //내용

    private int groupNum; //댓글 그룹
    private int layer; //계층
    private int sequence; //순서
    private LocalDateTime postTime = LocalDateTime.now(); //게시 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id")
    private StudyMember studyMember;

    public Comment(String comment, int groupNum, int layer, int sequence,
                   StudyMember studyMember) {
        this.comment = comment;
        this.groupNum = groupNum;
        this.layer = layer;
        this.sequence = sequence;
        this.studyMember = studyMember;
    }
}
