package com.cloudwebshop.generator;

import com.cloudwebshop.generator.model.ServiceDefinition;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Starting code generation...");

            ModelParser parser = new ModelParser();
            ServiceGenerator interfaceGenerator = new ServiceGenerator();
            ServiceImplGenerator implGenerator = new ServiceImplGenerator();

            Path inputFile = Paths.get("src/main/resources/system-definition.mermaid");
            Path outputDir = Paths.get("target/generated-sources/java");

            System.out.println("Parsing definition file: " + inputFile.toAbsolutePath());
            List<ServiceDefinition> services = parser.parse(inputFile);
            System.out.println("Found " + services.size() + " service definitions.");

            for (ServiceDefinition service : services) {
                System.out.println("--- Generating for " + service.getName() + " ---");
                interfaceGenerator.generate(service, outputDir);
                implGenerator.generate(service, outputDir);
            }

            System.out.println("Code generation finished successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
