package study.datajpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.QMember;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class DataJpaApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {
		Member member = new Member("A");
		em.persist(member);

		JPAQueryFactory query = new JPAQueryFactory(em);
		QMember qMember = QMember.member;

		Member member1 = query.selectFrom(qMember).fetchOne();

		Assertions.assertEquals(member.getId(),member1.getId());


	}

}
