package com.pccw.springboot.persistence;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pccw.springboot.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	
	 @Query("SELECT u FROM User u where u.emailId = :emailId") 
	 Optional<User> findUserByEmail(@Param("emailId") String emaiId);

	
	
}
