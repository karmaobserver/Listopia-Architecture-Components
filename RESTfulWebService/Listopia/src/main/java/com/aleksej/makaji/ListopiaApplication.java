package com.aleksej.makaji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


@SpringBootApplication
public class ListopiaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ListopiaApplication.class, args);
		System.out.println("Listopia RESTful Web Service started");
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ListopiaApplication.class);
    }
}
