package com.fronchak.dscatalog.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fronchak.dscatalog.domain.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {}
