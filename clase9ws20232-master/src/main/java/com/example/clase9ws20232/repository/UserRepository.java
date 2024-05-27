package com.example.clase9ws20232.repository;

import com.example.clase9ws20232.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
