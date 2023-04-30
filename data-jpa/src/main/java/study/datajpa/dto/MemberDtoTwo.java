package study.datajpa.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDtoTwo {

    private String username;
    private int age;

    @QueryProjection
    public MemberDtoTwo(String username, int age){
        this.username = username;
        this.age = age;
    }
}
