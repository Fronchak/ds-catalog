package com.fronchak.dscatalog.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.fronchak.dscatalog.api.dtos.RoleDTO;
import com.fronchak.dscatalog.api.dtos.UserDTO;

public class CustomizeAsserts {

	public static void assertRoleDTO(RoleDTO result) {
		assertEquals(0L, result.getId());
		assertEquals("Mock authority 0", result.getAuthority());
	}
	
	public static void assertRoleDTO_1(RoleDTO result) {
		assertEquals(1L, result.getId());
		assertEquals("Mock authority 1", result.getAuthority());
	}
	
	public static void assertRoleDTOSet(Set<RoleDTO> resultSet) {
		RoleDTO result = resultSet.stream().filter(role -> role.getId().equals(0L)).findFirst().get();
		assertRoleDTO(result);
		result = resultSet.stream().filter(role -> role.getId().equals(1L)).findFirst().get();
		assertRoleDTO_1(result);
	}
	
	public static void assertUserDTO(UserDTO expected) {
		assertEquals(0L, expected.getId());
		assertEquals("Mock firstName 0", expected.getFirstName());
		assertEquals("Mock lastName 0", expected.getLastName());
		assertEquals("Mock email 0", expected.getEmail());
		assertRoleDTOSet(expected.getRoles());
	}
	
	public static void assertUserDTO_1(UserDTO expected) {
		assertEquals(1L, expected.getId());
		assertEquals("Mock firstName 1", expected.getFirstName());
		assertEquals("Mock lastName 1", expected.getLastName());
		assertEquals("Mock email 1", expected.getEmail());
		assertRoleDTOSet(expected.getRoles());
	}
	
	public static void assertUserDTOPage(Page<UserDTO> page) {
		List<UserDTO> list = page.getContent();
		UserDTO result = list.get(0);
		assertUserDTO(result);
		result = list.get(1);
		assertUserDTO_1(result);
	}

}
