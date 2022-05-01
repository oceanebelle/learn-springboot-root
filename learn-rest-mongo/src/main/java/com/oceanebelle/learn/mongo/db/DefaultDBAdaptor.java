package com.oceanebelle.learn.mongo.db;

import com.oceanebelle.learn.logging.LogMessageFactory;
import dev.morphia.Datastore;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.RawBsonDocument;

@AllArgsConstructor
@Log4j2
public class DefaultDBAdaptor implements DBAdaptor {

    private static final String DB = "DB";
    private Datastore datastore;

    @Override
    public String getVersion() {
        log.info(LogMessageFactory.startAction(DB).method().kv("message", "Fetching Version"));
        try {
            String version = datastore.getDatabase().runCommand(RawBsonDocument.parse("{ buildInfo: 1 }"))
                    .getString("version");
            log.info(LogMessageFactory.endAction(DB).method().kv("version", version));
            return version;
        } catch (RuntimeException e) {
            log.error(LogMessageFactory.failAction(DB).method().message(e.getMessage()), e);
            throw e;
        }
    }
}
