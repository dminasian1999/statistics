package telran.java51.accounting.service;

import telran.java51.accounting.dto.RolesDto;
import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.dto.UserEditDto;
import telran.java51.accounting.dto.UserRegisterDto;

public interface UserAccountService {

	UserDto register(UserRegisterDto userRegisterDto);

	void recoveryPasswordLink(String login);

	void recoveryPassword(String token, String newPassword);

	UserDto removeUser(String login);

	UserDto updateUser(String login, UserEditDto userEditDto);

	RolesDto changeRolesList(String login, String role, boolean isAddRole);

	void changePassword(String login, String newPassword);

	UserDto getUser(String login);

}
