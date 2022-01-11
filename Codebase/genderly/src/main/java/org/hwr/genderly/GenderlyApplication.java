package org.hwr.genderly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.net.URI;

@SpringBootApplication
public class GenderlyApplication {

	/**
	 * Handing over control to object of type 'GenderlyController'
	 * @param args set of command line arguments, remain unused in this version
	 */
	public static void main(String[] args) {
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI("http://localhost:8081/"));
			}
		} catch (Exception ignored) {
			// If this fails, please open the Browser by hand
		}
		SpringApplication.run(GenderlyApplication.class, args);
	}

}