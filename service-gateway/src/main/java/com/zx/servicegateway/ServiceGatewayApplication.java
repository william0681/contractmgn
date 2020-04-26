package com.zx.servicegateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

@EnableSwagger2
@MapperScan("com.zx.servicegateway.mapper")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ServiceGatewayApplication extends SpringBootServletInitializer {
	
	@Value("${tmp.dir}")
	private String tmpdir;

	public static void main(String[] args) {
		SpringApplication.run(ServiceGatewayApplication.class, args);
	}

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.zx.servicegatewayntractmgn.controller"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("合同管理系统测试接口平台API").description("cathy demo  API.")
				.termsOfServiceUrl("Terms of service").contact(new Contact("ylw", "", "807281235@qq.com"))
				.version("1.0").build();
	}

	@Bean
	@Qualifier("contract")
	public Map<String, String> getContractMap() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("合同名称", "c.name contract_name");
		map.put("合同类别", "ct.name contract_type");
		map.put("合同内容类别", "ctype.name content_type");
		map.put("归属区块", "b.name block_name");
		map.put("签订日期", "c.sign_date sign_date");
		map.put("经办人", "c.operator operator");
		map.put("销售业务员", "substring(get_sellers(c.id),2) sellers");
		map.put("技术服务员", "c.technician technician");
		map.put("客户", "c.customer customer");
		map.put("合同总额", "c.amount total_money");
		map.put("待收余额", "c.rest_amount rest_money");
		return map;

	}


	@Bean
	@Qualifier("financial")
	public Map<String,String> getFinancialMap()
	{
		Map<String, String> map = new LinkedHashMap<>();
		map.put("合同名称", "c.name contract_name");
		map.put("合同类别", "ct.name contract_type");
		map.put("合同内容类别", "ctype.name content_type");
		map.put("归属区块", "b.name block_name");
		return map;
	}

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder builder) {
		return builder.sources(this.getClass());
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		File tmpDir = new File(tmpdir);
		if(!tmpDir.exists()){
			tmpDir.mkdir();
		}
		factory.setLocation(tmpdir);//指定临时文件路径
		return factory.createMultipartConfig();
	}
}
