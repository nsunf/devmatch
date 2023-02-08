package com.devmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>  {
	public Member findByEmail(String email);
	public Member findBySsn(String ssn);
}
