package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); //쿼리메소드

    List<Member> findTop3HelloBy();

    @Query(name = "Member.findByUsername") //namedquery의 장점은 애플리케이션 로딩시점에 쿼리 문법에러를 알 수 있다.
    List<Member> findByUsername(@Param("username") String username);

    //복잡한 jpql을 바로 넣어서 해결가능 , 쿼리의 오타가 있어도 로딩시점에 문법에러를 알 수 있다.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //DTO로 반환하기
    @Query("select new study.datajpa.dto.MemberDto(m.id,m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //컬렉션 파라미터 바인딩딩
   @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

   //반환형 다르게 하기
    List<Member> findListMemberByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalMemberByUsername(String username); //단건 Optional

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m") // 카운트 쿼리 분리가능, 조인을 할때 카운트 쿼리도 같이 조인해서 가저와서 성능 문제 발생가능
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) //update시에 넣어야함
    @Query("update Member m set m.age = m.age+1 where m.age>=:age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team") //jpql 패치조인
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"}) //패치조인해준다.
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph(); //이렇게 사용도 가능

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);



    //이러면 처음에 영속성컨텍스트에 저장될 때, 변경이 안된다고 생각해서 원본을 저장안한다.
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true") )
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE) //락 사용하기  https://isntyet.github.io/jpa/JPA-%EB%B9%84%EA%B4%80%EC%A0%81-%EC%9E%A0%EA%B8%88(Pessimistic-Lock)/
    List<Member> findLockByUsername(String username);

    //List<UsernameOnlyDto>로도 가능
    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

    <T> List<T> findProjectionsGenericByUsername(@Param("usernname") String username, Class<T> type);

}
