package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LikePostRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LikePostRepository repository;

    private BlogPost post;
    private User user;
    private LikePost likepost;
    private User postAuthor;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setFirstName("Jacek");
        user.setLastName("Stasiak");
        user.setEmail("email@domain.com");
        user.setAccountStatus(AccountStatus.CONFIRMED);

        postAuthor = new User();
        postAuthor.setEmail("author@domain.com");
        postAuthor.setAccountStatus(AccountStatus.CONFIRMED);

        post = new BlogPost();
        post.setUser(postAuthor);
        post.setEntry("Not important");

        likepost = new LikePost();
        likepost.setPost(post);
        likepost.setUser(user);

        User persistedUser = entityManager.persist(user);
        User persistedAuthor = entityManager.persist(postAuthor);
        BlogPost persistedPost = entityManager.persist(post);
    }

    @Test
    public void createLikePostWithAssignedUserAndPost() {
        LikePost like = repository.save(likepost);

        List<LikePost> likes = repository.findAll();

        assertEquals(like, likes.get(0));
    }

    @Test
    public void findLikeByUserAndPost_likeExists_AndShouldBeFound(){
        LikePost like = repository.save(likepost);

        Optional<LikePost> foundLike = repository.findByUserAndPost(user, post);

        assertTrue(foundLike.isPresent());
        assertEquals(like, foundLike.get());
    }

    @Test
    public void findLikeByUserAndPost_likeDoesNotExist_andShouldntBeFound(){
        Optional<LikePost> foundLike = repository.findByUserAndPost(user, post);

        assertFalse(foundLike.isPresent());
    }


//    @Test
//    public void modifyPostInLikePostEntity() {
//        LikePost like = repository.save(likepost);
//
//        User newPostUser = new User();
//        newPostUser.setEmail("coolio@domain.com");
//        newPostUser.setAccountStatus(AccountStatus.CONFIRMED);
//        User persistedNewPostUser = entityManager.persist(newPostUser);
//
//        BlogPost newPost = new BlogPost();
//        newPost.setUser(newPostUser);
//        newPost.setEntry("not significant");
//        BlogPost persistedNewPost = entityManager.persist(newPost);
//
//        LikePost oldLike = repository.findAll().get(0);
//        likepost.setPost(newPost);
//        LikePost newLike = repository.save(likepost);
//
//        assertEquals(persistedNewPost, newLike.getPost());
//        assertTrue(post.getLikes().isEmpty());
//    }
}
