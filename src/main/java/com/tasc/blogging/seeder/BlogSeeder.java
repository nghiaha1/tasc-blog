package com.tasc.blogging.seeder;

import com.tasc.blogging.entity.blog.Blog;
import com.tasc.blogging.repository.BlogRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class BlogSeeder {
    @Autowired
    private BlogRepository blogRepository;

    private final static int ITEM_COUNT = 1000;

    private Faker faker = new Faker();

    public void seed() {
        List<Blog> blogs = new LinkedList<>();
        for (int i = 0; i < ITEM_COUNT; i++) {
            Blog blog = Blog.builder()
                    .title(faker.name().title())
                    .content(faker.lorem().paragraph())
                    .build();
            blogs.add(blog);
        }
        blogRepository.saveAll(blogs);
    }
}
