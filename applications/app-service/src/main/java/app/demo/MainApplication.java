package app.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    //Descomentar para lanzar acción de prueba al iniciar la App
//    @Bean
//    public CommandLineRunner runner(CreateTasksUseCase useCase, AssignTasksUseCase assignTasksUseCase, CompleteTasksUseCase completeUseCase) throws InterruptedException {
//        return args -> useCase.createNew("Test1", "Desc 1")
//            .flatMap(task -> assignTasksUseCase.assignTask(task.getId(), "56"))
//            .block();
//    }
}
