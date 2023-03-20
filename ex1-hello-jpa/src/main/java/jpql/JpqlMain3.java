package jpql;

import domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlMain3 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // jpa는 트랜잭션 안에서 실행되어야한다., JPA의 모든 데이터 변경은 트랜잭션 안에서 실행

        try {
            //패치 조인
            JpqlTeam team = new JpqlTeam();
            team.setName("팀A");
            em.persist(team);

            JpqlTeam teamB = new JpqlTeam();
            teamB.setName("팀B");
            em.persist(teamB);

            JpqlMember member1 = new JpqlMember();
            member1.setUsername("회원1");
            member1.setTeam(team);
            em.persist(member1);

            JpqlMember member2 = new JpqlMember();
            member2.setUsername("회원2");
            member2.setTeam(team);
            em.persist(member2);

            JpqlMember member3 = new JpqlMember();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select m from JpqlMember m";
            List<JpqlMember> result = em.createQuery(query, JpqlMember.class)
                    .getResultList();

            for(JpqlMember member : result){
                System.out.println("mem = "+member.getUsername() + ", "+ member.getTeam().getName());
                //회원1, 팀A(sql)
                //회원2, 팀A(1차캐시)
                //회원3, 팀B(SQL)

                //회원 100명 조회  -> N+1 => 회원 가저오는 1번 쿼리에 팀을 찾으려고 100(N)번돈다. => 패치조인으로 해결
            }
            //패치조인 -> 팀도 프록시가 아닌 실제데이터가 담긴다.
            query = "select m from JpqlMember m join fetch m.team";

            em.flush();
            em.clear();

            String query2 = "select t from JpqlTeam t join fetch t.members"; //만약에 패치조인안하고 그냥조인하면 팀에 대한 정보만 나온다.
            List<JpqlTeam> result2 = em.createQuery(query2, JpqlTeam.class).getResultList();

            for(JpqlTeam t : result2){
                System.out.println("team = "+ t.getName()+", "+team.getMembers().size());
                // 데이터가 맴버개수만큼 나온다. 팀A인 맴버가 2명이라 팀A가 두번나온다. 조심하기
                // Distinct 사용 , jpa에서 distinct는 sql의 distinct 역할에 같은 엔티티면 삭제해준다(추가기능)
            }

            query2 = "select ditinct t from JpqlTeam t join fetch t.members";
            //패치조인의 한계점
            //1. 패치조인은 별칭을 주면 안된다. -> 패치조인은 기본적으로 연관된 애들을 다끌고온다.,잘못동작할 수도 있어
            //별칭을 안주는것이 관례다.관례가 패치조인시에는 모든것을 가저오는것이라, 5개중에만 3개를 뽑아오겠다라고 하면
            //처음부터 3개를 뽑고 시작하는것이 맞다. join패치할때 쓴다.
            //2. 둘 이상의 컬렉션은 페치 조인 할 수 없다.
            //3. 컬렉션 페치 조인은 페이징하면안된다. -> 하이버네이트는 경고로그 남기고 메모리에서 페이징함, 즉 팀의 데이터를 다 가저오므로 운영에서 매우 위험함.
            // 일대일 다대일은 페이징 가능
            em.flush();
            em.clear();

            query2 = "select t from JpqlTeam t"; //JpqlTeam members에 batchsize를 준다. 그러면 배치사이즈 만큼 맴버를 가져온다. batchsize는 글로벌세팅으로 가저갈수있다.
            System.out.println("batchsize@@");
            List<JpqlTeam> result3 = em.createQuery(query2)
                                    .setFirstResult(0)
                                    .setMaxResults(2)
                    .getResultList();

            for(JpqlTeam t : result3){
                System.out.println("Team = "+t.getName() );
                for(JpqlMember mem : t.getMembers()){ //이떄 배치사이즈만큼 가져온다
                    System.out.println("-> member : "+mem);
                }
            }

            em.clear();
            em.flush();

            List<JpqlMember> resultList = em.createNamedQuery("JpqlMember.findByUserName", JpqlMember.class)
                    .setParameter("username", "회원1")
                    .getResultList();
            for(JpqlMember mem : resultList){
                System.out.println("mem : "+mem);
            }

            em.clear();
            em.flush();

            //벌크연산

            //벌크연산은 디비에 바로 넣어버린다. 이때 영속성 컨텍스트에 있는것도 모두 쿼리로 나간다.(flush)
            int count = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();
            // 벌크연산으로 넣은건 영속성 컨텍스트에 반영이 안되어있다. 그래서 한번 영속 컨텍스트 날려주기(초기화)
            em.clear();

            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
    }
}
