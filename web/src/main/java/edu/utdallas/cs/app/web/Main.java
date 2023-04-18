package edu.utdallas.cs.app.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "edu.utdallas.cs.app")
@EntityScan(basePackages = "edu.utdallas.cs.app")
@EnableJpaRepositories(basePackages = "edu.utdallas.cs.app")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
