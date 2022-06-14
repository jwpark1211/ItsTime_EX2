package ItsTime5.Service;

import ItsTime5.Domain.StudyMember.Answer;
import ItsTime5.Domain.StudyMember.StudyMember;
import ItsTime5.Domain.StudyMember.StudyMemberStatus;
import ItsTime5.Repository.StudyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyMemberService {

    /*@Transactional 안 붙은 건 조회만, 나머지는 추가적인 작업이 필요한 것*/

    private final StudyMemberRepository studyMemberRepository;

    //스터디 유저 저장
    @Transactional
    public Long save(StudyMember studyMember){
        Long id =studyMemberRepository.save(studyMember);
        return id;
    }

    //스터디 유저 탈퇴
    @Transactional
    public Long removeStudyMember(Long id){
        StudyMember studyMember = studyMemberRepository.findOne(id);
        studyMember.getStudy().modifyPersonLimit(+1);
        return studyMemberRepository.removeStudyMember(id);
    }

    //지원서 저장
    @Transactional
    public void saveAnswer(Answer ... answers){
        for (Answer answer : answers) {
            studyMemberRepository.saveAnswer(answer);
        }
    }

    //스터디 유저 지원 수락
    @Transactional
    public void joinStudy(StudyMember studyMember){
        studyMember.setStatus(StudyMemberStatus.join);
        studyMember.getStudy().modifyPersonLimit(-1);
    }

    //스터디 유저 id로 찾기
    public StudyMember findOne(Long id){
        return studyMemberRepository.findOne(id);
    }

}
