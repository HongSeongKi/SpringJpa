package domain;

import lombok.Data;

import javax.persistence.*;

//다대다 관계는 풀어서 정리
@Entity
@Data
public class MemberProduct {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member3 member3;

    @ManyToOne
    @JoinColumn(name="PRODUCT_ID")
    private Product product;

    //엔티티로 따로빼야지 다른 값들을 넣을 수 있다.
    private int count;
    private int price;
}
