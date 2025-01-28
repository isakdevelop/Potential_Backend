package com.potential.api.common.component;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class ContextBuilderComponent {
    private final Map<String, Object> variables = new HashMap<>();

    public ContextBuilderComponent setVariable(String key, Object value) {
        variables.put(key, value);
        return this;
    }

    public Context build() {
        Context context = new Context();
        context.setVariables(variables);
        return context;
    }
}
