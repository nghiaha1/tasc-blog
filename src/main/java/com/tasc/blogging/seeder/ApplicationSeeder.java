package com.tasc.blogging.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSeeder implements CommandLineRunner {
    private boolean isSeeded = false;

    @Autowired
    private BlogSeeder blogSeeder;
    @Autowired
    private UserSeeder userSeeder;
    @Autowired
    private CategorySeeder categorySeeder;

    @Override
    public void run(String... args) throws Exception {
        if (isSeeded) {
            blogSeeder.seed();
            userSeeder.seed();
            categorySeeder.seed();
        }
    }
}
