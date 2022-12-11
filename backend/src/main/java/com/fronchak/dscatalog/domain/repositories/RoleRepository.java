package com.fronchak.dscatalog.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fronchak.dscatalog.domain.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {}
