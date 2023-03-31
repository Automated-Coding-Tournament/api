package com.pvp.codingtournament.business.utils.impl;

import com.pvp.codingtournament.business.Constants;
import com.pvp.codingtournament.business.utils.BaseTaskCodeBuilder;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

public class BaseTaskCodeBuilderImpl implements BaseTaskCodeBuilder {
    private String methodName = "methodNamePlaceholder";
    private String returnType = "returnTypePlaceholder";
    private ArrayList<String> methodArguments = new ArrayList<>();
    private ArrayList<String> methodArgumentTypes = new ArrayList<>();

    @Override
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public void setMethodArguments(ArrayList<String> methodArguments) {
        this.methodArguments = methodArguments;
    }

    @Override
    public void setMethodArgumentTypes(ArrayList<String> methodArgumentTypes) {
        this.methodArgumentTypes = methodArgumentTypes;
    }

    @Override
    public String build() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String baseCode = Constants.taskBaseCode;
        String className = "Solution";
        baseCode = baseCode.replaceAll("classNamePlaceholder", className + username);
        baseCode = baseCode.replaceAll("methodNamePlaceholder", methodName);
        baseCode = baseCode.replaceAll("returnTypePlaceholder", returnType);

        baseCode = baseCode.replaceAll("methodArgumentPlaceholder", buildMethodArguments(methodArguments, methodArgumentTypes));
        baseCode = baseCode.replaceAll("inputPlaceholder", buildMethodInputs(methodArgumentTypes));
        return baseCode;
    }

    private String buildMethodArguments(ArrayList<String> methodArguments, ArrayList<String> methodArgumentTypes) {
        StringBuilder methodArgumentBuilder = new StringBuilder();
        for (int i = 0; i < methodArguments.size(); i++) {
            methodArgumentBuilder.append(String.format("%s %s, ", methodArgumentTypes.get(i), methodArguments.get(i)));
        }
        String arguments = methodArgumentBuilder.toString();
        arguments = arguments.substring(0, arguments.length() - 2);
        return arguments;
    }

    private String buildMethodInputs(ArrayList<String> methodInput) {
        StringBuilder methodInputBuilder = new StringBuilder();
        for (int i = 0; i < methodInput.size(); i++) {
            String inputType = methodInput.get(i);
            switch (inputType.toLowerCase()) {
                case "int" -> methodInputBuilder.append(String.format("Integer.parseInt(args[%d]), ", i));
                case "double" -> methodInputBuilder.append(String.format("Double.parseDouble(args[%d]), ", i));
                case "string" -> methodInputBuilder.append(String.format("args[%d], ", i));
            }
        }
        String methodInputs = methodInputBuilder.toString();
        methodInputs = methodInputs.substring(0, methodInputs.length() - 2);
        return methodInputs;
    }
}
