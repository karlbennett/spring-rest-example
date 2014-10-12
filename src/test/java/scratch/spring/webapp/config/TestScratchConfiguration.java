package scratch.spring.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import scratch.spring.webapp.data.DBUnitUserRepository;
import scratch.spring.webapp.data.UserSteps;

import javax.sql.DataSource;

@Configuration
@Import(ScratchConfiguration.class)
public class TestScratchConfiguration {

    @Autowired
    @Bean
    public UserSteps userSteps(DataSource dataSource) {

        final DBUnitUserRepository dbUnitUserRepository = new DBUnitUserRepository(dataSource);

        return new UserSteps(dbUnitUserRepository);
    }
}
