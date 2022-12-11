package com.fronchak.dscatalog.mocks;

import static com.fronchak.dscatalog.mocks.RoleMocksFactory.mockRoleDTOSet;
import static com.fronchak.dscatalog.mocks.RoleMocksFactory.mockRoleSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.fronchak.dscatalog.api.dtos.RoleDTO;
import com.fronchak.dscatalog.api.dtos.UserDTO;
import com.fronchak.dscatalog.api.dtos.UserInsertDTO;
import com.fronchak.dscatalog.domain.entities.Role;
import com.fronchak.dscatalog.domain.entities.User;

public class UserMocksFactory {

	public static User mockUser() {
		return mockUser(0);
	}
	
	public static User mockUser(int i) {
		User mock = new User();
		mock.setId(mockId(i));
		mock.setFirstName(mockFirstName(i));
		mock.setLastName(mockLastName(i));
		mock.setEmail(mockEmail(i));
		mock.setPassword(mockPassword(i));
		Set<Role> roles = mockRoleSet();
		roles.forEach(role -> mock.getRoles().add(role));
		return mock;
	}
	
	private static Long mockId(int i) {
		return i + 0L;
	}
	
	private static String mockFirstName(int i) {
		return "Mock firstName " + i;
	}
	
	private static String mockLastName(int i) {
		return "Mock lastName " + i;
	}
	
	private static String mockEmail(int i) {
		return "Mock email " + i;
	}
	
	private static String mockPassword(int i) {
		return "Mock password " + i;
	}
	
	public static UserDTO mockUserDTO() {
		return mockUserDTO(0);
	}
	
	public static UserDTO mockUserDTO(int i) {
		UserDTO mock = new UserDTO();
		mock.setId(mockId(i));
		mock.setFirstName(mockFirstName(i));
		mock.setLastName(mockLastName(i));
		mock.setEmail(mockEmail(i));
		Set<RoleDTO> roles = mockRoleDTOSet();
		roles.forEach(role -> mock.getRoles().add(role));
		return mock;
	}
	
	public static UserInsertDTO mockUserInsertDTO(int i) {
		UserInsertDTO mock = new UserInsertDTO();
		mock.setId(mockId(i));
		mock.setFirstName(mockFirstName(i));
		mock.setLastName(mockLastName(i));
		mock.setEmail(mockEmail(i));
		mock.setPassword(mockPassword(i));
		Set<RoleDTO> roles = mockRoleDTOSet();
		roles.forEach(role -> mock.getRoles().add(role));
		return mock;
	}
	
	public static Page<User> mockUserPage() {
		return new PageImpl<>(mockUserList());
	}

	public static List<User> mockUserList() {
		List<User> mockList = new ArrayList<>();
		mockList.add(mockUser(0));
		mockList.add(mockUser(1));
		return mockList;
	}
	
	public static Page<UserDTO> mockUserDTOPage() {
		return new PageImpl<>(mockUserDTOList());
	}
	 
	public static List<UserDTO> mockUserDTOList() {
		List<UserDTO> mockList = new ArrayList<>();
		mockList.add(mockUserDTO(0));
		mockList.add(mockUserDTO(1));
		return mockList;
	}
}
