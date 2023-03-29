package com.devmatch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
	Page<Request> findByCustomerIdOrderByRegTimeDesc(Long customerId, Pageable pageable);
	Page<Request> findByProviderIdOrderByRegTimeDesc(Long providerId, Pageable pageable);
}
