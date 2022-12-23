package com.fronchak.dscatalog.api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fronchak.dscatalog.api.dtos.UserDTO;
import com.fronchak.dscatalog.api.dtos.UserInsertDTO;
import com.fronchak.dscatalog.api.dtos.UserUpdateDTO;
import com.fronchak.dscatalog.domain.exceptions.ResourceNotFoundException;
import com.fronchak.dscatalog.domain.services.UserService;
import com.fronchak.dscatalog.mocks.UserMocksFactory;

@WebMvcTest(UserController.class)
public class UserControllerTest {

	private Long VALID_ID = 1L;
	private Long INVALID_ID = 2L;
	private MediaType mediaType = MediaType.APPLICATION_JSON;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private UserService service;

	
	
	@Test
	public void findByIdShouldReturnDTOWhenIdExists() throws Exception {
		UserDTO dto = UserMocksFactory.mockUserDTO();
		when(service.findById(VALID_ID)).thenReturn(dto);
		
		ResultActions result = mockMvc.perform(get("/api/users/{id}", VALID_ID)
				.accept(mediaType));
		result.andExpect(status().isOk());
		assertUserDTO(result);
	}
	
	private void assertUserDTO(ResultActions result) throws Exception {
		result.andExpect(jsonPath("$.id").value(0L));
		result.andExpect(jsonPath("$.firstName").value("Mock firstName 0"));
		result.andExpect(jsonPath("$.lastName").value("Mock lastName 0"));
		result.andExpect(jsonPath("$.email").value("Mock email 0"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		when(service.findById(INVALID_ID)).thenThrow(ResourceNotFoundException.class);
		
		ResultActions result = mockMvc.perform(get("/api/users/{id}", INVALID_ID)
				.accept(mediaType));
		
		assertNotFound(result);
	}
	
	private void assertNotFound(ResultActions result) throws Exception {
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.status").value(404));
		result.andExpect(jsonPath("$.error").value("Resource not found"));
	}
	 
	@Test
	public void saveShouldReturnDTOAndCreated() throws Exception {
		UserDTO insertDTO = new UserDTO();
		UserDTO dto = UserMocksFactory.mockUserDTO();
		when(service.save(any())).thenReturn(dto);
		
		String jsonBody = objectMapper.writeValueAsString(insertDTO);
		
		ResultActions result = mockMvc.perform(post("/api/users")
				.content(jsonBody)
				.contentType(mediaType)
				.accept(mediaType));
		
		result.andExpect(status().isCreated());
		assertUserDTO(result);
	}
	
	@Test
	public void updateShouldReturnSucessAndDTOWhenIdExists() throws Exception {
		UserUpdateDTO insertDTO = new UserUpdateDTO();
		UserDTO dto = UserMocksFactory.mockUserDTO();
		when(service.update(any(UserUpdateDTO.class), eq(VALID_ID))).thenReturn(dto);
		
		String jsonBody = objectMapper.writeValueAsString(insertDTO);
		
		ResultActions result = mockMvc.perform(put("/api/users/{id}", VALID_ID)
				.content(jsonBody)
				.accept(mediaType)
				.contentType(mediaType));
		
		result.andExpect(status().isOk());
		assertUserDTO(result);
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		when(service.update(any(UserUpdateDTO.class), eq(INVALID_ID))).thenThrow(ResourceNotFoundException.class);
		UserDTO dto = new UserDTO();
	
		String jsonPath = objectMapper.writeValueAsString(dto);
		
		ResultActions result = mockMvc.perform(put("/api/users/{id}", INVALID_ID)
				.content(jsonPath)
				.accept(mediaType)
				.contentType(mediaType));
		
		assertNotFound(result);
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExist() throws Exception {
		doNothing().when(service).delete(VALID_ID);
		
		ResultActions result = mockMvc.perform(delete("/api/users/{id}", VALID_ID)
				.accept(mediaType));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		doThrow(ResourceNotFoundException.class).when(service).delete(INVALID_ID);
		ResultActions result = mockMvc.perform(delete("/api/users/{id}", INVALID_ID)
				.accept(mediaType));
		assertNotFound(result);
	}
	
	@Test
	public void findAllShouldReturnSuccessAndPage() throws Exception {
		Page<UserDTO> dtos = UserMocksFactory.mockUserDTOPage();
		when(service.findAllPaged(any())).thenReturn(dtos);
		
		ResultActions result = mockMvc.perform(get("/api/users")
				.accept(mediaType));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(0L));
		result.andExpect(jsonPath("$.content[0].firstName").value("Mock firstName 0"));
		result.andExpect(jsonPath("$.content[1].id").value(1L));
		result.andExpect(jsonPath("$.content[1].firstName").value("Mock firstName 1"));
	}
	
	@Test
	public void saveShouldReturnErrorWhenFirstNameIsEmpty() throws Exception {
		UserInsertDTO insertDTO = UserMocksFactory.mockUserInsertDTO(0);
		insertDTO.setFirstName(null);
		
		when(service.save(any())).thenReturn(new UserInsertDTO());
		
		String jsonPath = objectMapper.writeValueAsString(insertDTO);
		
		ResultActions result = mockMvc.perform(post("/api/users")
				.content(jsonPath)
				.accept(mediaType)
				.contentType(mediaType));
	
		result.andExpect(status().isOk());
	}
}
