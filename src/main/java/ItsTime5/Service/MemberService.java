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
    public void save(Member member) {
        try {
            memberRepository.save(member);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("**member save error**");
        }
    }

    @Transactional
    public void modifyNickname(Long id, String nickname){
        try{
            Member member = memberRepository.findOne(id);
            member.setNickname(nickname);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("회원 조회가 잘못 되었습니다.");
        }
    }

    @Transactional
    public void saveReview(Review review){
        int amount=0;

        memberRepository.saveReview(review);
        amount = (review.getStar()-3); //star 1~5라고 가정하고 3을 +0으로 둠
        review.getRecipient().modifyBattery(amount);
    }

    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public List<Review> findAllReview(Long memberId){return memberRepository.findAllReview(memberId);}
}
