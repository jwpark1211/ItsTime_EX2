package ItsTime5.Service;

import ItsTime5.Domain.Study.Question;
import ItsTime5.Domain.Study.RecruitStatus;
import ItsTime5.Domain.Study.Study;
import ItsTime5.Domain.Study.StudyInfo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudyServiceTest {

    @Autowired StudyService studyService;

    @Test
    @Transactional
    public void SaveTest(){
       Study study = getStudy1();
       studyService.save(study);

       Assertions.assertThat(study).isSameAs(studyService.findOne(study.getId()));
    }

    @Test
    @Transactional
    public void modifyTest(){
        Study study = getStudy1();
        studyService.save(study);

        StudyInfo studyInfo = new StudyInfo("동대문구 배드민턴 모집","동대문","월",
                "대면","취미",6,"대학생 위주로 모집해요!");

        studyService.modifyStudyInfo(study.getId(),studyInfo);

        Assertions.assertThat("동대문").isEqualTo(studyService.findOne(study.getId()).getStudyInfo().getRegion());
    }

    @Test
    @Transactional
    public void EndRecruitTest(){
        Study study = getStudy1();
        studyService.save(study);

        studyService.endRecruit(study.getId());

        Assertions.assertThat(RecruitStatus.endRecruit).isEqualByComparingTo(study.getStatus());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void saveQuestion(){
        Study study1 = getStudy1();
        Study study2 = getStudy2();

        Question question1 = new Question("당신의 나이는?", study1);
        Question question2 = new Question("당신의 이름은?",study2);

        studyService.save(study1,question1);
        studyService.save(study2,question2);

        Assertions.assertThat(1).isEqualTo(studyService.findAllQuestion(study1.getId()).size());
    }

    private Study getStudy1() {
        StudyInfo studyInfo = new StudyInfo("강남구 배드민턴 모집","강남","월",
                "대면","취미",6,"대학생 위주로 모집해요!");
        Study study = new Study();
        study.setStudyInfo(studyInfo);
        study.setStatus(RecruitStatus.recruit);
        return study;
    }

    private Study getStudy2() {
        StudyInfo studyInfo = new StudyInfo("야탑 모각코","수도권","월화수",
                "대면","프로그래밍",6,"각자 모여서 모각코 할 사람 구해요");
        Study study = new Study();
        study.setStudyInfo(studyInfo);
        study.setStatus(RecruitStatus.recruit);
        return study;
    }
}
