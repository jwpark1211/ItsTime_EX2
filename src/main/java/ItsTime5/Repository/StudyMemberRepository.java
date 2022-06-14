package ItsTime5.Repository;

import ItsTime5.Domain.StudyMember.Answer;
import ItsTime5.Domain.StudyMember.StudyMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyMemberRepository {

    private final EntityManager em;

    //스터디 유저 저장하기
    public Long save(StudyMember studyMember){
        em.persist(studyMember);
        return studyMember.getId();
    }

    //스터디 유저 삭제하기
    public Long removeStudyMember(Long id){
        em.remove(this.findOne(id));
        return id;
    }

    //id 값을 통해 스터디 유저 가져오기
    public StudyMember findOne(Long id){
        return em.find(StudyMember.class,id);
    }

    //모든 스터디 유저를 가져오기
    public List<StudyMember> findAll(){
        return em.createQuery("select s from StudyMember s",StudyMember.class)
                .getResultList();
    }

    //지원서 저장하기
    public void saveAnswer(Answer answer){ em.persist(answer);}

    //id 값을 통해 가져온 스터디 유저의 지원서 모두 가져오기
    public List<Answer> findAllAnswer(Long studyMemberId){
        String jpql  = "select a from Answer a join a.studyMember s where s.id = :studyMemberId ";
        return em.createQuery(jpql,Answer.class)
                .setParameter("studyMemberId",studyMemberId)
                .getResultList();
    }

}
