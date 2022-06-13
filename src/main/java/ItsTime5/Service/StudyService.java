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
    public Long save(Study study) {
        Long id = studyRepository.save(study);
        return id;
    }

    @Transactional
    public void saveQuestion(Question...questions){
        for (Question question : questions) {
            studyRepository.saveQuestion(question);
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

    public List<Study> findAllWithQuestion() {
        return studyRepository.findAllStudyWithQuestion();
    }

    public Study findOneWithQuestion(Long id) {
        return studyRepository.findOneStudyWithQuestion(id);
    }

    @Transactional
    public void removeStudy(Long id) {
        studyRepository.removeStudy(id);
    }
}
