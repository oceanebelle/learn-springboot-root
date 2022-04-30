package com.oceanebelle.learn.mongo.db;

import dev.morphia.Datastore;
import lombok.AllArgsConstructor;
import org.bson.RawBsonDocument;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DefaultDBAdaptor implements DBAdaptor {

    private Datastore datastore;

    @Override
    public String getVersion() {
        return datastore.getDatabase().runCommand(RawBsonDocument.parse("{ buildInfo: 1 }"))
                .getString("version");
    }
}
