package study.datajpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;

    protected Member(){ // 하이버네이트가 프록시를 만드는데 private으로 하면 다막힐수 있어서 protected로 열어둔다.

    }

    public Member(String username) {
        this.username = username;
    }
}
