package com.zx.serviceschedule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan("com.zx.serviceschedule.mapper")
@EnableEurekaClient
public class ServiceScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceScheduleApplication.class, args);
	}

}
