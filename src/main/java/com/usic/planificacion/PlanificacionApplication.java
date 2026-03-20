package com.usic.planificacion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.usic.planificacion.model.entity.Persona;
import com.usic.planificacion.model.entity.Rol;
import com.usic.planificacion.model.entity.Usuario;
import com.usic.planificacion.model.service.PersonaService;
import com.usic.planificacion.model.service.RolService;
import com.usic.planificacion.model.service.UsuarioService;
import com.usic.planificacion.model.service.UtilidadesService;

@SpringBootApplication
public class PlanificacionApplication {
	private static final Logger logger = LoggerFactory.getLogger(PlanificacionApplication.class);

	public static void main(String[] args) throws IOException {
		SpringApplication.run(PlanificacionApplication.class, args);

		Path rootPathCarnet = Paths.get("uploads/");
		String rutaDirectorioCarnet = rootPathCarnet + "";
		if (!Files.exists(rootPathCarnet)) {
			Files.createDirectories(rootPathCarnet);
			System.out.println("Directorio creado: " + rutaDirectorioCarnet);
		} else {
			System.out.println("El directorio ya existe: " + rutaDirectorioCarnet);
		}
	}

	@Bean
	ApplicationRunner init(UsuarioService usuarioService, PersonaService personaService, RolService rolService,
			UtilidadesService utilidadesService) {

		return args -> {

			logger.info("Sistema Iniciado...");

			// Verificar y crear roles si no existen
			String[] roles = { "SUPER USUARIO", "ADMINISTRADOR", "RESPONSABLE", "EVALUADOR" };
			Rol[] rolObjects = new Rol[roles.length];
			for (int i = 0; i < roles.length; i++) {
				Rol rol = rolService.buscaPorNombre(roles[i]);
				if (rol == null) {
					rol = new Rol();
					rol.setNombre(roles[i]);
					rol.setEstado("ACTIVO");
					rolService.save(rol);
				}
				rolObjects[i] = rol;
			}

			// Crear o actualizar la primera persona y su usuario
			String[] cis = { "111", "222" };
			String[] nombres = { "PRIMER USUARIO", "SEGUNDO USUARIO" };
			String[] usuarios = { "admin1", "admin2" };
			String[] password = { "123", "456" };
			for (int i = 0; i < cis.length; i++) {
				// Verificar si la persona ya existe
				Persona persona = personaService.buscarPorCi(cis[i]);
				if (persona == null) {
					persona = new Persona();
					persona.setNombre(nombres[i]);
					persona.setPaterno("ApellidoP" + (i + 1));
					persona.setMaterno("ApellidoM" + (i + 1));
					persona.setCi(cis[i]);
					persona.setEstado("ACTIVO");
					personaService.save(persona);
				}

				// Verificar si el usuario ya existe
				Usuario usuario = usuarioService.buscarPorNombreUser(usuarios[i]);
				if (usuario == null) {
					usuario = new Usuario();
					usuario.setNombre(usuarios[i]);
					// usuario.setPassword(password[i]);
					usuario.setPassword(utilidadesService.encrypt(password[i]));
					usuario.setPersona(persona); // Asociar la persona con el usuario

					// Asignar los roles correspondientes al usuario
					Set<Rol> usuarioRoles = new HashSet<>();
					usuarioRoles.add(rolObjects[i]); // Asigna el rol correspondiente (por ejemplo, rol de admin1,
														// admin2, etc.)
					usuario.setRoles(usuarioRoles); // Asignar el conjunto de roles al usuario
					usuario.setEstado("ACTIVO");
					usuarioService.save(usuario);
				}
			}
		};
	}
}
