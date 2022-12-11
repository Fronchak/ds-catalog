package com.fronchak.dscatalog.api.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fronchak.dscatalog.api.dtos.RoleDTO;
import com.fronchak.dscatalog.domain.entities.Role;

@Service
public class RoleMapper {

	public RoleDTO convertEntityToDTO(Role entity) {
		RoleDTO dto = new RoleDTO();
		dto.setId(entity.getId());
		dto.setAuthority(entity.getAuthority());
		return dto;
	}
	
	public Set<RoleDTO> convertEntitySetToDTOSet(Set<Role> entities) {
		return entities
				.stream()
				.map(entity -> convertEntityToDTO(entity))
				.collect(Collectors.toSet());
	}
}
