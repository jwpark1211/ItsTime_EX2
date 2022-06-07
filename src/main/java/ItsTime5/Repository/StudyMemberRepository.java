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

    public void save(StudyMember studyMember){
        em.persist(studyMember);
    }

    public void saveAnswer(Answer answer){ em.persist(answer);}

    public StudyMember findOne(Long id){
        return em.find(StudyMember.class,id);
    }

    public List<StudyMember> findAll(){
        return em.createQuery("select s from StudyMember s",StudyMember.class)
                .getResultList();
    }

    public List<Answer> findAllAnswer(Long studyMemberId){
        String jpql  = "select a from Answer a join a.studyMember s where s.id = :studyMemberId ";
        return em.createQuery(jpql,Answer.class)
                .setParameter("studyMemberId",studyMemberId)
                .getResultList();
    }

}
