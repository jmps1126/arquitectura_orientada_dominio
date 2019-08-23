package app.demo.usecase.todo;

import app.demo.domain.common.ex.BusinessException;
import app.demo.domain.todo.TaskToDo;
import app.demo.domain.todo.TaskToDoOperations;
import app.demo.domain.user.User;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskToDoOperationsTest {

    @Test
    /*
     * Note que ya que las funciones son puras (poseen trasparencia referencial, es muy fácil probarlas y no se
     * requieren mocks para validar su comportamiento
     */
    public void shouldMarkAsDone() {
        final TaskToDo task1 = TaskToDo.builder().id("01").description("Desc").name("Task 1")
            .reportStatus(TaskToDo.TaskReportStatus.ASSIGNED).assignedUserId("32").build();
        final Date doneDate = new Date();
        final TaskToDo taskDone = TaskToDoOperations.markAsDone(doneDate).apply(task1).block();

        // Assert that function has referential transparency (return a new instance)
        assertThat(task1).isNotEqualTo(taskDone);
        assertThat(task1.isDone()).isFalse();
        assertThat(task1.getDoneDate()).isNull();
        //

        //Assert function result
        assertThat(taskDone).extracting(TaskToDo::getDoneDate,
            TaskToDo::isDone,
            TaskToDo::getId,
            TaskToDo::getDescription,
            TaskToDo::getName)
            .containsExactly(doneDate, true, "01", "Desc", "Task 1");
    }

    @Test
    public void shouldFailMarkAsDone() {
        final TaskToDo task1 = TaskToDo.builder().id("01").description("Desc").name("Task 1")
            .reportStatus(TaskToDo.TaskReportStatus.PENDING_ASSIGNMENT).assignedUserId("32").build();
        final Date doneDate = new Date();
        final Mono<TaskToDo> taskDone = TaskToDoOperations.markAsDone(doneDate).apply(task1);

        StepVerifier.create(taskDone).expectErrorSatisfies(err ->
            assertThat(err).hasMessage("Task has not been assigned!"));
    }

    @Test
    public void shouldAssignToUser() {
        final TaskToDo task1 = TaskToDo.builder().id("01").description("Desc").name("Task 1").build();
        final String userId = "42";

        final Mono<TaskToDo> result = TaskToDoOperations.assignToUser(task1, User.builder().id(userId).build());
        StepVerifier.create(result).assertNext(taskToDo -> {
            // Assert that function has referential transparency (return a new instance)
            assertThat(task1).isNotEqualTo(taskToDo);
            assertThat(task1.getAssignedUserId()).isNull();
            //

            //Assert function result
            assertThat(taskToDo).extracting(
                TaskToDo::getId,
                TaskToDo::getDescription,
                TaskToDo::getName,
                TaskToDo::getAssignedUserId)
                .containsExactly("01", "Desc", "Task 1", userId);
        }).verifyComplete();
    }

    @Test
    public void shouldFailAssignToUserWhenAlreadyAssigned() {
        final TaskToDo task1 = TaskToDo.builder().id("01").description("Desc").name("Task 1")
            .assignedUserId("01").build();
        final String userId = "42";

        final Mono<TaskToDo> result = TaskToDoOperations.assignToUser(task1, User.builder().id(userId).build());

        StepVerifier.create(result)
            .expectErrorSatisfies(err -> assertThat(err)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Task already assigned!"))
            .verify();
    }

    @Test
    public void setPendingToReAssign() {
        final TaskToDo task = TaskToDo.builder().id("01").description("Desc")
            .name("Task 1").assignedUserId("56").reportStatus(TaskToDo.TaskReportStatus.ASSIGNED).build();

        final TaskToDo task1 = TaskToDoOperations.setPendingToReAssign().apply(task);

        // Assert that function has referential transparency (return a new instance)
        assertThat(task).isNotEqualTo(task1);
        assertThat(task.getReportStatus()).isEqualByComparingTo(TaskToDo.TaskReportStatus.ASSIGNED);
        //

        //Assert function result
        assertThat(task1.getReportStatus()).isEqualByComparingTo(TaskToDo.TaskReportStatus.PENDING_REASSIGNMENT);
        assertThat(task1.getAssignedUserId()).isNull();
    }
}