package ItsTime5.Service;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Study.Question;
import ItsTime5.Domain.Study.RecruitStatus;
import ItsTime5.Domain.Study.Study;
import ItsTime5.Domain.Study.StudyInfo;
import ItsTime5.Domain.StudyMember.MemberGrade;
import ItsTime5.Domain.StudyMember.StudyMember;
import ItsTime5.Repository.MemberRepository;
import ItsTime5.Repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    /*@Transactional 안 붙은 건 조회만, 나머지는 추가적인 작업이 필요한 것*/

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberService studyMemberService;

    //스터디를 저장하고, 그 스터디를 만든 작성자 스터디 유저를 저장함.
    @Transactional
    public Long save(Study study,Long memberId) {
        Long id = studyRepository.save(study); //스터디 저장

        Member member = memberRepository.findOne(memberId); //작성자 저장
        StudyMember studyMember = new StudyMember();
        studyMember.setMember(member);
        studyMember.setStudy(study);
        studyMember.setGrade(MemberGrade.host);
        studyMemberService.joinStudy(studyMember);
        studyMemberService.save(studyMember);

        return id; //스터디 id 리턴
    }

    //스터디를 삭제함 -> cascade.All (StudyMember)
    @Transactional
    public void removeStudy(Long id) {
        studyRepository.removeStudy(id);
    }

    //스터디 info 를 한 번에 수정함 **주의: put 메소드이기 때문에 수정시 모든 값을 다시 받아와야 함
    @Transactional
    public void modifyStudyInfo(Long id, StudyInfo info){
        Study study = studyRepository.findOne(id);
        study.setStudyInfo(info);
    }

    //스터디의 모집을 마감함
    @Transactional
    public void endRecruit(Long id){
        Study study = studyRepository.findOne(id);
        study.setStatus(RecruitStatus.endRecruit);
    }

    //스터디의 질문들을 저장함
    @Transactional
    public void saveQuestion(Question...questions){
        for (Question question : questions) {
            studyRepository.saveQuestion(question);
        }
    }

    //id 값을 통해 스터디를 찾아옴
    public Study findOne(Long id){
        return studyRepository.findOne(id);
    }

    //모든 스터디를 찾아옴
    public List<Study> findAll(){
        return studyRepository.findAll();
    }

    //특정 스터디의 모든 질문을 찾아옴
    public List<Question> findAllQuestion(Long studyId){
        return studyRepository.findAllQuestion(studyId);
    }

    //모든 스터디를 질문과 함께 찾아옴
    public List<Study> findAllWithQuestion() {
        return studyRepository.findAllStudyWithQuestion();
    }

    //id를 통해 가져온 스터디를 질문과 함께 찾아옴
    public Study findOneWithQuestion(Long id) {
        return studyRepository.findOneStudyWithQuestion(id);
    }

    //조건에 맞는 스터디 가져오기 [검색 => Find_study_Page]
    public List<Study> findStudyListByCondition(String dayOfWeek, String isOnline, String categories){
        return studyRepository.findStudyListByCondition(dayOfWeek,isOnline,categories);
    }

}
