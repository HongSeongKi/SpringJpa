package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {
    //스프링 데이터 jpa가 구현체를 만들어서 여기에다가 인젝션, memberRepository에는 프록시 객체가 들어감
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Optional<Member> findMember = memberRepository.findById(savedMember.getId());

        Assertions.assertEquals(savedMember.getId(), findMember.get().getId());
        assertThat(findMember.get().getUsername()).isEqualTo(member.getUsername());
    }


    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUserNameAndAgeGreaterThen() {
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA",15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy(){
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA",10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for(String s : usernameList){
            System.out.println("s = "+s);
        }
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> result = memberRepository.findMemberDto();
        for(MemberDto dto : result){
            System.out.println("dto = "+dto);
        }
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<String> list = new ArrayList<>();
        list.add(m1.getUsername());
        list.add(m2.getUsername());

        List<Member> result = memberRepository.findByNames(list);

        for(Member member : result){
            System.out.println("member = "+member);
        }
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //data jpa에서 List는 데이터 없으면 그냥 null이 아니라 없는걸로 준다, 그래서 exception 나지않는다.
        List<Member> aaa = memberRepository.findListMemberByUsername("AAA");
        //단건인 경우에는 데이터 없으면, null
        Member aaa1 = memberRepository.findMemberByUsername("AAA");
        System.out.println("aaa1 : "+aaa1);
        Optional<Member> aaa2 = memberRepository.findOptionalMemberByUsername("AAA");
        System.out.println("aaa2 : "+aaa2);
    }

    @Test
    public void paging(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0,3,Sort.by(Sort.Direction.DESC,"username"));

        //when , page가 totalcount까지 같이 날려서 totalcount를 위한 쿼리 필요없음
        Page<Member> page = memberRepository.findByAge(age,pageRequest);

        //dto로 변환, 이걸 나중에 반환하기, 페이지 유지하면서 Dto로 반환.
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //totalcount 쿼리 안날리고 3개 가저오라하면 4개 가저온다.(요청한것보다 몇개 더가저온다) 현재 findByAge는 반환형이 Page인데 Slice로 바꿔서 실행하기.
        Slice<Member> slice = memberRepository.findByAge(age,pageRequest);

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for(Member mem : content){
            System.out.println("member = "+mem);
        }
        System.out.println("totalElements = "+totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0); //페이지 넘버
        assertThat(page.getTotalPages()).isEqualTo(2); //페이지 전체개수
        assertThat(page.isFirst()).isTrue(); // 첫번째 페이지
        assertThat(page.hasNext()).isTrue(); // 다음페이지 존재여부
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0); //여기서 나이는 40살이다. 벌크연산은 영속성컨텍스트에 반영하지 않기때문에.. => 그래서 벌크연산이후에 영속성컨텍스트 날려준다.

        em.flush(); //쿼리 보내기
        em.clear(); //영속성 컨텍스트 날리기
        //사실 위의 역할을 data jpa에서는 Modifying옆에 clearAutomatically = true 넣어줌으로써 해결가능

        result = memberRepository.findByUsername("member5");
        member5 = result.get(0); // 여기서는 41살로 들어옴옴


        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1",10,teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        //select Member 1 , team N => N+1문제 => fetch join으로 해결 , findMemberFetchJoin
        //spring data jpa에서는 entitygraph를 통해서 메소드이름으로 패치조인을 사용할 수 있다.
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for(Member mem : members){
            System.out.println("mem = "+mem); //Member만 select함
            System.out.println("mem.teamclass = "+mem.getTeam().getClass()); //텅텅빈 프록시 객체가 출력됨
            System.out.println("mem.team = "+mem.getTeam().getName()); //이때 실제로 프록시 객체의 team에 실제 데이터 저장됨
        }
    }

    @Test
    public void queryHint(){
        //given
        Member member = new Member("member1",10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();
        findMember.setUsername("member2");

    }

    @Test
    public void lock(){
        //given
        Member member = new Member("member1",10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom(){
        memberRepository.findMemberCustom();
    }

    @Test
    public void queryByExample() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        //Probe
        Member member = new Member("m1");
        Team team = new Team("teamA");
        member.setTeam(team);
        //team이랑 member랑 이너조인하며 두개의 name을 사용한다.

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age"); //age정보를 빼기 위해서 사용,username으로만 매칭

        Example<Member> example = Example.of(member ,matcher);

        List<Member> result = memberRepository.findAll(example);

        assertThat(result.get(0).getUsername()).isEqualTo("m1");
    }

    @Test
    public void projections(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        //List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

        List<UsernameOnlyDto> all = memberRepository.findProjectionsGenericByUsername("m1",UsernameOnlyDto.class);

        for(UsernameOnlyDto usernameOnly : all){
            System.out.println("usernameOnly = "+usernameOnly);
        }

        //team 컬럼들 다들고온다.
        List<NestedClosedProjections> all2 = memberRepository.findProjectionsGenericByUsername("m1",NestedClosedProjections.class);
        for(NestedClosedProjections prj : all2){
            System.out.println("usernameOnly = "+prj);
        }

    }


}
