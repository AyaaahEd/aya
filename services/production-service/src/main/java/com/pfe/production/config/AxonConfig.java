package com.pfe.production.config;

import com.mongodb.client.MongoClient;

import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;

import org.axonframework.serialization.Serializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    public MongoTemplate axonMongoTemplate(MongoClient mongoClient) {
        return DefaultMongoTemplate.builder()
                .mongoDatabase(mongoClient)
                .build();
    }

    // TokenStore is optional and can be added later for Tracking Processors.

    @Bean
    public EventStorageEngine eventStorageEngine(MongoClient mongoClient, Serializer serializer) {
        return MongoEventStorageEngine.builder()
                .mongoTemplate(DefaultMongoTemplate.builder()
                        .mongoDatabase(mongoClient)
                        .build())
                .eventSerializer(serializer)
                .snapshotSerializer(serializer)
                .build();
    }
}
