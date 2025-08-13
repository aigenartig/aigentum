package com.cloudwebshop.generator.model;

import java.util.ArrayList;
import java.util.List;

public class MethodDefinition {
    private String returnType;
    private String name;
    private List<ParameterDefinition> parameters = new ArrayList<>();

    // Getters and Setters
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<ParameterDefinition> getParameters() { return parameters; }
    public void setParameters(List<ParameterDefinition> parameters) { this.parameters = parameters; }
}
