package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.annotation.processing.Generated;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA는 기본생성자가 존재해야한다.
public class Item implements Persistable<String> {

    @Id
    private String id;

    @CreatedDate //EntityListeners가 있어야한다.
    private LocalDateTime createdDate;

    public Item(String id){
        this.id = id;
    }

    //spring data jpa에서 save를 날리면 새로운객체인지 판단한다.(isNew를 통해서 판단)
    //객체를 주면 널인지 아닌지에 대해서 판단하는데 객체 안에 키값이 널이 아닌상태로 save하면 persist가 아닌 merge를 호출한다.(save함수 보기)
    //merge를 호출하면 select를 한번해서 비효율적이다. 그래서 오버라이딩해서 새로 만들어준다.
    //Persistable을 implements하고 isNew를 구현
    @Override
    public boolean isNew() { //새로운 객체인지 판단
        return createdDate == null;
    }
}
