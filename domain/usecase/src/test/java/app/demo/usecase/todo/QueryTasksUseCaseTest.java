package app.demo.usecase.todo;

import app.demo.domain.todo.TaskToDo;
import app.demo.domain.todo.gateway.TaskToDoRepository;
import app.demo.domain.user.User;
import app.demo.domain.user.gateway.UserGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static reactor.function.TupleUtils.consumer;

@RunWith(MockitoJUnitRunner.class)
public class QueryTasksUseCaseTest {

    @InjectMocks
    private QueryTasksUseCase useCase;

    @Mock
    private TaskToDoRepository repository;

    @Mock
    private UserGateway userGateway;

    private AtomicInteger queryCount = new AtomicInteger();

    private final TaskToDo task1 = TaskToDo.builder().id("1").name("Task 1").description("Task Desc").assignedUserId("56").build();
    private final TaskToDo task2 = TaskToDo.builder().id("2").name("Task 2").description("Task Desc").assignedUserId("56").build();
    private final TaskToDo task3 = TaskToDo.builder().id("3").name("Task 3").description("Task Desc").build();
    private final String taskId = "1";
    private final String userId = "56";
    private final User user = User.builder().id("56").name("Daniel").lastName("Ospina").build();

    @Before
    public void init() {
        final Flux<TaskToDo> tasks = Flux.just(
            task1,
            task2,
            task3
        ).doOnSubscribe(s -> queryCount.incrementAndGet());
        when(repository.findAll()).thenReturn(tasks);
        when(repository.findById(taskId)).thenReturn(Mono.just(task1));
        when(repository.findById("3")).thenReturn(Mono.just(task3));
        when(userGateway.findById(userId)).thenReturn(Mono.just(user));
    }

    @Test
    public void findAll() {
        //Assert query
        StepVerifier.create(useCase.findAll()).expectNext(task1, task2, task3).verifyComplete();
        StepVerifier.create(useCase.findAll()).expectNext(task1, task2, task3).verifyComplete();
        StepVerifier.create(useCase.findAll()).expectNext(task1, task2, task3).verifyComplete();

        //Assert cache
        assertThat(queryCount).hasValue(1);
    }

    @Test
    public void findTodoWithDetails() {
        final Mono<Tuple2<TaskToDo, User>> withDetails = useCase.findTodoWithDetails(taskId);
        StepVerifier.create(withDetails)
            .assertNext(consumer((task, user) -> {
                assertThat(task).isEqualTo(task1);
                assertThat(user).isEqualTo(user);
            })).verifyComplete();
    }

    @Test
    public void findTodoWithDetailsWhenUserIsEmpty() {
        final Mono<Tuple2<TaskToDo, User>> withDetails = useCase.findTodoWithDetails("3");
        StepVerifier.create(withDetails)
            .assertNext(consumer((task, user) -> {
                assertThat(task).isEqualTo(task3);
                assertThat(user.getId()).isNull();
            })).verifyComplete();
    }
}