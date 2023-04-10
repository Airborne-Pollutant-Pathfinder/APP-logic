package edu.utdallas.cs.app.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "edu.utdallas.cs.app")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
