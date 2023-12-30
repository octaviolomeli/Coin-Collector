package com.octaviolomeli.coinCollector.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(CoreApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}

	@PostMapping("/generateWorld")
	public void generateWorld(@RequestBody WorldInfo wi) {
//		Engine newGame = new Engine(wi.getSeed(), wi.getKeyPresses());
//		newGame.run();
		System.out.println(wi.getKeyPresses());
		System.out.println(wi.getSeed());
		Engine engine = new Engine();
		engine.interactWithKeyboard();
	}

	// For mapping JSON to WorldInfo object
	public static class WorldInfo {
		private final String seed;
		private String keyPresses = "";

		public WorldInfo(String s, String k) {
			this.seed = s;
			this.keyPresses = k;
		}

		public String getSeed() { return this.seed; }

		public String getKeyPresses() { return this.keyPresses; }
	}
}
