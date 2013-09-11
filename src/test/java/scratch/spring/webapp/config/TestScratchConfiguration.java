package scratch.spring.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import scratch.spring.webapp.data.User;
import scratch.spring.webapp.data.UserRepository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Configuration class used to configure the Spring framework for the integration tests. A separate configuration file
 * is used so that the Spring MVC framework is not started.
 *
 * @author Karl Bennett
 */
@Configuration
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

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void postConstruct() {

        User.setStaticRepository(userRepository);
    }
}
