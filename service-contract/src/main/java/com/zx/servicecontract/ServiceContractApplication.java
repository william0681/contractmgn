package com.zx.servicecontract;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.zx.servicecontract.mapper")
@EnableEurekaClient
@EnableFeignClients
public class ServiceContractApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceContractApplication.class, args);
	}

}
