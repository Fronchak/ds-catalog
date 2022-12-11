package com.fronchak.dscatalog.domain.services;

import static com.fronchak.dscatalog.mocks.UserMocksFactory.mockUser;
import static com.fronchak.dscatalog.mocks.UserMocksFactory.mockUserDTO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.dscatalog.api.dtos.UserDTO;
import com.fronchak.dscatalog.api.dtos.UserInsertDTO;
import com.fronchak.dscatalog.api.dtos.UserUpdateDTO;
import com.fronchak.dscatalog.api.mappers.UserMapper;
import com.fronchak.dscatalog.domain.entities.User;
import com.fronchak.dscatalog.domain.exceptions.ResourceNotFoundException;
import com.fronchak.dscatalog.domain.repositories.UserRepository;
import com.fronchak.dscatalog.mocks.UserMocksFactory;
import com.fronchak.dscatalog.util.CustomizeAsserts;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService service;
	
	@Mock
	private UserRepository repository;
	
	@Mock
	private UserMapper mapper;
	
	private Long VALID_ID = 1L;
	private Long INVALID_ID = 2L;
	
	@Test
	public void findByIdShouldReturnDTOWhenIdExists() {
		User entity = mockUser();
		UserDTO dto = mockUserDTO();
		when(repository.findById(VALID_ID)).thenReturn(Optional.of(entity));
		when(mapper.convertEntityToDTO(entity)).thenReturn(dto);
		
		UserDTO result = service.findById(VALID_ID);
		
		CustomizeAsserts.assertUserDTO(result);
		verify(repository, times(1)).findById(VALID_ID);
		verify(mapper, times(1)).convertEntityToDTO(entity);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExpectionWhenIdDoesNotExist() {
		when(repository.findById(INVALID_ID)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> service.findById(INVALID_ID));
		verify(repository, times(1)).findById(INVALID_ID);
		verify(mapper, never()).convertEntityToDTO(any());
	}
	
	@Test
	public void updateShouldReturnDTOWhenIdExists() {
		UserUpdateDTO dto = UserMocksFactory.mockUserUpdateDTO();
		User entity = mockUser();
		User entityUpdated = mockUser(1);
		UserDTO dtoUpdated = mockUserDTO(1);
		when(repository.getReferenceById(VALID_ID)).thenReturn(entity);
		doNothing().when(mapper).copyDTOToEntity(dto, entity);
		when(repository.save(entity)).thenReturn(entityUpdated);
		when(mapper.convertEntityToDTO(entityUpdated)).thenReturn(dtoUpdated);
		
		UserDTO result = service.update(dto, VALID_ID);
		
		CustomizeAsserts.assertUserDTO_1(result);
		verify(repository, times(1)).save(entity);
		verify(mapper, times(1)).convertEntityToDTO(entityUpdated);
		verify(mapper, times(1)).copyDTOToEntity(dto, entity);
	}
	
	@Test
	void updateShouldThrowResourceNotFoundExpectionWhenIdDoesNotExist() {
		UserUpdateDTO dto = UserMocksFactory.mockUserUpdateDTO();
		when(repository.getReferenceById(INVALID_ID)).thenThrow(EntityNotFoundException.class);
		
		assertThrows(ResourceNotFoundException.class, () -> service.update(dto, INVALID_ID));
		verify(repository, never()).save(any());
		verify(mapper, never()).copyDTOToEntity(any(), any());
	}
	
	@Test
	void deleteShouldDeleteEntityWhenIdExists() {
		doNothing().when(repository).deleteById(VALID_ID);
		
		assertDoesNotThrow(() -> service.delete(VALID_ID));
		verify(repository, times(1)).deleteById(VALID_ID);
	}
	
	@Test
	void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(INVALID_ID);
		assertThrows(ResourceNotFoundException.class, () -> service.delete(INVALID_ID));
	}
	
	@Test
	void saveShouldSaveUser() {
		UserInsertDTO insertDTO = UserMocksFactory.mockUserInsertDTO(1);
		User entity = new User();
		User entitySaved = mockUser(0);
		UserDTO dtoSaved = mockUserDTO(0);
		doNothing().when(mapper).copyDTOToEntity(insertDTO, entity);
		when(repository.save(entity)).thenReturn(entitySaved);
		when(mapper.convertEntityToDTO(entitySaved)).thenReturn(dtoSaved);
		
		UserDTO result = service.save(insertDTO);
		
		CustomizeAsserts.assertUserDTO(result);
	}
	
	@Test
	void findAllShouldReturnPageOfDTO() {
		Pageable pageRequest = PageRequest.of(0, 5);
		Page<User> entities = UserMocksFactory.mockUserPage();
		Page<UserDTO> dtos = UserMocksFactory.mockUserDTOPage();
		
		when(repository.findAll(pageRequest)).thenReturn(entities);
		when(mapper.convertEntityPageToDTOPage(entities)).thenReturn(dtos);
		
		Page<UserDTO> page = service.findAllPaged(pageRequest);
		CustomizeAsserts.assertUserDTOPage(page);
	}
}
