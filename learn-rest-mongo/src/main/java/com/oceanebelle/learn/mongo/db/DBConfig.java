package com.oceanebelle.learn.mongo.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.oceanebelle.learn.mongo.db.model.Person;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
public class DBConfig {

    @Bean
    Datastore datastore(DBProperties dbProperties) {
        MongoClient client = MongoClients.create(mongoClientSettings(dbProperties));
        Datastore datastore = Morphia.createDatastore(client, dbProperties.getName());

        datastore.getMapper().mapPackageFromClass(Person.class);

        datastore.ensureIndexes();

        return datastore;
    }

    private MongoClientSettings mongoClientSettings(DBProperties dbProperties) {
        ConnectionString connectionString = new ConnectionString(
                String.format("mongodb://%s/", dbProperties.getServers()));

        MongoCredential credential = MongoCredential.createCredential(
                dbProperties.getName(),
                dbProperties.getAuthDB(),
                dbProperties.getPassword().toCharArray());

        return MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(credential)
                .build();
    }
}
