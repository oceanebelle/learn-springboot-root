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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration @ComponentScan
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@EnableConfigurationProperties(DBProperties.class)
public class DBConfig {
    @Bean DBAdaptor dbAdaptor(Datastore datastore) {
        return new DefaultDBAdaptor(datastore);
    }

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
