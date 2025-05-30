package com.saintnet.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AggregatorApplication {
	private final Environment environment;

	public AggregatorApplication(Environment environment){
		this.environment = environment;
	}

	public static void main(String[] args) {
		SpringApplication.run(AggregatorApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String port = environment.getProperty("server.port", "8090"); // Usar el puerto de application.properties
        System.out.println("****************************************************************************");
        System.out.println("* SAINT Agregador Iniciado                            *");
        System.out.println("* *");
        System.out.println("* Interfaz de Administración de Tiendas: http://localhost:" + port + "/admin-tiendas *");
        System.out.println("* API para Aplicaciones Móviles:    http://localhost:" + port + "/api/v1/...      *"); // Ajustar la ruta base de tu API
        System.out.println("****************************************************************************");
    }

}
