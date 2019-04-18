Liquibase Changelog Converter
=============================

A little utility that can be used to convert a liquibase changelog from one
form to another.

Based on https://github.com/GoGoris/liquibase-changelog-converter

Simply build:

    mvn clean install
    
And then you can use the generated JAR file

    java -jar target/liquibase-changelog-converter-1.0-SNAPSHOT.jar --input input.json --output output.yaml
    
Hope it helps
