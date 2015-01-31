package example.rest.spring.config;

import example.rest.spring.data.DBUnitUserRepository;
import example.rest.spring.data.UserSteps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TestConfiguration {

    @Bean
    @Autowired
    public UserSteps userSteps(DataSource dataSource) {

        final DBUnitUserRepository dbUnitUserRepository = new DBUnitUserRepository(dataSource);

        return new UserSteps(dbUnitUserRepository);
    }
}
