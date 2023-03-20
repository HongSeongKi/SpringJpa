package jpql;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.List;

public class JpqlJpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // jpa는 트랜잭션 안에서 실행되어야한다., JPA의 모든 데이터 변경은 트랜잭션 안에서 실행

        try{
            JpqlMember member = new JpqlMember();
            member.setUsername("member1");
            em.persist(member);

            TypedQuery<JpqlMember> query = em.createQuery("select m from JpqlMember m",JpqlMember.class); //반환타입이 Member로 확실하므로 TypedQuery사용
            query.getResultList(); // List<JpqlMember> 로 반환 ,결과가 없으면 빈리스트
            //query.getSingleResult(); //하나만 반환될때, 결과가 없거나 둘 이상이면 exception 반환
            //그래서 spring data jpa에선 결과가 없으면 exception날리거나 아니면 optional로 반환

            Query query2 = em.createQuery("select m.username, m.age from JpqlMember m"); //이런 경우는 반환타입이 2개라 하나로 확정불가능
            List resultList = query2.getResultList();
            Object o = resultList.get(0); //타입을 몰라서 object로 돌려준다.
            Object[] result = (Object[]) o;
            System.out.println("username : "+result[0]);
            System.out.println("age : "+result[1]);
            //아니면 List<Object[]> resultList로 해서 받아오기., 혹은 username,age를 가지고있는 클래스를 만들어서 반환

            em.createQuery("select new jpql.MemberDTO(m.username,m.age) from Member m",MemberDTO.class).getResultList();


            TypedQuery<JpqlMember> query3 = em.createQuery("select m from JpqlMember m where m.username = :username",JpqlMember.class);
            query3.setParameter("username","member1");
            JpqlMember singleResult = query3.getSingleResult();
            System.out.println("singleResult = " + singleResult);
            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();

        }
    }
}
