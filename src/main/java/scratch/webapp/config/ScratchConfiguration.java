package scratch.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import scratch.webapp.controller.ScratchController;

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
@ComponentScan(basePackageClasses = {ScratchController.class})
// Tell Spring which package to look in for data access repositories.
@EnableJpaRepositories(basePackages = "scratch.webapp.data")
@EnableTransactionManagement
public class ScratchConfiguration {

    /**
     * Configure the {@link HibernateExceptionTranslator}, it doesn't mention to do this in the Spring documentation,
     * but I had to or the application context wouldn't initialise.
     *
     * @return the {@code HibernateExceptionTranslator} that will be used to map Hibernate exceptions.
     */
    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator(){
        return new HibernateExceptionTranslator();
    }

    /**
     * The {@code HSQL} data source that will be used to persist data into the in memory HSQL database.
     *
     * @return the {@code DataSource} that will be used for interfacing with the database.
     */
    @Bean
    public DataSource dataSource() {

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
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("scratch.webapp.data");
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    /**
     * The Spring transaction manager. All the default Spring Data
     * {@link org.springframework.data.repository.Repository} interface methods are transactional by default.
     *
     * @return the {@link PlatformTransactionManager}.
     */
    @Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }
}
