package app.demo.domain.todo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;
import app.demo.domain.user.User;

import java.util.Date;
import java.util.function.Function;

import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static app.demo.domain.common.StringUtils.isEmpty;
import static app.demo.domain.common.ex.BusinessException.Type.TASK_ALREADY_ASSIGNED;
import static app.demo.domain.common.ex.BusinessException.Type.TASK_NOT_ASSIGNED;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskToDoOperations {

    public static Function<TaskToDo, Mono<TaskToDo>> markAsDone(Date doneDate){
        return task ->
            task.getReportStatus() != TaskToDo.TaskReportStatus.ASSIGNED ?
                error(TASK_NOT_ASSIGNED.build()) :
                just(task.toBuilder().doneDate(doneDate).done(true).build());
    }

    public static Function<TaskToDo, TaskToDo> setPendingToReAssign(){
        return task -> task.toBuilder().assignedUserId(null).reportStatus(TaskToDo.TaskReportStatus.PENDING_REASSIGNMENT).build();
    }

    public static Mono<TaskToDo> assignToUser(TaskToDo task, User user){
        return !isEmpty(task.getAssignedUserId()) ? error(TASK_ALREADY_ASSIGNED.build()) :
            just(task.toBuilder().assignedUserId(user.getId()).reportStatus(TaskToDo.TaskReportStatus.ASSIGNED).build());
    }

}
