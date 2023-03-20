package jpql;

import domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlJpaMain2 {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // jpa는 트랜잭션 안에서 실행되어야한다., JPA의 모든 데이터 변경은 트랜잭션 안에서 실행

        try{

//            for(int i = 0;i<100;i++){
//                JpqlMember member = new JpqlMember();
//                member.setUsername("member" + i);
//                member.setAge(i);
//                em.persist(member);
//            }
//
//            em.flush();
//            em.clear();
//
//            List<JpqlMember> result = em.createQuery("select m from JpqlMember m order by m.age desc", JpqlMember.class)
//                    .setFirstResult(0)
//                    .setMaxResults(10)
//                    .getResultList();
//            System.out.println("result = "+result.size());
//
//            for(JpqlMember mem : result){
//                System.out.println("mem : " + mem);
//            }

            JpqlTeam team = new JpqlTeam();
            team.setName("teamA");
            em.persist(team);

            JpqlMember member1 = new JpqlMember();
            member1.setUsername("member");
            member1.setAge(10);
            member1.setTeam(team);
            member1.setType(MemberType.ADMIN);
            em.persist(member1);

            em.flush();
            em.clear();

            String query = "select m from JpqlMember m inner join m.team t";
            List<JpqlMember> result = em.createQuery(query,JpqlMember.class).getResultList();

            String query2 = "select m.username, 'HELLO', TRUE from JpqlMember m " +
                    "where m.type = jpql.MemberType.ADMIN"; //enum은 패키지까지 다적어야한다.
            //setParameter로 써서 패키로 안넣게 해도된다.
            List<Object[]> result2 = em.createQuery(query2).getResultList();

            for(Object[] objects : result2){
                System.out.println("objects = "+objects[0]);
                System.out.println("objects = "+objects[1]);
                System.out.println("objects = "+objects[2]);
            }

            String query3 = "select " +
                    "case when m.age<=10 then '학생요금' " +
                    "when m.age>=60 then '경로요금' " +
                    "else '일반요금' " +
                    "end " +
                    "from JpqlMember m";
            List<String> result3 = em.createQuery(query3,String.class).getResultList();

            for(String s : result3){
                System.out.println("s = "+s);
            }


            tx.commit();
        }catch(Exception e){
            tx.rollback();
        }finally {
            em.close();
        }

    }
}

