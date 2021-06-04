package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void join() throws Exception{

        //given

        Member member = new Member();
        member.setName("Spring");

        //when

        Long saveId = memberService.join(member);

        //then

        Member findMember = memberService.findOne(member.getId()).get();
        Assertions.assertThat(findMember.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void validdateDuplicateMember() throws Exception{

        //given

        Member member1 = new Member();
        member1.setName("Spring");

        Member member2 = new Member();
        member2.setName("Spring");

        //when

        memberService.join(member1);

        // assertThorows 메소드 사용(기대되는 결과, 실제 수행 결과)
        assertThrows(IllegalStateException.class, ()->memberService.join(member2));
    }

}
