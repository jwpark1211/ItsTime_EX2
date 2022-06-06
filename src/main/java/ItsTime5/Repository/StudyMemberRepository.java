package ItsTime5.Repository;

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

    public StudyMember findOne(Long id){
        return em.find(StudyMember.class,id);
    }

    public List<StudyMember> findAll(){
        return em.createQuery("select s from StudyMember s",StudyMember.class)
                .getResultList();
    }
}
