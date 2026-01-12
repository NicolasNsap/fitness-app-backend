package com.fitnessapp.config;
//DataInitializer inicializa datos maestros en la base de datos

import com.fitnessapp.entity.Role;
import com.fitnessapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
/*DataInitializer inicializa datos maestros en la base de datos, es como un preparador que llega antes que todos y deja todo listo
 CommandLineRunner interface de spring que ejecuta código automáticamente al iniciar la application
 @Slf4j anotación de lombok que genera automáticamente un logger(log) para escribir mensajes en consola
*/
@Component//es como decir oye spring, esta clase existe y necesito que la conozcas
@RequiredArgsConstructor //constructor con dependencias final
@Slf4j
public class DataInitializer implements CommandLineRunner {

    //inyección de dependencias por constructor
    private final RoleRepository roleRepository;


    /*Método run() se ejecuta automáticamente al iniciar la app
     @param args argumentos de la linea de comandos (no los usamos aqui)
     @throws Exceptions si algo falla
    */
    @Override
    public void run(String... args) throws Exception {
        log.info("\uD83D\uDE80 Iniciando DataInitializer...");

        initializeRoles();

        log.info("✅ DataInitializer completado exitosamente");

    }

    /*
    * Inicializa los roles USER y ADMIN en la base de datos */

    private void initializeRoles(){
        log.info("\uD83D\uDCCB Verificando roles en la base de datos...");

        //creación de rol USER si no existe
        if(!roleRepository.existsByName("USER")){
            //CREACIÓN DEL OBJETO ROLE PARA CREAR EL ROL USER
            Role userRole = Role.builder()
                    .name("USER")
                    .build();
            //guardando en la base de datos
            roleRepository.save(userRole);
            log.info("✅ Rol USER creado exitosamente");
        }else {
            log.info("ℹ️ Rol USER ya existe, omitiendo creación");
        }

        //cracion de rol ADMIN si no existe
        if(!roleRepository.existsByName("ADMIN")){
            //CREACIÓN DEL OBJETO ROLE PARA CREAR EL ROL ADMIN
            Role adminROle = Role.builder()
                    .name("ADMIN")
                    .build();
            //guardando en la base de  datos
            roleRepository.save(adminROle);
            log.info("✅ Rol ADMIN creado exitosamente");
        }else {
            log.info("ℹ️ Rol ADMIN ya existe, omitiendo creación");

        }
    }
}


/*
 * NOTA: Más adelante podríamos agregar más métodos aquí
 * para inicializar otros datos maestros:
 * - initializeExercises() → Ejercicios básicos del catálogo
 * - initializeAdminUser() → Usuario administrador por defecto
 * etc.
 */
