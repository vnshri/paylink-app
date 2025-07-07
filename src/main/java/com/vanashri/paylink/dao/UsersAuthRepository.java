package com.vanashri.paylink.dao;

import com.vanashri.paylink.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersAuthRepository extends JpaRepository<UserEntity, Long> {

    public boolean existsByEmail(String email);

    public Optional<UserEntity> findByEmail(String email);
}
