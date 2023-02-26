package domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 테이블마다 생성됨
@Inheritance(strategy = InheritanceType.JOINED)// 없으면 single_table이 기본값 => 테이블 하나에 다 합처진다.
@DiscriminatorColumn //Dtype 생성 name으로 dtype말고 다른 컬럼명으로 변경가능
public abstract class Item2 {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;
}
