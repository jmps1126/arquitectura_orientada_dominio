package app.demo.usecase.todo;

import app.demo.domain.todo.gateway.TaskToDoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

@RequiredArgsConstructor
@Log
public class ReAssignUserTasksUseCase {

    private final TaskToDoRepository tasks;

    public Mono<Void> markUserTaskAsPendingToReAssign(String userId) {
        log.log(Level.INFO, "La tarea con ID {0} fue creada!!", userId);
        return Mono.empty();
//        return tasks.findAllUserOpenTasks(userId)
//            .map(setPendingToReAssign())
//            .as(tasks::saveAll);
    }
}
