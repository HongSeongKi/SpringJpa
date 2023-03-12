package domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class JpaMain7 {
    public static void main(String[] args) { //영속성전이
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin(); // jpa는 트랜잭션 안에서 실행되어야한다., JPA의 모든 데이터 변경은 트랜잭션 안에서 실행

        try {
//            Member4 member4 = new Member4();
//            member4.setUsername("hello");
//            member4.setAddress(new Address("city","street","100"));
//            member4.setPeriod(new Period());
//            em.persist(member4);
            Address address = new Address("city","street","100");
            Member4 member1 = new Member4();
            member1.setUsername("member1");
            member1.setAddress(address);
            em.persist(member1);

            //임베디드 타입은 값타입으로 만들어야한다.
            //그래야지 member1을 수정했을때 member2가안바뀐다.
            //보통 그래서 setter를 만들지 않는다.
            //값 바꿀때는, 새로 new로 해서 객체를 만들도록 한다.
            Address copyAddress = new Address(address.getCity(),address.getStreet(),address.getZipcode());
            Member4 member2 = new Member4();
            member2.setUsername("member2");
            member2.setAddress(copyAddress);
            em.persist(member2);

            member1.getAddress().setCity("newCity");

            //값 타입 컬렉션

            Member4 member41 = new Member4();
            member41.setUsername("member41");
            member41.setAddress(new Address("city1","street","10000"));

            member41.getFavoriteFoods().add("치킨");
            member41.getFavoriteFoods().add("족발");
            member41.getFavoriteFoods().add("피자");

            member41.getAddressHistory().add(new Address("old1","street","100000"));
            member41.getAddressHistory().add(new Address("old2","street","100000"));

            //값타입 컬렉션들이 MEMBER4에 의존한다. 즉 라이프사이클이 의존해서 값타입들은 따로 persist해줄필요가 없다. cascade한거랑 비슷한 느낌.
            em.persist(member41);

            em.flush();
            em.clear();

            System.out.println("============== START ================");
            //여기서보면, MEMBER4만 조회해서 온다. 값 타입 컬렉션은 모두 지연로딩이다.
            Member4 findMember = em.find(Member4.class, member41.getId());

            //여기서 쿼리나간다.
            List<Address> addressHistory = findMember.getAddressHistory();
            for(Address address1 : addressHistory){
                System.out.println("address = "+address1.getCity());
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for(String favoriteFood : favoriteFoods){
                System.out.println("favoriteFood = "+favoriteFood);
            }

            //실제로 setter뺴고 이런식으로 값변경하기(갈아끼우기)
            findMember.setAddress(new Address("newCity",findMember.getAddress().getStreet(),"100000"));

            //값 타입 인스턴스 변경

            //치킨을 한식으로 바꾸기
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            findMember.getAddressHistory().remove(new Address("old1","street","100000")); //remove는 기본적으로 찾을때 equals로 동작
            findMember.getAddressHistory().add(new Address("newCity","street","100000")); //그리고 값 타입컬렉션을 삭제하면 모두지우고 모든데이터를 새로 다시 insert한다.
            //사실상 값타입 컬렉션 쓰면안된다.. => 그냥 일대다 관계로 풀어내기
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

}
