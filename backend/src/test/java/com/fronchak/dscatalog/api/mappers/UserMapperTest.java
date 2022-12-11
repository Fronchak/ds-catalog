package com.fronchak.dscatalog.api.mappers;

import static com.fronchak.dscatalog.mocks.RoleMocksFactory.mockRoleDTOSet;
import static com.fronchak.dscatalog.mocks.UserMocksFactory.mockUser;
import static com.fronchak.dscatalog.mocks.UserMocksFactory.mockUserDTO;
import static com.fronchak.dscatalog.util.CustomizeAsserts.assertUserDTO;
import static com.fronchak.dscatalog.util.CustomizeAsserts.assertUserDTOPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.dscatalog.api.dtos.RoleDTO;
import com.fronchak.dscatalog.api.dtos.UserDTO;
import com.fronchak.dscatalog.domain.entities.User;
import com.fronchak.dscatalog.mocks.UserMocksFactory;

@ExtendWith(SpringExtension.class)
public class UserMapperTest {
	
	@InjectMocks
	private UserMapper mapper;
	
	@Mock
	private RoleMapper roleMapper;
	
	@Test
	public void convertEntityToDTOShouldConvertCorrectly() {
		User entity = mockUser();
		Set<RoleDTO> roles = mockRoleDTOSet();
		when(roleMapper.convertEntitySetToDTOSet(entity.getRoles())).thenReturn(roles);
		
		UserDTO result = mapper.convertEntityToDTO(entity);
		assertUserDTO(result);
	}

	@Test
	public void copyDTOToEntityShouldCopyValuesCorrectly() {
		UserDTO dto = mockUserDTO(1);
		User entity = new User();
		
		mapper.copyDTOToEntity(dto, entity);
		
		assertEquals(dto.getFirstName(), entity.getFirstName());
		assertEquals(dto.getLastName(), entity.getLastName());
		assertEquals(dto.getEmail(), entity.getEmail());
	}
	
	@Test
	public void convertEntityPageToDTOPageShouldConvertCorrectly() {
		Page<User> entities = UserMocksFactory.mockUserPage();
		Set<RoleDTO> roles = mockRoleDTOSet();
		when(roleMapper.convertEntitySetToDTOSet(anySet())).thenReturn(roles);
		
		Page<UserDTO> page = mapper.convertEntityPageToDTOPage(entities);
		assertUserDTOPage(page);
	}
	

}
