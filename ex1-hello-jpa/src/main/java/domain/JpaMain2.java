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

            Team team2 = new Team();
            team2.setName("TeamB");
            em.persist(team2);

            Member3 member3 = new Member3();
            member3.setUsername("AAA");
            member3.setTeam(team); //연관관계의 주인인 member에서 team을 넣어준다. ,team에다가 맴버넣어도 포렌키에 값이 저장이 안된다.
            em.persist(member3);

            Member3 member31 = new Member3();
            member31.setUsername("BBB");
            member31.setTeam(team);
            em.persist(member31);


            Member3 member32 = em.find(Member3.class,member3.getId());
            Team memberTeam = member32.getTeam();
            System.out.println("team name : "+ memberTeam.getName());

            //단방향에서 team 아이디로 member가져오기 -> jpql사용
            String jpql = "select m from Member3 m join m.team t where t.id = :teamId";
            List<Member3> resultList = em.createQuery(jpql,Member3.class).setParameter("teamId",team.getId()).getResultList();

            for(Member3 mem : resultList){
                System.out.println("mem_name : "+mem.getUsername() );
            }
           // List<Member> memberList = em.find(Member.class,)

//            em.flush(); // 쿼리날아감
//            em.clear(); // 영속성 컨텍스트 날림
            //em.flush랑 clear안해주면 쿼리가 안날아가는데 캐시에는 team엔티티 쪽에 member정보가 없다.
            //그래서 문제가 될 수 있다. 따라서 그냥 team에도 set을 통해서 값을 넣어주자
            //Team findTeam = em.find(Team.class,team.getId());
            //List<Member3> members = findTeam.getMembers();
//            for(Member3 member : members){
//                System.out.println("m = "+member.getUsername());
//            }

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