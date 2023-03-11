package domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Member3 extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "USERNAME")
    private String username;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="TEAM_ID")
    private Team team;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    @OneToMany(mappedBy = "member3")
    private List<MemberProduct> memberProducts = new ArrayList<>();

//    public void changeTeam(Team team){
//        this.team = team;
//        team.getMembers().add(this);
//    }
}
