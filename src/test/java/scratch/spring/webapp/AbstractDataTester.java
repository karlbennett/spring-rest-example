package scratch.spring.webapp;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import scratch.spring.webapp.config.ScratchConfiguration;

import javax.sql.DataSource;

import static org.springframework.util.ResourceUtils.getFile;

/**
 * @author Karl Bennett
 */
@RunWith(SpringJUnit4ClassRunner.class)
// This annotation tells Spring where to find the configuration file.
@ContextConfiguration(classes = ScratchConfiguration.class)
// This annotation enables the Spring MVC framework.
@WebAppConfiguration
public abstract class AbstractDataTester {

    @Autowired
    private DataSource dataSource;

    private IDatabaseConnection connection;


    @Before
    public void dataSetUp() throws Exception {

        connection = new DatabaseConnection(dataSource.getConnection());
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(getFile("classpath:testData.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
    }

    @After
    public void dataTearDown() throws Exception {

        truncateTable("user");
        connection.close();
    }


    /**
     * Truncate the table with the supplied name.
     *
     * @param tableName the name of the table to truncate.
     * @throws Exception when the truncation fails.
     */
    public void truncateTable(String tableName) throws Exception {

        DatabaseOperation.TRUNCATE_TABLE.execute(connection, new DefaultDataSet(new DefaultTable(tableName)));
    }

    /**
     * Get the row with the supplied {@code id} form the table with the supplied {@code name}.
     *
     *
     * @param name the name of the table to request the row from.
     * @param email
     * @return the table containing only the request rwo.
     * @throws Exception if the table or row could not be found.
     */
    public ITable getTableRow(String name, String email) throws Exception {

        // This is the name given to the DBUnit table object not the name of the table in the database.
        final String tableObjectName = "table";

        QueryDataSet dataSet = new QueryDataSet(connection);
        dataSet.addTable(tableObjectName, "SELECT * FROM " + name + " WHERE email = '" + email + "'");

        return dataSet.getTable(tableObjectName);
    }
}
