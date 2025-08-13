package com.cloudwebshop.generator.model;

import java.util.ArrayList;
import java.util.List;

public class ServiceDefinition {
    private String name;
    private List<MethodDefinition> methods = new ArrayList<>();

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<MethodDefinition> getMethods() { return methods; }
    public void setMethods(List<MethodDefinition> methods) { this.methods = methods; }
}
