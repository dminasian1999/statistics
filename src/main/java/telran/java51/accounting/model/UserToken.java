package telran.java51.accounting.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

@Getter
@Document(collection = "tokens")
public class UserToken {
	@Id
	String email;
	String token;
	LocalDateTime expirationDate;

	public UserToken(String email) {
		this.email = email;
		this.token = UUID.randomUUID().toString();
		this.expirationDate = LocalDateTime.now().plusHours(1);
	}
}
