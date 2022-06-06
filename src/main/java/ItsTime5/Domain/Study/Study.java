package ItsTime5.Domain.Study;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter @Setter
public class Study {

    @Id @GeneratedValue
    @Column(name = "study_id")
    private Long id;

    private StudyInfo studyInfo;

    @Enumerated(EnumType.STRING)
    private RecruitStatus status = RecruitStatus.recruit;
    private LocalDateTime postTime = LocalDateTime.now();

}
