package app.demo.usecase.todo;

import app.demo.domain.common.EventsGateway;
import app.demo.domain.common.UniqueIDGenerator;
import app.demo.domain.common.ex.BusinessException;
import app.demo.domain.todo.TaskToDo;
import app.demo.domain.todo.TaskToDoOperations;
import app.demo.domain.todo.events.TaskAssigned;
import app.demo.domain.todo.gateway.TaskToDoRepository;
import app.demo.domain.user.User;
import app.demo.domain.user.gateway.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

@RequiredArgsConstructor
public class AssignTasksUseCase {

    private final TaskToDoRepository tasks;
    private final UserGateway users;
    private final EventsGateway eventBus;

    public Mono<Void> assignTask(String taskId, String userId){
        return Mono.zip(findTask(taskId), findUser(userId))
            .flatMap(TupleUtils.function(TaskToDoOperations::assignToUser))
            .flatMap(tasks::save)
            .flatMap(this::emitAssignedEvent);
    }

    private Mono<Void> emitAssignedEvent(TaskToDo task) {
        return UniqueIDGenerator.now().flatMap(now -> eventBus.emit(new TaskAssigned(task, now)));
    }

    private Mono<TaskToDo> findTask(String id){
        return tasks.findById(id).switchIfEmpty(Mono.error(BusinessException.Type.TASK_NOT_FOUND.defer()));
    }

    private Mono<User> findUser(String id){
        return users.findById(id).switchIfEmpty(Mono.error(BusinessException.Type.USER_NOT_EXIST.defer()));
    }
}
