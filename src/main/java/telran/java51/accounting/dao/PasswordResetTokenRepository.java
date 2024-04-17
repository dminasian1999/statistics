package telran.java51.accounting.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java51.accounting.model.PasswordResetToken;


public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, String>{

}
