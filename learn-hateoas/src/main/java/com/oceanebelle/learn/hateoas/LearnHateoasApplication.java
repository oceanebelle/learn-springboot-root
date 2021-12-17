package com.oceanebelle.learn.hateoas;

import com.oceanebelle.learn.logging.LogMessage;
import com.oceanebelle.learn.logging.LogMessageFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

@Log4j2
@SpringBootApplication
public class LearnHateoasApplication {
	private static final String APP="APP";

	public static void main(String[] args) {
		log.info(LogMessageFactory.startAction(APP).kv("message","Starting up application."));
		SpringApplication.run(LearnHateoasApplication.class, args);
	}

	@PreDestroy
	public void destroy() {
		log.info(LogMessageFactory.endAction(APP).kv("message","Shutting down application."));
	}
}
