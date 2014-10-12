package scratch.spring.webapp.data;

public interface UserTestTemplate {

    public void I_can_create_a_user() throws Exception;

    public void I_cannot_create_the_same_user_twice() throws Exception;

    public void I_cannot_create_two_users_with_the_same_email() throws Exception;

    public void I_can_create_two_users_with_the_same_first_name() throws Exception;

    public void I_can_create_two_users_with_the_same_last_name() throws Exception;

    public void I_cannot_create_a_user_with_a_null_email() throws Exception;

    public void I_cannot_create_a_user_with_a_null_first_name() throws Exception;

    public void I_cannot_create_a_user_with_a_null_last_name() throws Exception;

    public void I_can_retrieve_a_user() throws Exception;

    public void I_cannot_retrieve_a_user_with_an_invalid_id() throws Exception;

    public void I_cannot_retrieve_a_user_with_a_null_id() throws Exception;

    public void I_can_retrieve_all_the_persisted_users() throws Exception;

    public void I_can_update_a_user() throws Exception;

    public void I_cannot_update_a_user_to_be_equal_to_an_existing_user() throws Exception;

    public void I_cannot_update_a_user_to_have_the_same_email_as_an_existing_user() throws Exception;

    public void I_can_update_a_user_to_have_the_same_first_name_as_an_existing_user() throws Exception;

    public void I_can_update_a_user_to_have_the_same_last_name_as_an_existing_user() throws Exception;

    public void I_cannot_update_a_user_with_a_null_id() throws Exception;

    public void I_cannot_update_a_user_with_a_null_email() throws Exception;

    public void I_cannot_update_a_user_with_a_null_first_name() throws Exception;

    public void I_cannot_update_a_user_with_a_null_last_name() throws Exception;

    public void I_can_delete_a_user() throws Exception;

    public void I_cannot_delete_a_user_with_an_invalid_id() throws Exception;

    public void I_cannot_delete_a_user_with_a_null_id() throws Exception;
}
