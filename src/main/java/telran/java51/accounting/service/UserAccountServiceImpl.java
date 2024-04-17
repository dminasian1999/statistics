package telran.java51.accounting.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.accounting.dao.PasswordResetTokenRepository;
import telran.java51.accounting.dao.UserAccountRepository;
import telran.java51.accounting.dto.RolesDto;
import telran.java51.accounting.dto.UserDto;
import telran.java51.accounting.dto.UserEditDto;
import telran.java51.accounting.dto.UserRegisterDto;
import telran.java51.accounting.dto.exceptions.InvalidEmailExeption;
import telran.java51.accounting.dto.exceptions.TokenExpiredExeption;
import telran.java51.accounting.dto.exceptions.UserExistsException;
import telran.java51.accounting.dto.exceptions.UserNotFoundException;
import telran.java51.accounting.model.PasswordResetToken;
import telran.java51.accounting.model.UserAccount;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {

	final PasswordResetTokenRepository passwordResetTokenRepository;
	final UserAccountRepository userAccountRepository;
	final ModelMapper modelMapper;
	final PasswordEncoder passwordEncoder;

	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		checkEmail(userRegisterDto.getEmail());
		if (userAccountRepository.existsById(userRegisterDto.getEmail())) {
			throw new UserExistsException();
		}
		userAccount.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	private void checkEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new InvalidEmailExeption();
		}

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
	public PasswordResetToken generateToken(String email) {
		UserAccount userAccount = userAccountRepository.findById(email).orElseThrow(UserNotFoundException::new);
		String token = generateRandomToken();
		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setUser(userAccount);
		resetToken.setToken(token);
		resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Токен действителен 1 час
		passwordResetTokenRepository.save(resetToken);
		return resetToken;
	}

	private String generateRandomToken() {
		SecureRandom random = new SecureRandom();
		int tokenLength = 32;
		byte[] tokenBytes = new byte[tokenLength];
		random.nextBytes(tokenBytes);
		StringBuilder tokenBuilder = new StringBuilder();
		for (byte b : tokenBytes) {
			tokenBuilder.append(Base64.getEncoder().encodeToString(new byte[] { b }));
		}
		String token = tokenBuilder.toString().replace("=", "").replace("+", "");
		return token;
	}

	@Override
	public void changePasswordByToken(String token, String newPassword) {
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findById(token)
				.orElseThrow(UserNotFoundException::new);
		if (LocalDateTime.now().isAfter(passwordResetToken.getExpiryDate())) {
			passwordResetTokenRepository.delete(passwordResetToken);
			throw new TokenExpiredExeption();
		}
		UserAccount userAccount = passwordResetToken.getUser();
		changePassword(userAccount.getEmail(), newPassword);
		return;
	}

}
