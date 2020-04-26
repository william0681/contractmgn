package com.zx.servicereminder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan("com.zx.servicereminder.mapper")
@EnableEurekaClient
public class ServiceReminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceReminderApplication.class, args);
	}

}
