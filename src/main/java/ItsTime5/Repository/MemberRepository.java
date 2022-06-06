package ItsTime5.Repository;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Member.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member){
        //프론트에서 user id 값으로 받아오나?
        em.persist(member);
    }

    public void saveReview(Review review){
        em.persist(review);
    }

    public Member findOne(Long id){
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    public List<Review> findAllReview(Long memberId){
        return em.createQuery("select r from Review r where where r.recipient = :memberId", Review.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }

}
