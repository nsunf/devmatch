package com.devmatch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.MemberImg;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {
	Optional<MemberImg> findByMemberId(Long memberId);
}
