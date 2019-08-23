package app.demo.domain.todo.events;

import app.demo.domain.todo.TaskToDo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import app.demo.domain.common.Event;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class TaskCreated implements Event {

    public static final String EVENT_NAME = "todoTasks.task.created";
    private final TaskToDo task;
    private final Date date;

    @Override
    public String name() {
        return EVENT_NAME;
    }

}
