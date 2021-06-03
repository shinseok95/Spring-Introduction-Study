package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository repository;

    @BeforeEach
    public void beforeEach(){

        // 의존성 주입 (DI)
        repository = new MemoryMemberRepository();
        memberService = new MemberService(repository);
    }

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    void join() {

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
    void validdateDuplicateMember(){

        //given

        Member member1 = new Member();
        member1.setName("Spring");

        Member member2 = new Member();
        member2.setName("Spring");

        //when

        memberService.join(member1);

        // assertThorows 메소드 사용(기대되는 결과, 실제 수행 결과)
        assertThrows(IllegalStateException.class, ()->memberService.join(member2));

        // try~catch 문으로 테스트
        /*
        try{
            memberService.join(member2);
            fail();

        }catch (IllegalStateException e){
            Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
        */

        //then
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}