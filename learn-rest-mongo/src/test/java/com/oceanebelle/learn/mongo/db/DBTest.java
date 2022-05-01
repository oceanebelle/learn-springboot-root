package com.oceanebelle.learn.mongo.db;

import eu.rekawek.toxiproxy.ToxiproxyClient;
import org.junit.ClassRule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

/**
 * Partially load the spring context required for DB.
 *  To load entire application @SpringBootTest can be used.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = DBTest.DBInitializer.class,
        classes = {DBConfig.class})
@Testcontainers
public abstract class DBTest {
    private static final String USER = "root";
    private static final String PASS = "admin";
    public static final String MONGO_SERVICE = "mongo";
    public static final int MONGO_PORT = 27017;

    public static final String TOXIPROXY_SERVICE = "toxiproxy";
    public static final int MONGO_PROXY_PORT = 27000;
    public static final int TOXIPROXY_PORT = 8474;

    static ToxiproxyClient toxiproxyClient;

    @ClassRule
    public static DockerComposeContainer env = new DockerComposeContainer(new File("src/docker/docker-compose-db.yml"))
            .withExposedService(MONGO_SERVICE, MONGO_PORT, Wait.forListeningPort())
            .withExposedService(TOXIPROXY_SERVICE, TOXIPROXY_PORT, Wait.forListeningPort())
            .withExposedService(TOXIPROXY_SERVICE, MONGO_PROXY_PORT, Wait.forListeningPort());

    static {
        env.start();
        toxiproxyClient = new ToxiproxyClient(env.getServiceHost(TOXIPROXY_SERVICE, TOXIPROXY_PORT),
                env.getServicePort(TOXIPROXY_SERVICE, TOXIPROXY_PORT));
    }

    public static class DBInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "db.servers=" + env.getServiceHost(TOXIPROXY_SERVICE, MONGO_PROXY_PORT) + ":" +
                            env.getServicePort(TOXIPROXY_SERVICE, MONGO_PROXY_PORT),
                    "db.name=" + USER,
                    "db.password=" + PASS,
                    "db.authDB=admin"
            );
        }
    }

}
