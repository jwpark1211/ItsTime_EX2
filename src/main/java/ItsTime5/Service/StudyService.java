package ItsTime5.Service;

import ItsTime5.Domain.Study.Question;
import ItsTime5.Domain.Study.RecruitStatus;
import ItsTime5.Domain.Study.Study;
import ItsTime5.Domain.Study.StudyInfo;
import ItsTime5.Repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    @Transactional
    public void save(Study study, Question...questions){
        try {
            studyRepository.save(study);

            for (Question question : questions) {
                studyRepository.saveQuestion(question);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("**study save error**");
        }
    }

    @Transactional
    public void modifyStudyInfo(Long id, StudyInfo info){
        Study study = studyRepository.findOne(id);
        study.setStudyInfo(info);
    }

    @Transactional
    public void endRecruit(Long id){
        Study study = studyRepository.findOne(id);
        study.setStatus(RecruitStatus.endRecruit);
    }

    public Study findOne(Long id){
        return studyRepository.findOne(id);
    }

    public List<Study> findAll(){
        return studyRepository.findAll();
    }

    public List<Question> findAllQuestion(Long studyId){
        return studyRepository.findAllQuestion(studyId);
    }

}
