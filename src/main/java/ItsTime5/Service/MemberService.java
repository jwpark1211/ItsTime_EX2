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
    private final StudyMemberService studyMemberService;

    /*@Transactional 안 붙은 건 조회만, 나머지는 추가적인 작업이 필요한 것*/

    //유저 회원가입
    @Transactional
    public Long join(Member member) {
           return memberRepository.save(member);
    }

    //유저 탈퇴
    @Transactional
    public Long removeMember(Long id){
        studyMemberService.removeAllStudyMemberByMember(id);
       return memberRepository.removeMember(id);
        /*유저의 후기는 cascade.All 상태이기 때문에 따로 로직을 두지 않음.
        => 보낸, 받은 후기 함께 삭제 + 변경된 배터리는 그대로 */
    }

    //유저의 후기를 저장하고 배터리를 수정함.
    @Transactional
    public Long saveReview(Review review){
        int amount = (review.getStar()-3); //star 1~5라고 가정하고 3보다 작으면 battery -, 크면 +
        review.getRecipient().modifyBattery(amount);
        return memberRepository.saveReview(review);
    }

    //유저의 닉네임을 수정
    @Transactional
    public Long modifyNickname(Long id, String nickname){
        Member member = memberRepository.findOne(id);
        member.setNickname(nickname);
        return member.getId();
    }

    //id를 통해 유저 찾아오기
    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }

    //모든 유저 찾아오기
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    //유저가 받은 모든 후기 가져오기
    public List<Review> findAllReviewWithMember(Long id) {
        return memberRepository.findAllReviewWithMember(id);
    }
}
