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

    public Long  save(Member member){
        //프론트에서 user id 값으로 받아오나?
        em.persist(member);
        return member.getId();
    }

    public Long saveReview(Review review){
        em.persist(review);
        return review.getId();
    }

    public Member findOne(Long id){
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    public void removeMember(Long memberId){
         em.remove(this.findOne(memberId));
    }

    public List<Review> findAllReviewWithMember(Long id) {
        return em.createQuery(
                "select r from Review r"+
                        " join fetch r.sender s"+
                        " join fetch r.recipient re"+
                        " where re.id = :id",Review.class)
                .setParameter("id",id)
                .getResultList();
    }
}
