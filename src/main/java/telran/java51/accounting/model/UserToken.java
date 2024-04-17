package telran.java51.accounting.model;

import java.util.Date;
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
	Date expirationDate;

	public UserToken(String email) {
		this.email = email;
		this.token = UUID.randomUUID().toString();
		this.expirationDate = new Date(System.currentTimeMillis()+1000*60*3);
	}
	
	
}
