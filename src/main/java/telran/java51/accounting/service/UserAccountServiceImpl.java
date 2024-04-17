package telran.java51.accounting.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dao.UserAccountRepository;
import telran.java51.accounting.dao.UserTokenRepository;
import telran.java51.accounting.dto.RolesDto;
import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.dto.UserEditDto;
import telran.java51.accounting.dto.UserRegisterDto;
import telran.java51.accounting.dto.exceptions.UserExistsException;
import telran.java51.accounting.dto.exceptions.UserNotFoundException;
import telran.java51.accounting.model.UserAccount;
import telran.java51.accounting.model.UserToken;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {

	final UserAccountRepository userAccountRepository;
	final UserTokenRepository userTokenRepository;
	final ModelMapper modelMapper;
	final PasswordEncoder passwordEncoder;
	final JavaMailSender javaMailSender;

	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		if (userAccountRepository.existsById(userRegisterDto.getEmail())) {
			throw new UserExistsException();
		}
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		userAccount.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto getUser(String email) {
		UserAccount userAccount = userAccountRepository.findById(email).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto removeUser(String email) {
		UserAccount userAccount = userAccountRepository.findById(email).orElseThrow(UserNotFoundException::new);
		userAccountRepository.delete(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto updateUser(String email, UserEditDto userEditDto) {
		UserAccount userAccount = userAccountRepository.findById(email).orElseThrow(UserNotFoundException::new);
		if (userEditDto.getFirstName() != null) {
			userAccount.setFirstName(userEditDto.getFirstName());
		}
		if (userEditDto.getLastName() != null) {
			userAccount.setLastName(userEditDto.getLastName());
		}
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public RolesDto changeRolesList(String email, String role, boolean isAddRole) {
		UserAccount userAccount = userAccountRepository.findById(email).orElseThrow(UserNotFoundException::new);
		boolean res;
		if (isAddRole) {
			res = userAccount.addRole(role);
		} else {
			res = userAccount.removeRole(role);
		}
		if (res) {
			userAccountRepository.save(userAccount);
		}
		return modelMapper.map(userAccount, RolesDto.class);
	}

	@Override
	public void changePassword(String email, String newPassword) {
		UserAccount userAccount = userAccountRepository.findById(email).orElseThrow(UserNotFoundException::new);
		userAccount.setPassword(passwordEncoder.encode(newPassword));
		userAccountRepository.save(userAccount);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!userAccountRepository.existsById("admin")) {
			String password = passwordEncoder.encode("admin");
			UserAccount userAccount = new UserAccount("admin", password, "", "");
			userAccount.addRole("ADMINISTRATOR");
			userAccountRepository.save(userAccount);
		}
	}

	@Override
	public void recoveryPasswordLink(String email) {
		userAccountRepository.findById(email).orElseThrow(UserNotFoundException::new);
		UserToken userToken = new UserToken(email);
		userTokenRepository.save(userToken);
		sendRecoveryEmail(email, userToken.getToken());

	}

	private void sendRecoveryEmail(String email, String token) {
		String recoveryLink = "https://finstats.herokuapp.com/account/recovery/"  + token;
		String emailContent = "Click the following link to reset your password: " + recoveryLink;

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Password recovery request");
		message.setText(emailContent);
		javaMailSender.send(message);

	}

	@Override
	public void recoveryPassword(String token, String newPassword) {
		UserToken userToken = userTokenRepository.findById(token)
				.orElseThrow(null);
		if (LocalDateTime.now().isAfter(userToken.getExpirationDate())) {
			userTokenRepository.delete(userToken);
			throw new TokenExpiredExeption();
		}
		changePassword(userToken.getEmail(), newPassword);
	}

}
