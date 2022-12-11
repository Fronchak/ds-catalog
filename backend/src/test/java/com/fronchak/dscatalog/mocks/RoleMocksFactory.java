package com.fronchak.dscatalog.mocks;

import java.util.HashSet;
import java.util.Set;

import com.fronchak.dscatalog.api.dtos.RoleDTO;
import com.fronchak.dscatalog.domain.entities.Role;

public class RoleMocksFactory {

	public static Role mockRole() {
		return mockRole(0);
	}
	
	public static Role mockRole(int i) {
		Role mock = new Role();
		mock.setId(mockId(i));
		mock.setAuthority(mockAuthority(i));
		return mock;
	}
	
	private static Long mockId(int i) {
		return i + 0L;
	}
	
	private static String mockAuthority(int i) {
		return "Mock authority " + i;
	}
	
	public static Set<Role> mockRoleSet() {
		Set<Role> set = new HashSet<>();
		set.add(mockRole(0));
		set.add(mockRole(1));
		return set;
	}
	
	public static RoleDTO mockRoleDTO() {
		return mockRoleDTO(0);
	}
	
	public static RoleDTO mockRoleDTO(int i) {
		RoleDTO mock = new RoleDTO();
		mock.setId(mockId(i));
		mock.setAuthority(mockAuthority(i));
		return mock;
	}
	
	public static Set<RoleDTO> mockRoleDTOSet() {
		Set<RoleDTO> set = new HashSet<>();
		set.add(mockRoleDTO(0));
		set.add(mockRoleDTO(1));
		return set;
	}
}
