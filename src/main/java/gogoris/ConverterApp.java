package gogoris;

import picocli.CommandLine;

public class ConverterApp {

    public static void main(String[] args) {
        var cmd = new ConvertCommand();
        new CommandLine(cmd).execute(args);
    }

}
