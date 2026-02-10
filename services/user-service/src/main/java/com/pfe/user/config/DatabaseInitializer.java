package com.pfe.user.config;

import com.pfe.user.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initIndexes(MongoTemplate mongoTemplate) {
        return args -> {
            System.out.println("Initializing MongoDB Indexes for User Service...");

            mongoTemplate.indexOps(User.class).ensureIndex(
                    new Index().on("username", Sort.Direction.ASC).unique());

            mongoTemplate.indexOps(User.class).ensureIndex(
                    new Index().on("email", Sort.Direction.ASC).unique());

            System.out.println("Indexes created: username (unique), email (unique)");
        };
    }
}
