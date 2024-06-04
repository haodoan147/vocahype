package com.vocahype.repository;

import com.vocahype.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph("graph.user.role")
    Optional<User> findFirstById(String id);
}
