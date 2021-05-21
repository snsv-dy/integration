package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }

    @Test
    void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();


        assertThat(users, hasSize(1));
        assertThat(users.get(0)
                        .getEmail(),
                equalTo(persistedUser.getEmail()));
    }

    @Test
    void searchingUserByFirstNameThatExistsInDatabase_shouldResultInSucces(){
        User persistedUser = repository.save(user);
        String firstName = "Jan";
        List<User> results = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName, "", "");

        assertEquals(persistedUser, results.get(0));
    }

    @Test
    void searchingUserByLastNameThatExistsInDatabase_shouldResultInSucces(){
        String surname = "Jagiełło";
        user.setLastName(surname);
        User persistedUser = repository.save(user);
        List<User> results = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", surname, "");

        assertEquals(persistedUser, results.get(0));
    }

    @Test
    void searchingUserByEmailThatExistsInDatabase_shouldResultInSucces(){
        User persistedUser = repository.save(user);
        String email = "john@domain.com";
        List<User> results = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("", "", email);

        assertEquals(persistedUser, results.get(0));
    }


    @Test
    void searchingUserNameThatDoesNotExistsInDatabase_shouldResultInFailure(){
        User persistedUser = repository.save(user);
        String firstName = "Kacper";
        List<User> results = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName, "notnull", "notnull");
        System.out.println(results.isEmpty() + ", " + results.size());

        assertThat("result list should be empty", results.isEmpty());
    }
}
