package ${packageName};

// Import statements will need to be added here.
// A more advanced generator would collect all unique types and generate imports.
import java.util.UUID;
import java.util.List;
import com.cloudwebshop.userservice.model.User; // Example, needs to be dynamic
import com.cloudwebshop.orderservice.model.Order; // Example, needs to be dynamic

public interface ${service.name} {

<#list service.methods as method>
    ${method.returnType} ${method.name}(<#list method.parameters as param>${param.type} ${param.name}<#if param?has_next>, </#if></#list>);
</#list>
}
