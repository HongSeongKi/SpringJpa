package jpql;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class JpqlProduct {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private int price;
    private int stockAmount;
}
