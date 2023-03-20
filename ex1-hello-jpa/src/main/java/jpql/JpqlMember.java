package jpql;

import domain.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(
        name = "JpqlMember.findByUserName",
        query = "select m from JpqlMember m where m.username = :username "
)
public class JpqlMember {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private JpqlTeam team;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    public void changeTeam(JpqlTeam team){
        this.team = team;
        team.getMembers().add(this);
    }

    @Override
    public String toString() {
        return "JpqlMember{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
