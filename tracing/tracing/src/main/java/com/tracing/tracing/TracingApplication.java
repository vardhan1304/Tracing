package com.tracing.tracing;


import io.micrometer.tracing.Span;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class TracingApplication {



	public static void main(String[] args) {
		SpringApplication.run(TracingApplication.class, args);
	}

}
