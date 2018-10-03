package be.aquafin;

import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.serializer.ChangeLogSerializer;
import liquibase.serializer.ChangeLogSerializerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Xml2Json {
    public static void main(String[] args) throws LiquibaseException, IOException {

        ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
        String inputFilename = args[0];
        if (args[0] == null || args[0].length() < 5) {
            System.err.println("Invalid filename");
            System.exit(0);
        }
        ChangeLogParser parser = ChangeLogParserFactory.getInstance().getParser(".xml", resourceAccessor);
        DatabaseChangeLog changeLog = parser
            .parse(inputFilename, new ChangeLogParameters(), resourceAccessor);

        ChangeLogSerializer serializer = ChangeLogSerializerFactory.getInstance().getSerializer("json");
        String outputFilename = getBasepath(inputFilename) + ".json";
        try (OutputStream ymlOutputstream = Files.newOutputStream(Paths.get(outputFilename))) {
            serializer.write(changeLog.getChangeSets(), ymlOutputstream);
        }
    }

    private static String getBasepath(String inputFilename) {
        int extensionIndex = inputFilename.lastIndexOf('.');
        return inputFilename.substring(0, extensionIndex);
    }
}