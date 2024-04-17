package telran.java51.accounting.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "passwordResetTokens")
public class PasswordResetToken {
	    @Id
	    String token;
	    UserAccount user;
	    LocalDateTime expiryDate;

}
