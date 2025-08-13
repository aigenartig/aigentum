package com.cloudwebshop.generator;

import com.cloudwebshop.generator.model.MethodDefinition;
import com.cloudwebshop.generator.model.ParameterDefinition;
import com.cloudwebshop.generator.model.ServiceDefinition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelParser {

    public List<ServiceDefinition> parse(Path mermaidFile) throws IOException {
        List<ServiceDefinition> services = new ArrayList<>();
        List<String> lines = Files.readAllLines(mermaidFile);

        ServiceDefinition currentService = null;
        boolean inServiceBlock = false;

        Pattern servicePattern = Pattern.compile("class\\s+(\\w+Service)\\s+\\{");
        Pattern methodPattern = Pattern.compile("\\+([\\w~<>]+)\\s+(\\w+)\\((.*)\\)");

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith("class") && trimmedLine.contains("Service")) {
                Matcher serviceMatcher = servicePattern.matcher(trimmedLine);
                if (serviceMatcher.find()) {
                    currentService = new ServiceDefinition();
                    currentService.setName(serviceMatcher.group(1));
                    inServiceBlock = true;
                    continue;
                }
            }

            if (inServiceBlock) {
                if (trimmedLine.equals("}")) {
                    if (currentService != null) {
                        services.add(currentService);
                    }
                    currentService = null;
                    inServiceBlock = false;
                    continue;
                }

                if (trimmedLine.startsWith("<<Interface>>")) {
                    continue; // Skip this line
                }

                Matcher methodMatcher = methodPattern.matcher(trimmedLine);
                if (methodMatcher.find()) {
                    MethodDefinition method = new MethodDefinition();
                    String returnTypeRaw = methodMatcher.group(1);
                    String returnType = returnTypeRaw.replace("~", "<");
                    if (returnTypeRaw.contains("~")) {
                        returnType += ">";
                    }
                    method.setReturnType(returnType);
                    method.setName(methodMatcher.group(2));

                    String paramsString = methodMatcher.group(3);
                    if (!paramsString.isEmpty()) {
                        String[] params = paramsString.split(",");
                        for (String param : params) {
                            String[] parts = param.trim().split("\\s+");
                            ParameterDefinition p = new ParameterDefinition();
                            p.setType(parts[0]);
                            p.setName(parts[1]);
                            method.getParameters().add(p);
                        }
                    }
                    if (currentService != null) {
                        currentService.getMethods().add(method);
                    }
                }
            }
        }
        return services;
    }
}
