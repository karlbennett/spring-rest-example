package scratch.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.ITable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import scratch.webapp.AbstractDataTester;
import scratch.webapp.data.User;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import static org.junit.Assert.*;

/**
 * @author Karl Bennett
 */
public class ScratchControllerTest extends AbstractDataTester {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String ARRAY_ONE = "$[0].";
    private static final String ARRAY_TWO = "$[1].";
    private static final String ARRAY_THREE = "$[2].";
    private static final String ARRAY_FOUR = "$[3].";

    private static final String ID_PATH = "$." + ID;
    private static final String EMAIL_PATH = "$." + EMAIL;
    private static final String FIRST_NAME_PATH = "$." + FIRST_NAME;
    private static final String LAST_NAME_PATH = "$." + LAST_NAME;

    private static final String ERROR = "error";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;


    @Before
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void testHandle() throws Exception {
        mockMvc.perform(get("/").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.scratched").value(true));
    }

    @Test
    public void testCreate() throws Exception {

        User user = new User();
        user.setEmail(EMAIL_FOUR);
        user.setFirstName(FIRST_NAME_FOUR);
        user.setLastName(LAST_NAME_VALUE);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/users")
                        .content(OBJECT_MAPPER.writeValueAsString(user))
                        .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(User.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath(EMAIL_PATH).value(EMAIL_FOUR))
                .andExpect(jsonPath(FIRST_NAME_PATH).value(FIRST_NAME_FOUR))
                .andExpect(jsonPath(LAST_NAME_PATH).value(LAST_NAME_VALUE));

        ITable table = getTableRow(USER_TABLE, EMAIL_FOUR);

        assertEquals("a row with email four should have been created.", EMAIL_FOUR, table.getValue(0, EMAIL));
        assertEquals("a row with first name four should have been created.",
                FIRST_NAME_FOUR, table.getValue(0, FIRST_NAME));
        assertEquals("a row with the last name should have been created.",
                LAST_NAME_VALUE, table.getValue(0, LAST_NAME));
    }

    @Test
    public void testCreateExistingUser() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
                post("/users")
                        .content(OBJECT_MAPPER.writeValueAsString(USER_ONE))
                        .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(EntityExistsException.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$." + ERROR).value(EntityExistsException.class.getSimpleName()));
    }

    @Test
    public void testCreateExistingEmail() throws Exception {

        User user = new User();
        user.setEmail(EMAIL_TWO);
        user.setFirstName(FIRST_NAME_FOUR);
        user.setLastName(LAST_NAME_VALUE);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/users")
                        .content(OBJECT_MAPPER.writeValueAsString(user))
                        .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(PersistenceException.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$." + ERROR).value(PersistenceException.class.getSimpleName()));
    }

    @Test
    public void testRetrieve() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
                get("/users/{id}", 1)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(User.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath(ID_PATH).value(ID_ONE.intValue()))
                .andExpect(jsonPath(EMAIL_PATH).value(EMAIL_ONE))
                .andExpect(jsonPath(FIRST_NAME_PATH).value(FIRST_NAME_ONE))
                .andExpect(jsonPath(LAST_NAME_PATH).value(LAST_NAME_VALUE));
    }

    @Test
    public void testRetrieveNonExistentUser() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
                get("/users/{id}", -1)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(EntityNotFoundException.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$." + ERROR).value(EntityNotFoundException.class.getSimpleName()));
    }

    @Test
    public void testRetrieveAll() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
                get("/users")
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(Iterable.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath(ARRAY_ONE + ID).value(ID_ONE.intValue()))
                .andExpect(jsonPath(ARRAY_ONE + EMAIL).value(EMAIL_ONE))
                .andExpect(jsonPath(ARRAY_ONE + FIRST_NAME).value(FIRST_NAME_ONE))
                .andExpect(jsonPath(ARRAY_ONE + LAST_NAME).value(LAST_NAME_VALUE))
                .andExpect(jsonPath(ARRAY_TWO + ID).value(ID_TWO.intValue()))
                .andExpect(jsonPath(ARRAY_TWO + EMAIL).value(EMAIL_TWO))
                .andExpect(jsonPath(ARRAY_TWO + FIRST_NAME).value(FIRST_NAME_TWO))
                .andExpect(jsonPath(ARRAY_TWO + LAST_NAME).value(LAST_NAME_VALUE))
                .andExpect(jsonPath(ARRAY_THREE + ID).value(ID_THREE.intValue()))
                .andExpect(jsonPath(ARRAY_THREE + EMAIL).value(EMAIL_THREE))
                .andExpect(jsonPath(ARRAY_THREE + FIRST_NAME).value(FIRST_NAME_THREE))
                .andExpect(jsonPath(ARRAY_THREE + LAST_NAME).value(LAST_NAME_VALUE));
    }

    @Test
    public void testUpdate() throws Exception {

        User user = new User();
        user.setEmail(EMAIL_FOUR);
        user.setFirstName(FIRST_NAME_FOUR);
        user.setLastName(LAST_NAME_VALUE);

        MvcResult mvcResult = this.mockMvc.perform(
                put("/users/{id}", 3)
                        .content(OBJECT_MAPPER.writeValueAsString(user))
                        .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(User.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath(ID_PATH).value(ID_THREE.intValue()))
                .andExpect(jsonPath(EMAIL_PATH).value(EMAIL_FOUR))
                .andExpect(jsonPath(FIRST_NAME_PATH).value(FIRST_NAME_FOUR))
                .andExpect(jsonPath(LAST_NAME_PATH).value(LAST_NAME_VALUE));

        DefaultTable table = new DefaultTable("updated_table", COLUMNS);
        table.addRow(new Object[]{ID_THREE, EMAIL_FOUR, FIRST_NAME_FOUR, LAST_NAME_VALUE});

        assertTableEquals("user four should be created.", table, getTableRow("user", EMAIL_FOUR));
    }

    @Test
    public void testUpdateWithExistingEmail() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
                put("/users/{id}", 3)
                        .content(OBJECT_MAPPER.writeValueAsString(USER_ONE))
                        .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(DataIntegrityViolationException.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$." + ERROR).value(DataIntegrityViolationException.class.getSimpleName()));
    }

    @Test
    public void testDelete() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
                delete("/users/{id}", 3)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(User.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath(ID_PATH).value(ID_THREE.intValue()))
                .andExpect(jsonPath(EMAIL_PATH).value(EMAIL_THREE))
                .andExpect(jsonPath(FIRST_NAME_PATH).value(FIRST_NAME_THREE))
                .andExpect(jsonPath(LAST_NAME_PATH).value(LAST_NAME_VALUE));

        ITable table = getTableRow(USER_TABLE, EMAIL_THREE);

        assertEquals("the row with user three should not exists.", 0, table.getRowCount());
    }

    @Test
    public void testDeleteNonExistentUser() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
                delete("/users/{id}", -1)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
        )
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(EntityNotFoundException.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$." + ERROR).value(EntityNotFoundException.class.getSimpleName()));
    }
}
