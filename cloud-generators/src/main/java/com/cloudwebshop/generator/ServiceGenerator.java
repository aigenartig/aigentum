package com.cloudwebshop.generator;

import com.cloudwebshop.generator.model.ServiceDefinition;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ServiceGenerator {

    private final Configuration freemarkerCfg;

    public ServiceGenerator() {
        freemarkerCfg = new Configuration(Configuration.VERSION_2_3_32);
        freemarkerCfg.setClassForTemplateLoading(ServiceGenerator.class, "/");
        freemarkerCfg.setDefaultEncoding("UTF-8");
        freemarkerCfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerCfg.setLogTemplateExceptions(false);
        freemarkerCfg.setWrapUncheckedExceptions(true);
    }

    public void generate(ServiceDefinition service, Path outputDir) throws IOException, TemplateException {
        Template template = freemarkerCfg.getTemplate("service-interface.ftl");

        Map<String, Object> root = new HashMap<>();
        root.put("service", service);
        // Assuming a base package for now
        root.put("packageName", "com.cloudwebshop." + service.getName().toLowerCase().replace("service", "") + ".service");

        File outputFile = outputDir.resolve(service.getName() + ".java").toFile();
        outputFile.getParentFile().mkdirs();

        try (Writer writer = new FileWriter(outputFile)) {
            template.process(root, writer);
        }
        System.out.println("Generated " + outputFile.getAbsolutePath());
    }
}
