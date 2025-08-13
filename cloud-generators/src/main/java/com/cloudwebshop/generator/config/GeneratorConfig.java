package com.cloudwebshop.generator.config;

public class GeneratorConfig {

    public static final String BASE_PACKAGE = "com.cloudwebshop";

    public static String getModelPackage(String serviceName) {
        return BASE_PACKAGE + "." + serviceName.toLowerCase() + ".model";
    }

    public static String getRepositoryPackage(String serviceName) {
        return BASE_PACKAGE + "." + serviceName.toLowerCase() + ".repository";
    }

    public static String getServicePackage(String serviceName) {
        return BASE_PACKAGE + "." + serviceName.toLowerCase() + ".service";
    }

    public static String getServiceImplPackage(String serviceName) {
        return getServicePackage(serviceName); // Typically in the same package
    }
}
