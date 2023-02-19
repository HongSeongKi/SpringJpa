package domain;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Team {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team") //member3의 변수명, 연관관계의 주인을 설정할 때 사용, mapppedBy를 쓰면 양방향에서 연관관계의 주인이 될 수 없다., 외래키가 있는곳을 주인으로 정한다.
    private List<Member3> members = new ArrayList<>();
}
