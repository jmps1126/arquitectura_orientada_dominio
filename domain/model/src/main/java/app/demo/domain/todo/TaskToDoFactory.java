package app.demo.domain.todo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static app.demo.domain.common.StringUtils.isEmpty;
import static app.demo.domain.common.ex.BusinessException.Type.INVALID_TODO_INITIAL_DATA;
import static app.demo.domain.todo.TaskToDo.TaskReportStatus.PENDING_ASSIGNMENT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskToDoFactory {

    public static Mono<TaskToDo> createTask(String id, String name, String desc){
        return isEmpty(id, name, desc)
            ? error(INVALID_TODO_INITIAL_DATA.build())
            : just(TaskToDo.builder().id(id).name(name).description(desc).reportStatus(PENDING_ASSIGNMENT).build());
    }
}
