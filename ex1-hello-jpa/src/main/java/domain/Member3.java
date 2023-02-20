package domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Member3 {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "USERNAME")
    private String username;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="TEAM_ID")
    private Team team;

//    public void changeTeam(Team team){
//        this.team = team;
//        team.getMembers().add(this);
//    }
}
