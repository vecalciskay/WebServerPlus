package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.services.*;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClientServiceBuilder {
    private static final Logger logger = LogManager.getRootLogger();

    public static ClientService buildService(int id, List<String> inputLines, OutputStream output) {
        String httpCommand = inputLines.getFirst();
        String pathRelativo = "";
        String operation = "";
        String httpVersion = "";
        String operationCommand;
        String operationArguments;

        String regex =  "^(GET|POST) (.+) HTTP\\/([0-9]\\.[0-9])$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(httpCommand);
        if (matcher.find()) {
            operation = matcher.group(1);
            pathRelativo = matcher.group(2);
            httpVersion = matcher.group(3);
        }

        logger.info("ServiceBuilder acaba de reconocer operacion: {}, path: {}, version: {}",
                operation, pathRelativo, httpVersion);

        String possibleOperations = Arrays.stream(WebServerOperation.values())
                .map(Enum::name)          // or e -> e.toString()
                .collect(Collectors.joining("|"));
        String regexPath = "/__op__/(" + possibleOperations + ")/(.+)";
        pattern = Pattern.compile(regexPath);
        matcher = pattern.matcher(pathRelativo);
        if (!matcher.find()) {
            return new HttpServiceClient(id, operation, pathRelativo, inputLines, output);
        }
        operationCommand = matcher.group(1);
        operationArguments = matcher.group(2);

        logger.info("ServiceBuilder Se ha reconocido el path como una operación especial op: {}, args: {}",
                operationCommand, operationArguments);

        if (operationCommand.equals(WebServerOperation.hanoi.toString()))
            return new HanoiService(id, inputLines, output);

        if (operationCommand.equals(WebServerOperation.images.toString()))
            return new ImageService(id, inputLines, output);

        if (operationCommand.equals(WebServerOperation.complex.toString()))
            return new ComplexService(id, inputLines, output);

        return new NoService(id, inputLines, output);
    }
}
