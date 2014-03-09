package scratch.spring.webapp.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scratch.spring.webapp.config.TestScratchConfiguration;
import scratch.spring.webapp.data.User;
import scratch.spring.webapp.test.DatabaseTester;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static scratch.spring.webapp.controller.UserController.ErrorResponse;
import static scratch.spring.webapp.controller.UserController.NotFoundException;
import static scratch.spring.webapp.test.DatabaseTester.WithId;
import static scratch.spring.webapp.test.DatabaseTester.WithUser;
import static scratch.spring.webapp.test.DatabaseTester.WithUserAndId;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestScratchConfiguration.class)
public class UserControllerTest {

    @Autowired
    private DatabaseTester databaseTester;

    @Autowired
    private UserController userController;

    @Before
    public void setUp() {

        databaseTester.setUp();
    }

    @After
    public void tearDown() {

        databaseTester.tearDown();
    }

    @Test
    public void testCreate() throws Exception {

        databaseTester.createTest(new CreateWithUser());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCreateDuplicate() throws Exception {

        databaseTester.createDuplicateTest(new CreateWithUser());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCreateWithDuplicateEmail() throws Exception {

        databaseTester.createWithDuplicateEmailTest(new CreateWithUser());
    }

    @Test
    public void testCreateWithDuplicateFirstName() throws Exception {

        databaseTester.createWithDuplicateFirstNameTest(new CreateWithUser());
    }

    @Test
    public void testCreateWithDuplicateLastName() throws Exception {

        databaseTester.createWithDuplicateLastNameTest(new CreateWithUser());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCreateWithNullEmail() throws Exception {

        databaseTester.createWithNullEmailTest(new CreateWithUser());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCreateWithNullFirstName() throws Exception {

        databaseTester.createWithNullFirstNameTest(new CreateWithUser());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCreateWithNullLastName() throws Exception {

        databaseTester.createWithNullLastNameTest(new CreateWithUser());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullUser() throws Exception {

        databaseTester.createWithNullUserTest(new CreateWithUser());
    }

    @Test
    public void testRetrieve() throws Exception {

        databaseTester.retrieveTest(new RetrieveWithId());
    }

    @Test(expected = NotFoundException.class)
    public void testRetrieveWithInvalidId() throws Exception {

        databaseTester.retrieveWithInvalidIdTest(new RetrieveWithId());
    }

    @Test(expected = NullPointerException.class)
    public void testRetrieveWithNullId() throws Exception {

        databaseTester.retrieveWithNullIdTest(new RetrieveWithId());
    }

    @Test
    public void testRetrieveAll() throws Exception {

        databaseTester.retrieveAllTest(userController.retrieveAll());
    }

    @Test
    public void testUpdate() throws Exception {

        databaseTester.updateTest(new UpdateWithUserAndId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDuplicate() throws Exception {

        databaseTester.updateDuplicateTest(new UpdateWithUserAndId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateWithDuplicateEmail() throws Exception {

        databaseTester.updateWithDuplicateEmailTest(new UpdateWithUserAndId());
    }

    @Test
    public void testUpdateWithDuplicateFirstName() throws Exception {

        databaseTester.updateWithDuplicateFirstNameTest(new UpdateWithUserAndId());
    }

    @Test
    public void testUpdateWithDuplicateLastName() throws Exception {

        databaseTester.updateWithDuplicateLastNameTest(new UpdateWithUserAndId());
    }

    @Test
    public void testUpdateWithNullId() throws Exception {

        databaseTester.updateWithNullIdTest(new UpdateWithUserAndId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateWithNullEmail() throws Exception {

        databaseTester.updateWithNullEmailTest(new UpdateWithUserAndId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateWithNullFirstName() throws Exception {

        databaseTester.updateWithNullFirstNameTest(new UpdateWithUserAndId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateWithNullLastName() throws Exception {

        databaseTester.updateWithNullLastNameTest(new UpdateWithUserAndId());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateWithNullUser() throws Exception {

        databaseTester.updateWithNullUserTest(new UpdateWithUserAndId());
    }

    @Test
    public void testDelete() throws Exception {

        databaseTester.deleteTest(new DeleteWithId());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteWithInvalidId() throws Exception {

        databaseTester.deleteWithInvalidIdTest(new DeleteWithId());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteWithNullId() throws Exception {

        databaseTester.deleteWithNullIdTest(new DeleteWithId());
    }

    @Test
    public void testHandleExceptionWithNotFoundException() {

        handleExceptionTest(404, NotFoundException.class, new HandleException() {
            @Override
            public ErrorResponse handle(String message, HttpServletResponse response) {
                return userController.handleException(new NotFoundException(message), response);
            }
        });
    }

    @Test
    public void testHandleExceptionWithException() {

        handleExceptionTest(400, Exception.class, new HandleException() {
            @Override
            public ErrorResponse handle(String message, HttpServletResponse response) {
                return userController.handleException(new Exception(message), response);
            }
        });
    }

    public static void handleExceptionTest(int status, Class<? extends Exception> type, HandleException handler) {

        final String MESSAGE = "test error";

        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ErrorResponse errorResponse = handler.handle(MESSAGE, response);

        assertEquals("error should be correct.", type.getSimpleName(), errorResponse.getError());
        assertEquals("message should be correct.", MESSAGE, errorResponse.getMessage());

        verify(response, times(1)).setStatus(status);

        verifyNoMoreInteractions(response);
    }

    public static interface HandleException {
        public ErrorResponse handle(String message, HttpServletResponse response);
    }

    private class CreateWithUser implements WithUser {

        @Override
        public User call(User user) throws Exception {
            return userController.create(user).call();
        }
    }

    private class RetrieveWithId implements WithId {

        @Override
        public User call(Long id) throws Exception {
            return userController.retrieve(id).call();
        }
    }

    private class DeleteWithId implements WithId {

        @Override
        public User call(Long id) throws Exception {
            return userController.delete(id).call();
        }
    }

    private class UpdateWithUserAndId implements WithUserAndId {

        @Override
        public User call(Long id, User user) throws Exception {
            return userController.update(id, user).call();
        }
    }
}
