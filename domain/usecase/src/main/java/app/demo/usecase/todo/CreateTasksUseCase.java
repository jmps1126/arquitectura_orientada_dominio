package app.demo.usecase.todo;

import app.demo.domain.common.EventsGateway;
import app.demo.domain.todo.TaskToDo;
import app.demo.domain.todo.TaskToDoFactory;
import app.demo.domain.todo.gateway.TaskToDoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import app.demo.domain.todo.events.TaskCreated;

import static app.demo.domain.common.UniqueIDGenerator.now;
import static app.demo.domain.common.UniqueIDGenerator.uuid;

@RequiredArgsConstructor
public class CreateTasksUseCase {

    private final TaskToDoRepository tasks;
    private final EventsGateway eventBus;

    public Mono<TaskToDo> createNew(String name, String description) {
        return uuid()
            .flatMap(id -> TaskToDoFactory.createTask(id, name, description))
            .flatMap(tasks::save)
            .flatMap(task -> emitCreatedEvent(task).thenReturn(task));
    }

    private Mono<Void> emitCreatedEvent(TaskToDo task) {
        return now().flatMap(now -> eventBus.emit(new TaskCreated(task, now)));
    }
}
