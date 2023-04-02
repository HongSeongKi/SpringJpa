package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom { //사용자 정의 레파지토리
    List<Member> findMemberCustom();
}
