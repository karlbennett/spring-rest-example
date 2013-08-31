package scratch.spring.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import scratch.spring.webapp.controller.ScratchController;
import scratch.spring.webapp.controller.UserController;
import scratch.spring.webapp.data.User;
import scratch.spring.webapp.data.UserRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Configuration class used to configure the Spring framework.
 *
 * @author Karl Bennett
 */
@Configuration
@EnableWebMvc // Enable the Spring Web MVC environment, this includes support for XML/JSON conversion and validation.
// Tell Spring which package to look in for controller classes. This has been done by providing a class from the
// required package.
@ComponentScan(basePackageClasses = {UserController.class, ScratchController.class})
// Tell Spring which package to look in for data access repositories.
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
@EnableTransactionManagement
public class ScratchConfiguration {

    /**
     * The {@code HSQL} data source that will be used to persist data into the in memory HSQL database.
     *
     * @return the {@code DataSource} that will be used for interfacing with the database.
     */
    @Bean
    public static DataSource dataSource() {

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.HSQL).build();
    }

    /**
     * The JPA {@link EntityManagerFactory} that Spring Data will use to interface with Hibernate for persisting
     * objects.
     *
     * @return the {@code EntityManagerFactory} that will be used for interfacing with the Hibernate.
     */
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

    /**
     * The Spring transaction manager. All the Spring Data {@link org.springframework.data.repository.Repository}
     * interface methods are transactional by default.
     *
     * @return the {@link PlatformTransactionManager}.
     */
    @Bean
    public static PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return txManager;
    }

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void postConstruct() {

        User.setStaticRepository(userRepository);
    }
}
