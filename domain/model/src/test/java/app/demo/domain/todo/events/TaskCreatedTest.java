package app.demo.domain.todo.events;

import app.demo.domain.todo.TaskToDo;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskCreatedTest {

    @Test
    public void shouldHasName() {
        final TaskCreated taskCreated = new TaskCreated(TaskToDo.builder().build(), new Date());
        assertThat(taskCreated.name()).isEqualTo(TaskCreated.EVENT_NAME);
    }
}