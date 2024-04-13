package telran.java51.accounting.controller;

import java.util.Base64;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dto.RolesDto;
import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.dto.UserEditDto;
import telran.java51.accounting.dto.UserRegisterDto;
import telran.java51.accounting.service.UserAccountService;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserAccountController {

	final UserAccountService userAccountService;

	@PostMapping("/register")
	public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
		return userAccountService.register(userRegisterDto);
	}
	
	@PostMapping("/login")
	public UserDto login(@RequestHeader("Authorization") String token) {
		token = token.split(" ")[1];
		String credentials = new String(Base64.getDecoder().decode(token));
		return userAccountService.getUser(credentials.split(":")[0]);
	}
	
	@GetMapping("/user/{email}")
	public UserDto getUser(@PathVariable String email) {
		return userAccountService.getUser(email);
	}

	@DeleteMapping("/user/{email}")
	public UserDto removeUser(@PathVariable String email) {
		return userAccountService.removeUser(email);
	}

	@PutMapping("/user/{email}")
	public UserDto updateUser(@PathVariable String email, @RequestBody UserEditDto userEditDto) {
		return userAccountService.updateUser(email, userEditDto);
	}

	@PutMapping("/user/{email}/role/{role}")
	public RolesDto addRole(@PathVariable String email, @PathVariable String role) {
		return userAccountService.changeRolesList(email, role, true);
	}

	@DeleteMapping("/user/{email}/role/{role}")
	public RolesDto deleteRole(@PathVariable String email, @PathVariable String role) {
		return userAccountService.changeRolesList(email, role, false);
	}

}
