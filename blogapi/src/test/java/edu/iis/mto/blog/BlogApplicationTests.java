package edu.iis.mto.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan("edu.iis.mto.blog.domain")
class BlogApplicationTests {

    @Test
    void contextLoads() {}

}
