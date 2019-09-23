package com.pccw.springboot.userservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages= {"com.pccw.springboot.persistence"})
@EntityScan(basePackages={"com.pccw.springboot.model"})
//@ComponentScan(basePackages= {"com.pccw.springboot.config","com.pccw.springboot.userservices"})

@SpringBootApplication
public class UserservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserservicesApplication.class, args);
	}

}
