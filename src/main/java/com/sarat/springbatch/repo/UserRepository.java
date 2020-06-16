package com.sarat.springbatch.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sarat.springbatch.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
