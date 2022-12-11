package com.fronchak.dscatalog.domain.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fronchak.dscatalog.api.dtos.RoleDTO;
import com.fronchak.dscatalog.api.dtos.UserDTO;
import com.fronchak.dscatalog.api.dtos.UserInsertDTO;
import com.fronchak.dscatalog.api.mappers.UserMapper;
import com.fronchak.dscatalog.domain.entities.User;
import com.fronchak.dscatalog.domain.exceptions.ResourceNotFoundException;
import com.fronchak.dscatalog.domain.repositories.RoleRepository;
import com.fronchak.dscatalog.domain.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserMapper mapper;
	
	@Transactional
	public UserDTO findById(Long id) {
		User entity = repository.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("User not found by ID: " + id));
		return mapper.convertEntityToDTO(entity);
	}
	
	@Transactional
	public UserDTO update(UserDTO dto, Long id) {
		try {
			User entity = repository.getReferenceById(id);
			copyDTOToEntity(dto, entity);
			entity = repository.save(entity);
			return mapper.convertEntityToDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("User not found by ID: " + id);
		}

	}
	
	private void copyDTOToEntity(UserDTO dto, User entity) {
		mapper.copyDTOToEntity(dto, entity);
		entity.getRoles().clear();
		for(RoleDTO roleDTO : dto.getRoles()) {
			entity.getRoles().add(roleRepository.getReferenceById(roleDTO.getId()));
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);	
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("User not found by ID: " + id);
		}
	}
	
	@Transactional
	public UserDTO save(UserInsertDTO insertDTO) {
		User entity = new User();
		copyDTOToEntity(insertDTO, entity);
		entity.setPassword(passwordEncoder.encode(insertDTO.getPassword()));
		entity = repository.save(entity);
		return mapper.convertEntityToDTO(entity);
	}
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> entities = repository.findAll(pageable);
		return mapper.convertEntityPageToDTOPage(entities);
	}
}
