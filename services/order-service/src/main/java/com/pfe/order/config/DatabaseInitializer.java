package com.pfe.order.config;

import com.pfe.order.domain.Order;
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
            System.out.println("Initializing MongoDB Indexes for Order Service...");

            mongoTemplate.indexOps(Order.class).ensureIndex(
                    new Index().on("userId", Sort.Direction.ASC));

            mongoTemplate.indexOps(Order.class).ensureIndex(
                    new Index().on("status", Sort.Direction.ASC));

            System.out.println("Indexes created: userId, status");
        };
    }
}
