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

    //유저를 저장
    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    //유저를 삭제
    public Long removeMember(Long memberId){
        Member member = this.findOne(memberId);
        Long id = member.getId();
        em.remove(member);
        return id;
    }

    //id 값으로 유저 가져오기
    public Member findOne(Long id){
        return em.find(Member.class,id);
    }

    //모든 유저 가져오기
    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    //후기를 저장하기
    public Long saveReview(Review review){
        em.persist(review);
        return review.getId();
    }

    //유저 id를 통해 그 유저의 모든 리뷰의 정보를 가져오기
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
