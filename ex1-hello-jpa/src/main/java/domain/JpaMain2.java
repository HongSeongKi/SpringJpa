package domain;

import hellojpa.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain2 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin(); // jpa는 트랜잭션 안에서 실행되어야한다., JPA의 모든 데이터 변경은 트랜잭션 안에서 실행

        try{
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member3 member3 = new Member3();
            member3.setUsername("AAA");
            member3.setTeam(team); //연관관계의 주인인 member에서 team을 넣어준다. ,team에다가 맴버넣어도 포렌키에 값이 저장이 안된다.
            em.persist(member3);

            em.flush(); // 쿼리날아감
            em.clear(); // 영속성 컨텍스트 날림

            //em.flush랑 clear안해주면 쿼리가 안날아가는데 캐시에는 team엔티티 쪽에 member정보가 없다.
            //그래서 문제가 될 수 있다. 따라서 그냥 team에도 set을 통해서 값을 넣어주자
            Team findTeam = em.find(Team.class,team.getId());
            List<Member3> members = findTeam.getMembers();
            for(Member3 member : members){
                System.out.println("m = "+member.getUsername());
            }

//            Member3 findMember = em.find(Member3.class, member3.getId());
//            System.out.println("===========================");
//            List<Member3> members = findMember.getTeam().getMembers();
//
//            for(Member3 m : members){
//                System.out.println("m = "+m.getUsername());
//            }
            tx.commit();
        }catch(Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}