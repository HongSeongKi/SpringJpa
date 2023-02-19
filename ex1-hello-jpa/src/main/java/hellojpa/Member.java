package hellojpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@SequenceGenerator(name = "member_seq_generator",
        sequenceName = "member_seq", //매핑할 디비 시퀀스 이름
        initialValue = 1, allocationSize = 1) //시퀀스에서 한번 가저올때 allocation개수만큼 가저온다
public class Member {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // 디비가 알아서 만들어준다. mysql autoincrement 기능
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "member_seq_generator") //시퀀스를 만든다. 시퀀스에서 값을 가저온다.
    private Long id;

    @Column(name = "name") //DB엔 컬럼명 name으로 저장
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING) //디비엔 enum 타입이 없다.
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP) //날짜 시간
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob //큰 컨텐츠를 넣고 싶을때
    private String description;

    @Transient //얘는 디비에 만들지 않고 메모리에서만 쓴다.
    private int temp;


}
