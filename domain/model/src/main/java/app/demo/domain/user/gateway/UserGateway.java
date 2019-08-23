package app.demo.domain.user.gateway;

import app.demo.domain.user.User;
import reactor.core.publisher.Mono;

public interface UserGateway {
    Mono<User> findById(String id);
}
