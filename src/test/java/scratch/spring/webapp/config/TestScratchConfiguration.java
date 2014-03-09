package scratch.spring.webapp.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import scratch.spring.webapp.controller.ScratchController;
import scratch.spring.webapp.controller.UserController;
import scratch.spring.webapp.data.User;
import scratch.spring.webapp.data.UserRepository;
import scratch.spring.webapp.test.DBUnit;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Test configuration that does not have the @EnableWebMvc annotation because the tests are not running with a
 * JEE container.
 *
 * @author Karl Bennett
 */
@Configuration
@ComponentScan(basePackageClasses = {UserController.class, ScratchController.class, DBUnit.class})
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
@EnableTransactionManagement
public class TestScratchConfiguration {

    @Bean
    public static DataSource dataSource() {

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.HSQL).build();
    }

    @Bean
    public static LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.HSQL);
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("scratch.spring.webapp.data");
        factory.setDataSource(dataSource());

        return factory;
    }

    @Bean
    public static PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return txManager;
    }

    @Bean
    public static WebTarget client(@Value("rest.baseUrl") String url) {

        final Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        return client.target(url);
    }

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void postConstruct() {

        User.setStaticRepository(userRepository);
    }
}
