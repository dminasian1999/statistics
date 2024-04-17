package telran.java51.accounting.service;

import telran.java51.accounting.dto.RolesDto;
import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.dto.UserEditDto;
import telran.java51.accounting.dto.UserRegisterDto;

public interface UserAccountService {

	UserDto register(UserRegisterDto userRegisterDto);

	void recoveryPasswordLink(String email);

	void recoveryPassword(String token, String newPassword);

	UserDto removeUser(String email);

	UserDto updateUser(String email, UserEditDto userEditDto);

	RolesDto changeRolesList(String email, String role, boolean isAddRole);

	void changePassword(String email, String newPassword);

	UserDto getUser(String email);

}
