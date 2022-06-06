package ItsTime5.Domain;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Member.MemberInfo;
import ItsTime5.Repository.MemberRepository;
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
public class MemberDomainTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void MemberSaveTest(){

        Member member = new Member();
        MemberInfo memberInfo = new MemberInfo("박정운","snb4560@gmail.com","Wjddns1234");
        member.setInfo(memberInfo);
        member.setNickname("0606 개발중");

        memberRepository.save(member);

        Assertions.assertThat(member).isSameAs(memberRepository.findOne(member.getId()));
    }
}
