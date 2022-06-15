package ItsTime5.Repository;

import ItsTime5.Domain.Study.Question;
import ItsTime5.Domain.Study.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyRepository {

    private final EntityManager em;

    //스터디를 저장하기
    public Long save(Study study){
        em.persist(study);
        return study.getId();
    }

    //스터디를 삭제하기
    public void removeStudy(Long id) {
        em.remove(this.findOne(id));
    }

    //Id 값을 통해 스터디 가져오기
    public Study findOne(Long id){
        return em.find(Study.class,id);
    }

    //모든 스터디 가져오기
    public List<Study> findAll(){
        return em.createQuery("select s from Study s",Study.class)
                .getResultList();
    }

    //조건에 맞는 스터디 가져오기 [검색 => Find_study_Page]
    public List<Study> findStudyListByCondition(String dayOfWeek, String isOnline, String categories){

        //Criteria 동적 쿼리 생성
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Study> cq = cb.createQuery(Study.class);

        Root<Study> s = cq.from(Study.class);

        List<Predicate> criteria = new ArrayList<Predicate>();

        if(dayOfWeek != null) criteria.add(cb.equal(s.get("studyInfo").get("dayOfWeek"),
                cb.parameter(String.class, "dayOfWeek")));
        if(isOnline != null) criteria.add(cb.equal(s.get("studyInfo").get("isOnline"),
                cb.parameter(String.class, "isOnline")));
        if(categories != null) criteria.add(cb.equal(s.get("studyInfo").get("categories"),
                cb.parameter(String.class, "categories")));

        cq.where(cb.and(criteria.toArray(new Predicate[0])));

        TypedQuery<Study> query = em.createQuery(cq);
        if(dayOfWeek != null) query.setParameter("dayOfWeek",dayOfWeek);
        if(isOnline != null) query.setParameter("isOnline",isOnline);
        if(categories != null) query.setParameter("categories",categories);

        return query.getResultList();
    }

    // 질문을 저장하기
    public void saveQuestion(Question question){
        em.persist(question);
    }

    // 특정 스터디의 질문을 가져오기
    public List<Question> findAllQuestion(Long studyId){
        String jpql = "select q from Question q join q.study s where s.id = :studyId ";
        return em.createQuery(jpql,Question.class)
                .setParameter("studyId",studyId)
                .getResultList();
    }

    //모든 스터디 가져오기 + 질문까지 함께
    public List<Study> findAllStudyWithQuestion() {
        String jpql = "select distinct s from Study s join fetch s.questionList q";
        return em.createQuery(jpql,Study.class)
                .getResultList();
    }

    //id 값을 통해 한 스터디 가져오기 + 질문까지 함께
    public Study findOneStudyWithQuestion(Long id) {
        String jpql = "select distinct s from Study s join fetch s.questionList q "
                + "where s.id = :id";
        return em.createQuery(jpql, Study.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
