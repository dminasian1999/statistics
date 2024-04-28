package telran.java51.accounting.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

@Getter
@Document(collection = "tokens")
public class UserToken {
	
	String login;
	@Id
	String token;
	LocalDateTime expirationDate;

	public UserToken(String login) {
		this.login = login;
		this.token = UUID.randomUUID().toString();
		this.expirationDate = LocalDateTime.now().plusHours(1);
	}
}
