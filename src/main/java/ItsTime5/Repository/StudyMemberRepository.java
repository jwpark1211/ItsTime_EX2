package ItsTime5.Repository;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Study.Study;
import ItsTime5.Domain.StudyMember.Answer;
import ItsTime5.Domain.StudyMember.Comment;
import ItsTime5.Domain.StudyMember.StudyMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.print.DocFlavor;
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

    //id 값을 통해 댓글 가져오기
    public Comment findOneComment(Long id){
        return em.find(Comment.class,id);
    }

    //댓글 저장하기
    public Long saveComment(Comment comment){
        em.persist(comment);
        return comment.getId();
    }

    //댓글 삭제하기
    public void removeComment(Long id){
        em.remove(this.findOneComment(id));
    }

    //id 값을 통해 가져온 스터디 유저의 지원서 모두 가져오기
    public List<Answer> findAllAnswer(Long studyMemberId){
        String jpql  = "select a from Answer a join fetch a.studyMember s where s.id = :studyMemberId ";
        return em.createQuery(jpql,Answer.class)
                .setParameter("studyMemberId",studyMemberId)
                .getResultList();
    }

    //유저의 id 값을 통해 그 유저의 스터디 유저 전체 가져오기
    public List<StudyMember> findAllStudyMemberWithMemberId(Long memberId){
        String jpql = "select sm from StudyMember sm join fetch sm.member m where m.id = :memberId";
        return em.createQuery(jpql, StudyMember.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }


    //스터디 유저의 id 값을 통해 그 유저의 스터디 가져오기
    public Study findAllStudyWithStudyMemberId(Long studyMemberId){
        String jpql = "select s from StudyMember sm join sm.study s where sm.id = :studyMemberId";
        return em.createQuery(jpql,Study.class)
                .setParameter("studyMemberId",studyMemberId)
                .getSingleResult();
    }

    //스터디 유저의 id 값을 통해 그 스터디 유저의 member 가져오기
    public Member findMemberWithStudyMemberId(Long studyMemberId){
        String jpql  = "select m from StudyMember sm join sm.member m where sm.id = :studyMemberId";
        return em.createQuery(jpql,Member.class)
                .setParameter("studyMemberId",studyMemberId)
                .getSingleResult();
    }

    //스터디의 id 값을 통해 그 스터디의 스터디 유저 모두 가져오기
    public List<StudyMember> findStudyMemberWithStudyId(Long studyId){
        String jpql = "select sm from StudyMember sm join fetch sm.study s where s.id = :studyId";
        return em.createQuery(jpql,StudyMember.class)
                .setParameter("studyId",studyId)
                .getResultList();
    }
}
