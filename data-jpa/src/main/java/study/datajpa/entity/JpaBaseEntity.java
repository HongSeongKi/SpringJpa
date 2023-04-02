package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 속성을 다른데서 사용하고 싶을때 사용.
public class JpaBaseEntity { //등록일 수정일에 대해서 자동화 할 수 있다. 순수 JPA로 이러한 문제(Auditing) 해결

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updateDate = now;
    }

    @PreUpdate()
    public void preUpdate(){
        updateDate = LocalDateTime.now();
    }
}
