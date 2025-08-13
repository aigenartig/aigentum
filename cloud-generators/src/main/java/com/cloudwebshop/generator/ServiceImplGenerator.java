package com.cloudwebshop.generator;

import com.cloudwebshop.generator.config.GeneratorConfig;
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

public class ServiceImplGenerator {

    private final Configuration freemarkerCfg;

    public ServiceImplGenerator() {
        freemarkerCfg = new Configuration(Configuration.VERSION_2_3_32);
        freemarkerCfg.setClassForTemplateLoading(ServiceImplGenerator.class, "/");
        freemarkerCfg.setDefaultEncoding("UTF-8");
        freemarkerCfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerCfg.setLogTemplateExceptions(false);
        freemarkerCfg.setWrapUncheckedExceptions(true);
    }

    public void generate(ServiceDefinition service, Path outputDir) throws IOException, TemplateException {
        Template template = freemarkerCfg.getTemplate("service-impl.ftl");

        String serviceNameOnly = service.getName().replace("Service", "");
        String repositoryName = serviceNameOnly + "Repository";

        Map<String, Object> root = new HashMap<>();
        root.put("service", service);
        root.put("packageName", GeneratorConfig.getServiceImplPackage(serviceNameOnly));
        root.put("servicePackage", GeneratorConfig.getServicePackage(serviceNameOnly));
        root.put("repositoryPackage", GeneratorConfig.getRepositoryPackage(serviceNameOnly));
        root.put("repositoryName", repositoryName);

        File outputFile = outputDir.resolve(service.getName() + "Impl.java").toFile();
        outputFile.getParentFile().mkdirs();

        try (Writer writer = new FileWriter(outputFile)) {
            template.process(root, writer);
        }
        System.out.println("Generated " + outputFile.getAbsolutePath());
    }
}
