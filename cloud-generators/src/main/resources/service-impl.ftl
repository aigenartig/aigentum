package ${packageName};

import ${repositoryPackage}.${repositoryName};
import ${servicePackage}.${service.name};
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Other imports will be needed for method implementations
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ${service.name}Impl implements ${service.name} {

    private final ${repositoryName} ${repositoryName?uncap_first};

<#list service.methods as method>
    @Override
    public ${method.returnType} ${method.name}(<#list method.parameters as param>${param.type} ${param.name}<#if param?has_next>, </#if></#list>) {
        // TODO: Implement method logic
        <#if method.returnType != "void">
        return null;
        </#if>
    }
</#list>
}
