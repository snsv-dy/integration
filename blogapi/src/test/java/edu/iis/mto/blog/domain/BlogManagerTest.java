package edu.iis.mto.blog.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.services.BlogService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BlogManagerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BlogPostRepository postRepository;

    @MockBean
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogService blogService;

    @Captor
    private ArgumentCaptor<User> userParam;

    @Test
    void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), equalTo(AccountStatus.NEW));
    }

//    void likingPostShouldOnlyBePossibleByUserWithStatusCONFIRMED() {
    @Test
    void likingPostByUserWithStatusNEW_ShouldResultInDomainError() {
        User user = new User();
        user.setAccountStatus(AccountStatus.NEW);
        user.setEmail("email@domain.com");
        Long userId = 2L;
//        userRepository.save(user);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

//        long postid = blogService.createPost(userId, new PostRequest());
        User postAuthor = new User();
        postAuthor.setAccountStatus(AccountStatus.CONFIRMED);
        postAuthor.setEmail("email@domain.com");
        postAuthor.setId(3L);
//        userRepository.save(postAuthor);

        long postId = 4L;
        BlogPost post = new BlogPost();
        post.setUser(postAuthor);
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(java.util.Optional.of(post));

        assertThrows(DomainError.class, () -> blogService.addLikeToPost(userId, postId));
    }
}
