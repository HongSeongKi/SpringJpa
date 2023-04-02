package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}") //이건 조회용으로만 쓴다. 가급적 사용자제
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @GetMapping("/members") // 호출시 localhost:8080/members?page=0&size=3&sort=id,desc&sort=username,desc
    public Page<MemberDto> list(@PageableDefault(size=5, sort="username") Pageable pageable){ //지역으로 설정
        Page<Member> all = memberRepository.findAll(pageable);
        Page<MemberDto> map = all.map(MemberDto::new);
        return map;
    }



//    @PostConstruct
//    public void init(){
//        //memberRepository.save(new Member("userA"));
//        for(int i = 0;i < 100;i++){
//            memberRepository.save(new Member("user"+i,i));
//        }
//    }
}
