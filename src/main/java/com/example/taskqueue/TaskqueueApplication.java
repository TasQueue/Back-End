package com.example.taskqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaskqueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskqueueApplication.class, args);
	}

}
