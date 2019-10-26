package com.ciemiorek.ScooterSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:message.properties")
@ComponentScan(basePackages = {
        "com.ciemiorek.ScooterSystem.controller",
        "com.ciemiorek.ScooterSystem.common",
        "com.ciemiorek.ScooterSystem.model",
        "com.ciemiorek.ScooterSystem.repository"

})
public class ScooterSystemApplication {
    public static void main(String[] args) {
    	SpringApplication.run(ScooterSystemApplication.class, args);
    }

}
