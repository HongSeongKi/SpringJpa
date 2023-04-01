package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

@Repository //없어도 상관없음
public interface TeamRepository extends JpaRepository<Team, Long> {

}
