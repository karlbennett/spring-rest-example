package scratch.spring.webapp.data;

import org.springframework.data.repository.CrudRepository;
import scratch.user.User;

/**
 * A CRUD repository for the {@link User} class. It extends from the Spring Data {@link CrudRepository} which means it's
 * implementation will be generated on application start.
 *
 * @author Karl Bennett
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
