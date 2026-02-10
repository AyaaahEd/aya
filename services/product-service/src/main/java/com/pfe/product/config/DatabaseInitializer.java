package com.pfe.product.config;

import com.pfe.product.domain.Product;
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
            System.out.println("Initializing MongoDB Indexes for Product Service...");

            mongoTemplate.indexOps(Product.class).ensureIndex(
                    new Index().on("category", Sort.Direction.ASC));

            mongoTemplate.indexOps(Product.class).ensureIndex(
                    new Index().on("name", Sort.Direction.ASC));

            System.out.println("Indexes created: category, name");
        };
    }
}
