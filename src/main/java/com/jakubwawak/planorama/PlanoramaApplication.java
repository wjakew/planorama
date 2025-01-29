/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.planorama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.jakubwawak.planorama.backend.database_service.Database;
import com.jakubwawak.planorama.backend.database_service.DatabaseUser;
import com.jakubwawak.planorama.maintanance.ConsoleColors;
import com.jakubwawak.planorama.maintanance.Properties;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.theme.Theme;

/**
 * Main class for the Planorama application.
 * Planorama is a web application that allows you to plan projects like tasks.
 */
@SpringBootApplication
@EnableVaadin({ "com.jakubwawak" })
@Theme(value = "global_planorama")
public class PlanoramaApplication extends SpringBootServletInitializer implements AppShellConfigurator {

	public static Properties properties;

	public static Database database;

	public static String version = "0.0.1";

	public static int accountCreationEnabled;

	public static String tenantName;

	/**
	 * Main method for the Planorama application.
	 * 
	 * @param args the arguments for the application
	 */
	public static void main(String[] args) {
		showHeader();
		database = new Database();
		properties = new Properties("planorama.properties");
		if (properties.fileExists) {
			properties.parsePropertiesFile();
			database.setDatabase_url(properties.getValue("database_url"));
			database.connect();
			if (database.connected) {
				accountCreationEnabled = Integer.parseInt(properties.getValue("account_creation_enabled"));
				tenantName = properties.getValue("tenant_name");

				// check if admin is registered
				DatabaseUser databaseUser = new DatabaseUser();
				if (!databaseUser.isUserRegistered("admin@planorama.com")) {
					int ans = databaseUser.registerDefaultAdmin();
					if (ans == 1) {
						System.out.println(ConsoleColors.GREEN
								+ "Default admin registered - email: admin@planorama.com, password: admin"
								+ ConsoleColors.RESET);
					} else {
						System.out
								.println(ConsoleColors.RED + "Failed to register default admin" + ConsoleColors.RESET);
					}
				}

				// run application
				SpringApplication.run(PlanoramaApplication.class, args);
			} else {
				System.out.println(ConsoleColors.RED + "Failed to connect to database" + ConsoleColors.RESET);
			}
		} else {
			System.out.println(ConsoleColors.RED + "Properties file not found" + ConsoleColors.RESET);
			properties.createPropertiesFile();
			System.out.println(ConsoleColors.GREEN + "Properties file created" + ConsoleColors.RESET);
		}
	}

	/**
	 * Function for showing header
	 */
	static void showHeader() {
		System.out.println(ConsoleColors.BLUE + "Planorama" + ConsoleColors.RESET);
		System.out.println(ConsoleColors.BLUE + "by Jakub Wawak" + ConsoleColors.RESET);
		System.out.println(ConsoleColors.BLUE + "kubawawak@gmail.com" + ConsoleColors.RESET);
		System.out.println(ConsoleColors.BLUE + "all rights reserved" + ConsoleColors.RESET);
		System.out.println(ConsoleColors.BLUE + "version " + version + ConsoleColors.RESET);
	}

	/**
	 * Function for showing notification
	 * @param message
	 */
	public static void showNotification(String message) {
		Notification.show(message);
	}

}
