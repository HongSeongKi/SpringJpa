package domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain5 {
    public static void main(String[] args) { //즉시로딩(EAGER) 지연로딩(LAZY) , 실무에서는 지연로딩을 가급적 사용해야한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin(); // jpa는 트랜잭션 안에서 실행되어야한다., JPA의 모든 데이터 변경은 트랜잭션 안에서 실행

        try {
//            Team team = new Team();
//            team.setName("teamA");
//            em.persist(team);
//
//            Member3 member1 = new Member3();
//            member1.setUsername("member1");
//            member1.setTeam(team);
//            em.persist(member1);
//
//            em.flush();
//            em.clear();
//
//            Member3 m = em.find(Member3.class,member1.getId());
//            System.out.println("m.team = "+m.getTeam().getClass());
//
//            System.out.println("=============");
//            m.getTeam().getName(); // 실제 사용하는 시점에 쿼리가 나감(지연로딩으로 한경우)
//            System.out.println("=============");
            //즉시로딩
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Team team2 = new Team();
            team2.setName("teamB");
            em.persist(team2);

            Member3 member1 = new Member3();
            member1.setUsername("member1");
            member1.setTeam(team);
            em.persist(member1);

            Member3 member2 = new Member3();
            member2.setUsername("member2");
            member2.setTeam(team2);
            em.persist(member2);

            em.flush();
            em.clear();

            System.out.println("--------------------------------------");
            //List<Member3> members = em.createQuery("select m from Member3 m", Member3.class).getResultList(); // 즉시로딩에서 N+1문제해결
            List<Member3> members = em.createQuery("select m from Member3 m join fetch m.team",Member3.class).getResultList(); //패치 조인으로 N+1해결
            System.out.println("--------------------------------------");
            tx.commit();

        }catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
        }
    }
}
