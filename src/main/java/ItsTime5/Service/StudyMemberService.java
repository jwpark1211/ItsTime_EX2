package ItsTime5.Service;

import ItsTime5.Domain.StudyMember.Answer;
import ItsTime5.Domain.StudyMember.Comment;
import ItsTime5.Domain.StudyMember.StudyMember;
import ItsTime5.Domain.StudyMember.StudyMemberStatus;
import ItsTime5.Repository.StudyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public void saveAnswer(Answer answer){
        studyMemberRepository.saveAnswer(answer);
    }

    //댓글 저장
    @Transactional
    public Long saveComment(Comment comment){
        Long id = studyMemberRepository.saveComment(comment);
        return id;
    }

    //댓글 수정
    @Transactional
    public void modifyComment(Long id, String modifyComment){
        Comment comment = studyMemberRepository.findOneComment(id);
        comment.setComment(modifyComment);
    }

    //스터디 유저 지원 수락
    @Transactional
    public void joinStudy(StudyMember studyMember){
        studyMember.setStatus(StudyMemberStatus.join);
        studyMember.getStudy().modifyPersonLimit(-1);
    }

    //유저의 스터디 유저 전체 삭제하기
    @Transactional
    public void removeAllStudyMemberByMember(Long memberId){
        List<StudyMember> studyMemberListOfMember = this.findAllStudyMemberWithMemberId(memberId);

        for (StudyMember studyMember : studyMemberListOfMember) {
            this.removeStudyMember(studyMember.getId());
        }
    }

    @Transactional
    public void removeComment(Long id){
        studyMemberRepository.removeComment(id);
    }
    //스터디 유저 id로 찾기
    public StudyMember findOne(Long id){
        return studyMemberRepository.findOne(id);
    }

    //유저 아이디로 유저의 스터디 유저 전체 가져오기
    public List<StudyMember> findAllStudyMemberWithMemberId(Long memberId){
        return studyMemberRepository.findAllStudyMemberWithMemberId(memberId);
    }
}
