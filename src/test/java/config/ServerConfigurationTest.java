package config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerConfigurationTest {

    @Test
    void readFromFileTest() {
        ServerConfiguration cfg = ServerConfiguration.getOrCreate();

        String result = cfg.toString();

        String expected = "ServerConfiguration{port=8023}";

        assertEquals(expected, result);
    }
}
