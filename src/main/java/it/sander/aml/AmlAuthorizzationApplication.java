package it.sander.aml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"it.sander.aml"})
public class AmlAuthorizzationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmlAuthorizzationApplication.class, args);
	}
    
}


