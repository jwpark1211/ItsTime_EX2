package ItsTime5.Service;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Member.MemberInfo;
import ItsTime5.Domain.Member.Review;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    @Transactional
    public void MemberSaveTest(){
        Member member = getMember1();
        memberService.save(member);
        Assertions.assertThat(member).isSameAs(memberService.findOne(member.getId()));
    }

    @Test
    @Transactional
    public void modifyTest(){
        Member member = getMember1();
        memberService.save(member);
        memberService.modifyNickname(member.getId(),"0606 개발중1");
        Assertions.assertThat(member.getNickname()).isEqualTo("0606 개발중1");
    }

    @Test
    @Transactional
    public void reviewTest(){
        Member member1 = getMember1();
        memberService.save(member1);

        Member member2 = getMember2();
        memberService.save(member2);

        Review review = new Review();
        review.setStar(1);
        review.setSender(member2);
        review.setRecipient(member1);

        memberService.saveReview(review);

        Assertions.assertThat(78).isEqualTo(member1.getBattery());
    }

    private Member getMember1() {
        Member member = new Member();
        MemberInfo memberInfo = new MemberInfo("김규리","rlarbfl@gmail.com","1234");
        member.setInfo(memberInfo);
        member.setNickname("0606 개발중");
        return member;
    }

    private Member getMember2() {
        Member member = new Member();
        MemberInfo memberInfo = new MemberInfo("박정운","qkrwjddns@gmail.com","5678");
        member.setInfo(memberInfo);
        member.setNickname("0606 개발중");
        return member;
    }
}
