package ru.bokgosha.parking.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.bokgosha.parking.repository")
@EntityScan(basePackages = "ru.bokgosha.parking.model")
@ComponentScan(basePackages = "ru.bokgosha.parking")
public class AppConfig {}
