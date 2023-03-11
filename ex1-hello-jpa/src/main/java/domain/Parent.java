package domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Child> childList = new ArrayList<>();
    // cascade랑 orphanRemoval을 다 켜두면 부모를 통해서 자식의 생명주기를 관리가능
    // persist나 remove든 부모만 하면된다. ,dao나 repository가 없어도 된다.
    public void addChild(Child child){
        childList.add(child);
        child.setParent(this);
    }
}
