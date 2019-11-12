package gogoris;

import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.serializer.ChangeLogSerializer;
import liquibase.serializer.ChangeLogSerializerFactory;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class ConvertCommand implements Callable<Void> {

    @CommandLine.Option(names = {"-i", "--input"}, paramLabel = "INPUT", description = "the input changelog (with extension)",required = true)
    String input;

    @CommandLine.Option(names = {"-o", "--output"}, paramLabel = "OUTPUT", description = "the output changelog (with extension)", required = true)
    String output;

    @Override
    public Void call() throws Exception {
        ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
        ChangeLogParser parser = null;
        try {
            parser = ChangeLogParserFactory.getInstance().getParser(FilenameUtils.getName(input), resourceAccessor);
            DatabaseChangeLog changeLog = parser
                    .parse(input, new ChangeLogParameters(), resourceAccessor);

            ChangeLogSerializer serializer = ChangeLogSerializerFactory.getInstance().getSerializer(FilenameUtils.getExtension(output));
            try (OutputStream ymlOutputstream = Files.newOutputStream(Paths.get(output))) {
                serializer.write(changeLog.getChangeSets(), ymlOutputstream);
            } catch (IOException e) {
                throw new RuntimeException("Unable to write output file " + output, e);
            }
        } catch (LiquibaseException e) {
            throw new RuntimeException("Unable to process liquibase file " + input, e);
        }return null;
    }
}
