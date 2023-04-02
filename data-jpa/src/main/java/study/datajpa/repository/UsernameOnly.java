package study.datajpa.repository;


import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {
    @Value("#{target.username + ' '+ target.age}") //이거쓰면 엔티티의 데이터를 다 가저오고 원하는 데이터를 이런식으로 찍어서 가저온다.
    String getUsername();
}
