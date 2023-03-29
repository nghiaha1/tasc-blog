package com.tasc.blogging.seeder;

import com.tasc.blogging.entity.blog.Category;
import com.tasc.blogging.entity.enums.BaseStatus;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.repository.CategoryRepository;
import com.tasc.blogging.util.Constant;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class CategorySeeder {
    @Autowired
    private CategoryRepository categoryRepository;
    private final static int ITEM_COUNT = 10;

    private Faker faker = new Faker();

    public void seed() {
        List<Category> categories = new LinkedList<>();
        for (int i = 0; i < ITEM_COUNT; i++) {
            Category category = Category.builder()
                    .title(faker.name().title())
                    .description(faker.lorem().paragraph())
                    .isRoot(Constant.ONOFF.ON)
                    .status(BaseStatus.ACTIVE)
                    .build();
            categories.add(category);
        }
        categoryRepository.saveAll(categories);
    }
}
