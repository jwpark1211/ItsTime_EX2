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

    private final StudyMemberRepository studyMemberRepository;

    @Transactional
    public void save(StudyMember studyMember, Answer ... answers){
        try {
            studyMemberRepository.save(studyMember);

            for (Answer answer : answers) {
                studyMemberRepository.saveAnswer(answer);
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("**studyMember save error**");
        }
    }

    @Transactional
    public void joinStudy(StudyMember studyMember){
        studyMember.setStatus(StudyMemberStatus.join);
        studyMember.getStudy().modifyPersonLimit(-1);
    }

}
