package gogoris;

import liquibase.changelog.ChangeLogParameters;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.serializer.ChangeLogSerializerFactory;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

class ConvertCommand implements Callable<Void> {

    @CommandLine.Option(names = {"-p", "--path"}, defaultValue = ".", paramLabel = "PATH", description = "the base path of the processed files")
    String path;

    @CommandLine.Option(names = {"-i", "--input"}, paramLabel = "INPUT", description = "the input changelog (with extension)", required = true)
    String input;

    @CommandLine.Option(names = {"-o", "--output"}, paramLabel = "OUTPUT", description = "the output changelog (with extension)", required = true)
    String output;

    @Override
    public Void call() throws Exception {
        ResourceAccessor resourceAccessor = new DirectoryResourceAccessor(Path.of(path));
        var parser = ChangeLogParserFactory.getInstance()
                .getParser(FilenameUtils.getName(input), resourceAccessor);
        var changeLog = parser.parse(input, new ChangeLogParameters(), resourceAccessor);

        var serializer = ChangeLogSerializerFactory.getInstance().getSerializer(FilenameUtils.getExtension(output));
        try (var ymlOutputstream = Files.newOutputStream(Path.of(path, output))) {
            serializer.write(changeLog.getChangeSets(), ymlOutputstream);
            return null;
        }
    }
}
