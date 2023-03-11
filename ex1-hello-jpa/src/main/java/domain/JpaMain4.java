package domain;

import hellojpa.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain4 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin(); // jpa는 트랜잭션 안에서 실행되어야한다., JPA의 모든 데이터 변경은 트랜잭션 안에서 실행

        try {

            Member3 member3 = new Member3();
            member3.setUsername("AAA");

            em.persist(member3);

            em.flush();
            em.clear();

//
//            Member3 findMember = em.getReference(Member3.class, member3.getId()); //이때는 쿼리안날아가고 직접 사용할떄(ex print시) 실제 쿼리가 날아간다., 이떄는 프록시 객체 생성
//            System.out.println("findMember id : "+findMember.getId());
//            System.out.println("findMember username : "+findMember.getUsername());
// --------------------------------------------------
//            System.out.println("BEFORE");
//            Member3 findMember3 = em.find(Member3.class, member3.getId()); //데이터베이스를 통해 실제 엔티티 객체 조회
//            System.out.println("findMember3 = "+findMember3.getClass());
//            Member3 member2 = em.getReference(Member3.class, member3.getId());
//            System.out.println("member2 = "+member2.getClass());
//            System.out.println("a == a : "+(findMember3 == member2) );
            // findMember3랑 member2가 같은형이나온다.
            // 1. 원래는 member2는 프록시로 만들어지지만, 이미 findMemver3로인해서 영속성컨텍스트에 실제 존재하는 Member3가있기에 프록시 만드는게 의미가 없다.
            // 2. 한 영속성컨텍스트에서 가저오면 무조건 같아야한다.(JPA는 한트랙잭션 안에서 항상 같은 값을 가저온다) => 진짜 이유
            // 3. 그래서 reference하고 find해도 둘다 프록시로 나온다(2번이유)
            //--------------------------------------------------
            Member3 refMember = em.getReference(Member3.class, member3.getId());
            em.detach(refMember);
            System.out.println("refMember : "+refMember.getUsername());
            //refMember에서 쿼리를 날릴때 영속성 컨텍스트를 통해서 날리는데 detach를 통해서 영속성 컨텍스트에서 관리하지 않아서 오류 발생
        }catch(Exception e){
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
    }

    private static void printMember(Member3 member){ //이때는 팀까지 가저오면 손해다.
        System.out.println("member: "+member.getUsername());

    }

    private static void printMemberAndTeam(Member3 member3){
        String username = member3.getUsername();
        System.out.println("username : "+username);

        Team team = member3.getTeam();
        System.out.println("team = "+team.getName());
    }
}
