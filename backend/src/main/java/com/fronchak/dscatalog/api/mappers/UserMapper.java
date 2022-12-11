package com.fronchak.dscatalog.api.mappers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fronchak.dscatalog.api.dtos.RoleDTO;
import com.fronchak.dscatalog.api.dtos.UserDTO;
import com.fronchak.dscatalog.domain.entities.User;

@Service
public class UserMapper {
	
	@Autowired
	private RoleMapper roleMapper;

	public UserDTO convertEntityToDTO(User entity) {
		UserDTO dto = new UserDTO();
		dto.setId(entity.getId());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setEmail(entity.getEmail());
		Set<RoleDTO> roles = roleMapper.convertEntitySetToDTOSet(entity.getRoles());
		roles.forEach(role -> dto.getRoles().add(role));
		return dto;
	}
	
	public void copyDTOToEntity(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
	}
	
	public Page<UserDTO> convertEntityPageToDTOPage(Page<User> entities) {
		return entities.map(entity -> convertEntityToDTO(entity));
	}
}
