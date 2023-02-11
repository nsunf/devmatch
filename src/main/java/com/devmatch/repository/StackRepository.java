package com.devmatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmatch.entity.Stack;

public interface StackRepository extends JpaRepository<Stack, Long> {
}
