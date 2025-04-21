package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class ServerConfiguration {
    private static final String CONFIG_FILE = "server-config.json";
    private static final Logger logger = LogManager.getRootLogger();
    private static ServerConfiguration instance;

    private int port;
    private String wwwroot;

    public static ServerConfiguration getOrCreate() {
        if (instance == null) {
            instance = new ServerConfiguration();
            logger.info("Instanciada la clase ServerConfiguration, esto debe aparecer solo una vez");
        }
        return instance;
    }

    private ServerConfiguration() {
        loadFromFile();
    }

    private void loadFromFile() {
        try (InputStream in = ServerConfiguration.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (in == null) {
                logger.error("Debe colocar el archivo server-config.json en la carpeta resources");
                throw new IllegalStateException(
                        "Configuration file not found on classpath: " + CONFIG_FILE);
            }

            try (JsonReader reader = Json.createReader(in)) {
                JsonObject root = reader.readObject();

                this.port = root.getInt(ServerConfigurationKey.port.toString());
                this.wwwroot = root.getString(ServerConfigurationKey.wwwroot.toString());
            } catch (Exception e) {
                logger.error("No pudo leer un elemento de la configuracion", e);
            }

        } catch (IOException e) {
            logger.error("No puede leer la configuración", e);
            throw new UncheckedIOException("Failed to read configuration", e);
        }
    }

    /* ---------- getters ---------- */
    public int getPort() {
        return port;
    }

    public String getWwwroot() {
        return wwwroot;
    }

    @Override public String toString() {
        return "ServerConfiguration{port=" + port + '}';
    }
}
