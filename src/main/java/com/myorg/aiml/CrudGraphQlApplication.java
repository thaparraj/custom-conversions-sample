package com.myorg.aiml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.myorg")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class CrudGraphQlApplication {

	public static void main(String[] args){

		SpringApplication.run(CrudGraphQlApplication.class,  args);
}

}
