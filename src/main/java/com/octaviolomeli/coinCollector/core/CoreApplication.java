package com.octaviolomeli.coinCollector.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@PostMapping("/seed")
	public void seed(@RequestParam(value = "seed") Long seed, @RequestParam(value = "keyPresses", defaultValue = "") String keyPresses) {

	}
}
