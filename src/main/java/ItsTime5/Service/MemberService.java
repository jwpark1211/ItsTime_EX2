package ItsTime5.Service;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Member.Review;
import ItsTime5.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
           return memberRepository.save(member);
    }

    @Transactional
    public void modifyNickname(Long id, String nickname){
        Member member = memberRepository.findOne(id);
        member.setNickname(nickname);
    }

    @Transactional
    public Long saveReview(Review review){
        int amount=0;
        amount = (review.getStar()-3); //star 1~5라고 가정하고 3을 +0으로 둠
        review.getRecipient().modifyBattery(amount);
        return memberRepository.saveReview(review);
    }

    @Transactional
    public void removeMember(Long id){
        memberRepository.removeMember(id);
    }

    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public List<Review> findAllReviewWithMember(Long id) {
        return memberRepository.findAllReviewWithMember(id);
    }
}
