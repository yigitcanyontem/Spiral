package com.yigitcanyontem.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.Locale;

@SpringBootApplication
@EnableCaching
public class AboutmeApplication {

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		SpringApplication.run(AboutmeApplication.class, args);
	}

}
