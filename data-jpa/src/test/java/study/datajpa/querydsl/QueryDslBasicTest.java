package study.datajpa.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.dto.MemberDtoTwo;
import study.datajpa.dto.QMemberDtoTwo;
import study.datajpa.dto.UserDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.QMember;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.datajpa.entity.QMember.*;
import static study.datajpa.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    EntityManager em;

    //JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    @BeforeEach
    public void before(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        Member member3 = new Member("member3", 10, teamB);
        Member member4 = new Member("member4", 10, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL() {
        //member1을 찾아라
        Member findMember = em.createQuery("select m from Member m where m.username = :username",Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerdsl(){

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember m = new QMember("m");

        //컴파일타임에 쿼리 오류를 잡을 수 있다.
        Member findMember = queryFactory
                .selectFrom(m)
                .where(m.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        Member findMember2 = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        (member.age.eq(10)))
                .fetchOne(); //쉼표도 가능

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void resultFetch() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults(); // 쿼리 두개나간다. totalcount도 들고와야한다.

        results.getTotal();
        List<Member> content = results.getResults();

        long total = queryFactory
                .selectFrom(member)
                .fetchCount();

    }

    @Test
    public void sort() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        em.persist(new Member(null,100));
        em.persist(new Member("member5",100));
        em.persist(new Member("member6",100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull  = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("membe6");
        assertThat(memberNull.getUsername()).isNull();

    }

    @Test
    public void paging() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults(); //count 쿼리가 나간다.

        assertThat(result.size()).isEqualTo(2);
        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

    @Test
    public void aggregation(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(40);

    }

    /**
     * 팀의 이름과 각팀의 평균 연령을 구해라
     */
    @Test
    public void group() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team) //member에있는 팀과 팀을 조인
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
    }

    /**
     * 팀 A에 소속된 모든 회원
     */
    @Test
    public void join() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1","member2");
    }

    @Test
    public void theta_join(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        //원래는 이런경우(연관관계없는경우) 외부조인 할 수가 없다.. , but on을 사용해서 가능해짐 -> join_on_no_relation()
        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA","teamB");
    }

    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회 => outer 조인
     * JPQL : select m, t from Member m left join m.team t on t.name = 'teamA'
     */

    @Test
    public void join_on_filtering() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();

        for(Tuple tuple : result){
            System.out.println("tuple "+tuple);
        }
    }

    @Test
    public void join_on_no_relation(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        //원래는 이런경우(연관관계없는경우) 외부조인 할 수가 없다.. , but on을 사용해서 가능해짐
        List<Tuple> result = queryFactory
                .select(member,team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for(Tuple tuple : result){
            System.out.println("tuple "+tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;


    @Test
    public void fetchJoinNo(){
        em.flush();
        em.clear();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//        Member findMember = queryFactory
//                .selectFrom(QMember.member)
//                .where(QMember.member.username.eq("member1"))
//                .fetchOne(); //member만 조회

        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .join(QMember.member.team, team)
                .where(QMember.member.username.eq("member1"))
                .fetchOne(); //fetch join적용안하고 lazy라서 team을 로딩안함.

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("패치 조인 미적용").isFalse();
    }

    @Test
    public void fetchJoinUse(){
        em.flush();
        em.clear();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .join(member.team, team).fetchJoin()
                .where(QMember.member.username.eq("member1"))
                .fetchOne(); //member만 조회

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("패치 조인 적용").isTrue();
    }

    /**
     * 나이가 가장 많은 회원 조회
     */
    @Test
    public void subQuery(){
        QMember memberSub = new QMember("memberSub"); // 앨리어스가 중복되면 안됨

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(10,10,10,10);
    }

    @Test
    public void selectSubQuery(){
        QMember memberSub  = new QMember("memberSub");
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<Tuple> result = queryFactory.select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for(Tuple tuple : result){
            System.out.println("tuple : "+tuple);
        }
    }

    //JPQL이나 QueryDsl 모두 from절엔 서브쿼리가안된다.. select랑 where절만 가능
    //해결법
    /*
    * 1. 서브쿼리를 join으로 변경한다.
    * 2. 애플리케이션에서 쿼리를 2번 분리해서 실행
    * 3. native sql 사용
    * */

    @Test
    public void basicCase() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for(String s : result){
            System.out.println("s = "+s);
        }

    }

    @Test
    public void complexCase() { //사실 이런 문제들은 프로그래밍적으로 해결하는 것이 좋다
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for(String s : result){
            System.out.println("s = "+s);
        }

    }

    @Test
    public void constant(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for(Tuple tuple : result){
            System.out.println("tuple : "+tuple);
        }
    }

    @Test
    public void concat() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .fetch();
        for(String s : result){
            System.out.println("s = "+s);
        }
    }

    //프로젝션 대상이 하나면 결과로 타입을 명확하게 지정가능
    //대상이 여러개면 Tuple 이나 DTO로 지정해야한다.

    @Test
    public void simpleProjection() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch(); //simple projection

        List<Tuple> result2 = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for(Tuple tuple : result2){
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
        }
    }

    @Test
    public void findDtoByJPQL() {
        List<MemberDtoTwo> result = em.createQuery("select new study.datajpa.dto.MemberDtoTwo(m.username,m.age) from Member m", MemberDtoTwo.class)
                .getResultList();
        for(MemberDtoTwo mem : result){
            System.out.println("memberDto = "+mem);
        }
    }

    @Test
    public void findDtoBySetter(){ // 기본생성자로 생성 후 setter를 통해 주입
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<MemberDtoTwo> result = queryFactory
                .select(Projections.bean(MemberDtoTwo.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch(); //기본생성자 필요

        for(MemberDtoTwo memberDtoTwo : result){
            System.out.println("member " + memberDtoTwo);
        }

    }


    @Test
    public void findDtoByFields(){ //필드에 그대로 떄려박음
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<MemberDtoTwo> result = queryFactory
                .select(Projections.fields(MemberDtoTwo.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for(MemberDtoTwo memberDtoTwo : result){
            System.out.println("member " + memberDtoTwo);
        }

    }

    @Test
    public void findDtoByConstructor(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<MemberDtoTwo> result = queryFactory
                .select(Projections.constructor(MemberDtoTwo.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for(MemberDtoTwo memberDtoTwo : result){
            System.out.println("member " + memberDtoTwo);
        }

    }

    @Test
    public void findUserDto() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        member.age))
                .from(member)
                .fetch();

        for(UserDto userDto : result){
            System.out.println("userDto : "+userDto);
        }
    }

    @Test
    public void findDtoByQueryProjection() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<MemberDtoTwo> result = queryFactory
                .select(new QMemberDtoTwo(member.username, member.age))
                .from(member)
                .fetch();

        for(MemberDtoTwo memberDtoTwo : result){
            System.out.println("memberDto = "+memberDtoTwo);
        }
    }

    @Test
    public void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam,ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        BooleanBuilder builder = new BooleanBuilder(); //생성자안에 초기값 넣을 수 있다.
        if(usernameCond != null){
            builder.and(member.username.eq(usernameCond));
        }
        if(ageCond != null){
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Test
    public void dynamicQuery() {
        String usernameParam = "member1";
        Integer ageParam = 10;
        
        List<Member> result = searchMember2(usernameParam,ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameParam, Integer ageParam) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory
                .selectFrom(member)
               // .where(usenameEq(usernameParam), ageEq(ageParam))
                .where(allEq(usernameParam,ageParam))
                .fetch();
    }

    private BooleanExpression allEq(String usernameParam, Integer ageParam) {
        return usenameEq(usernameParam).and(ageEq(ageParam));
    }

    private BooleanExpression ageEq(Integer ageParam) { // 이런 메소드는 재사용 가능!
        return ageParam != null ? member.age.eq(ageParam) : null; //null을반환하면 그냥 where에서 무시한다.
    }

    private BooleanExpression usenameEq(String usernameParam) {
        if(usernameParam == null){
            return null;
        }
        return member.username.eq(usernameParam);
    }

    @Test
    public void bulkUpdate() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.eq(10))
                .execute();
        assertThat(count).isEqualTo(4);
    }

    @Test
    public void bulkAdd() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();
    }

    @Test
    public void sqlFunction(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory
                .select(Expressions.stringTemplate(
                        "function('replace',{0},{1},{2})",
                        member.username, "member" , "M"))
                .from(member)
                .fetch();
    }

    @Test
    public void sqlFunction2(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory
                .select(member.username)
                .from(member)
                //.where(member.username.eq(Expressions.stringTemplate("function('lower',{0})",member.username)))
                .where(member.username.eq(member.username.lower())) //위에꺼랑 똑같다.
                .fetch();
    }
}
