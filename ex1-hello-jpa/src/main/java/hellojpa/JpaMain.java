package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin(); // jpa는 트랜잭션 안에서 실행되어야한다., JPA의 모든 데이터 변경은 트랜잭션 안에서 실행

        try{
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("HelloB");
//            em.persist(member); //저장
//            Member findMember = em.find(Member.class,1L);
//            System.out.println("findMember.id : "+findMember.getId());
//            System.out.println("findMember.name : "+findMember.getName());
            // 최초로 영속성 컨텍스트로 들어온 엔티티에 대해서 스냅샷을 해둔다.
            // 커밋할 때, 엔티티랑 스냅샷을 비교해서 바뀌면 디비에 반영
//            findMember.setName("HelloJPA1"); //update됨 -> 변경감지
//
//            //JPQL은 엔티티 객체를 대상으로 쿼리를 내보내고 SQL은 디비 테이블을 대상으로 쿼리내보냄
//            List<Member> result = em.createQuery("select m from Member m",Member.class)
//                    .setFirstResult(5)
//                    .setMaxResults(8)
//                    .getResultList(); //디비에 맞게 번역해서 쿼리가 나감
//
//            for(Member member : result){
//                System.out.println("member.name : "+member.getName());
//            }

//            //비영속
//            Member member = new Member();
//            member.setId(103L);
//            member.setName("HelloJPA102");
//            //영속
//            System.out.println("BEFORE");
//            em.persist(member);
//            System.out.println("AFTER");
//
//            //이때 select쿼리 안나감 왜냐면 영속성 컨텍스트에 1차캐시에 저장이 됨.
//            // 사실 1차캐시는 entityManager에서 관리하는데 보통 트랜잭션 단위로 사용
//            // 트랜잭션 끝나면 entityManager를 닫으면 영속성 컨텍스트가 없어짐
//            // 2차캐시는 전체에서 사용
//            Member findMemberTwo = em.find(Member.class,103L);
//
//            System.out.println("findMember.id = "+findMemberTwo.getId());
//            System.out.println("findMember.name = "+findMemberTwo.getName());
//
//            Member member1 = new Member(150L,"A");
//            Member member2 = new Member(160L,"B");
//
//            em.persist(member1);
//            em.persist(member2); // 이때 쿼리 안날아감.
//
//            em.remove(member1); // 삭제

            Member member = new Member();
            member.setUsername("AAA");

            //id가 generate.identity인 경우 테이블에 들어가야지 id를 알수 있어서 이런경우, 쿼리 바로날아간다.
            // 영속성 컨텍스트의 저장을 위해선 id값이 있어야하기 때문..
            //시퀀스전략에서는 시퀀스에서 id값을 가저온다
            em.persist(member);
           //쓰기 지연 커밋했을때 모든 쿼리가 날아간다.
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
