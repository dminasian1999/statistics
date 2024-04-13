package telran.java51.accounting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Document(collection = "users")
public class UserAccount {
	@Id
	String email;
	@Setter
	String password;
	@Setter
	String firstName;
	@Setter
	String lastName;
	Set<Role> roles;
	
	public UserAccount() {
		roles = new HashSet<>();
		addRole("USER");
	}

	public UserAccount(String email, String password, String firstName, String lastName) {
		this();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public boolean addRole(String role) {
		return roles.add(Role.valueOf(role.toUpperCase()));
	}

	public boolean removeRole(String role) {
		return roles.remove(Role.valueOf(role.toUpperCase()));
	}

}
