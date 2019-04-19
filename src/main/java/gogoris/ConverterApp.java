package gogoris;

import picocli.CommandLine;

public class ConverterApp {

    public static void main(String[] args) throws Exception {
        CommandLine.call(new ConvertCommand(), args);
    }

}
