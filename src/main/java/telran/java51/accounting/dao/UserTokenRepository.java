package telran.java51.accounting.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java51.accounting.model.UserToken;

public interface UserTokenRepository extends CrudRepository<UserToken, String> {

}
