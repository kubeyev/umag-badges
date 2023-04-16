package com.badgers.umag;

import com.badgers.umag.core.amq.MQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
public class UmagApplication {

	@Value("${server.port}")
	private String serverPort;

	@Autowired
	private MQSender sender;

	@PostConstruct
	public void showServerPort() {
		log.info("SERVER PORT IS " + this.serverPort);
		sender.send();
	}

	public static void main(String[] args) {
		SpringApplication.run(UmagApplication.class, args);
	}

}
