package jpql;

import domain.Address;

import javax.persistence.*;


@Entity
public class JpqlOrder {
    @Id
    @GeneratedValue
    private Long id;
    private int orderAmount;

    @Embedded
    private JpqlAddress address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private JpqlProduct product;
}
