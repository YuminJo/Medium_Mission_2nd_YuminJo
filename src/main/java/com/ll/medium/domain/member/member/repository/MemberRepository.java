package com.ll.medium.domain.member.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ll.medium.domain.member.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.isPaid = true WHERE m.username = :username")
    void setIsPaidTrueByUsername(String username);
}
