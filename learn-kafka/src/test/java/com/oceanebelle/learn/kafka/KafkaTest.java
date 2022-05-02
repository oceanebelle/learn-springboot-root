package com.oceanebelle.learn.kafka;

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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = KafkaTest.DockerInitializer.class,
        classes = {KafkaConfig.class})
@Testcontainers
public abstract class KafkaTest {

    public static final String TOXIPROXY_SERVICE = "toxiproxy";
    public static final int TOXIPROXY_PORT = 8474;
    public static final int TOXIPROXY_KAFKA_PORT = 9090;
    public static final int TOXIPROXY_ZOOKEEPER_PORT = 2180;

    public static final String KAFKA_SERVICE = "broker";
    public static final int KAFKA_BROKER_PORT = 29092;

    protected static final ToxiproxyClient toxiproxyClient;

    @ClassRule
    public static DockerComposeContainer env = new DockerComposeContainer(new File("src/docker/docker-compose-kafka.yml"))
            .withExposedService(KAFKA_SERVICE, KAFKA_BROKER_PORT, Wait.forListeningPort())
            .withExposedService(TOXIPROXY_SERVICE, TOXIPROXY_PORT, Wait.forListeningPort())
            .withExposedService(TOXIPROXY_SERVICE, TOXIPROXY_KAFKA_PORT, Wait.forListeningPort())
            .withExposedService(TOXIPROXY_SERVICE, TOXIPROXY_ZOOKEEPER_PORT, Wait.forListeningPort());

    static {
        env.start();
        toxiproxyClient = new ToxiproxyClient(env.getServiceHost(TOXIPROXY_SERVICE, TOXIPROXY_PORT),
                env.getServicePort(TOXIPROXY_SERVICE, TOXIPROXY_PORT));
    }

    public static class DockerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.kafka.broker=" + getHostPost(KAFKA_SERVICE, KAFKA_BROKER_PORT)
            );
        }
    }

    public static String getHostPost(String service, int servicePort) {
        return env.getServiceHost(service, servicePort) + ":" + env.getServicePort(service, servicePort);
    }

    public static String getKafkaBroker() {
        return getHostPost(KAFKA_SERVICE, KAFKA_BROKER_PORT);
    }
}
