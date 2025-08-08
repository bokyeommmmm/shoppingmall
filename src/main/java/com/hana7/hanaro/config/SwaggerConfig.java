package com.hana7.hanaro.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		Server devServer = new Server();
		Server prodServer = new Server();
		devServer.setUrl("/");
		prodServer.setUrl("/api");

		return new OpenAPI()
			.servers(List.of(devServer, prodServer))
			.info(getInfo());
	}

	private Info getInfo() {
		return new Info()
			.version("0.1.0")
			.title("SpringDemo APIs")
			.description("Java & Spring 개인과제 ");
	}

}
