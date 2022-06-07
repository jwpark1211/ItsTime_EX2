package ItsTime5.Service;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Member.MemberInfo;
import ItsTime5.Domain.Member.Review;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        Review review = new Review(member2,member1,1);

        memberService.saveReview(review);

        Assertions.assertThat(78).isEqualTo(member1.getBattery());
    }

    @Test
    @Transactional
    public void memberReviewTest(){
        Member member1 = getMember1();
        Member member2 = getMember2();
        Member member3 = getMember3();

        memberService.save(member1);
        memberService.save(member2);
        memberService.save(member3);

        Review review1 = new Review(member1,member3,5);
        Review review2 = new Review(member2,member3,5);
        Review review3 = new Review(member3,member1,4);

        memberService.saveReview(review1);
        memberService.saveReview(review2);
        memberService.saveReview(review3);

        Assertions.assertThat(2).isEqualTo(memberService.findAllReview(member3.getId()).size());

    }


    //========================================================================================//


    private Member getMember1() {
        Member member = new Member();
        MemberInfo memberInfo = new MemberInfo("김규리","rlarbfl@gmail.com","1234");
        member.setInfo(memberInfo);
        member.setNickname("귤");
        return member;
    }

    private Member getMember2() {
        Member member = new Member();
        MemberInfo memberInfo = new MemberInfo("박정운","qkrwjddns@gmail.com","5678");
        member.setInfo(memberInfo);
        member.setNickname("운");
        return member;
    }

    private Member getMember3() {
        Member member = new Member();
        MemberInfo memberInfo = new MemberInfo("김민준","rlaalswns@gmail.com","2345");
        member.setInfo(memberInfo);
        member.setNickname("준");
        return member;
    }
}
