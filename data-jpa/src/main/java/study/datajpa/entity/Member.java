package study.datajpa.entity;

import lombok.*;

import javax.annotation.processing.Generated;
import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","username","age"})
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member extends JpaBaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

//    protected Member(){ // 하이버네이트가 프록시를 만드는데 private으로 하면 다막힐수 있어서 protected로 열어둔다.
//
//    }

    public Member(String username, int age){
        this.username = username;
        this.age = age;
    }

    public Member(String username) {
        this(username,0);
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null)
            changeTeam(team);
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
