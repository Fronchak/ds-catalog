package com.fronchak.dscatalog.api.mappers;

import static com.fronchak.dscatalog.mocks.RoleMocksFactory.mockRole;
import static com.fronchak.dscatalog.mocks.RoleMocksFactory.mockRoleSet;
import static com.fronchak.dscatalog.util.CustomizeAsserts.assertRoleDTO;
import static com.fronchak.dscatalog.util.CustomizeAsserts.assertRoleDTOSet;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.dscatalog.api.dtos.RoleDTO;
import com.fronchak.dscatalog.domain.entities.Role;

@ExtendWith(SpringExtension.class)
public class RoleMapperTest {
	
	private RoleMapper mapper;
	
	@BeforeEach
	void setUp() {
		mapper = new RoleMapper();
	}
	
	@Test
	public void convertEntityToDTOShouldConvertCorrectly() {
		Role entity = mockRole();
		RoleDTO result = mapper.convertEntityToDTO(entity);
		assertRoleDTO(result);
	}
	
	@Test
	public void convertEntitySetToDTOSetShouldConvertCorrectly() {
		Set<Role> entities = mockRoleSet();		
		Set<RoleDTO> result = mapper.convertEntitySetToDTOSet(entities);		
		assertRoleDTOSet(result);
	}
}
