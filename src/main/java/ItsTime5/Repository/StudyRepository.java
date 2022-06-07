package ItsTime5.Repository;

import ItsTime5.Domain.Study.Question;
import ItsTime5.Domain.Study.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyRepository {

    private final EntityManager em;

    public void save(Study study){
        em.persist(study);
    }

    public void saveQuestion(Question question){
        em.persist(question);
    }

    public Study findOne(Long id){
        return em.find(Study.class,id);
    }

    public List<Study> findAll(){
        return em.createQuery("select s from Study s",Study.class)
                .getResultList();
    }

    public List<Question> findAllQuestion(Long studyId){
        String jpql = "select q from Question q join q.study s where s.id = :studyId ";
        return em.createQuery(jpql,Question.class)
                .setParameter("studyId",studyId)
                .getResultList();
    }
}
