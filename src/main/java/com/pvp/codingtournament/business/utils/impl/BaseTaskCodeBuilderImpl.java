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
    private ArrayList<String> imports = new ArrayList<>();

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
        String baseCode = Constants.javaTaskBaseCode;
        String className = "Solution";

        baseCode = baseCode.replaceAll("classNamePlaceholder", className + username);
        baseCode = baseCode.replaceAll("methodNamePlaceholder", methodName);
        baseCode = baseCode.replaceAll("returnTypePlaceholder", returnType);
        baseCode = baseCode.replaceAll("methodArgumentPlaceholder", buildMethodArguments(methodArguments, methodArgumentTypes));
        baseCode = baseCode.replaceAll("inputPlaceholder", buildMethodInputs(methodArgumentTypes));
        baseCode = addImportStatements(baseCode);
        baseCode = addToStringMethodIfReturnTypeArray(baseCode);

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
                case "int" -> methodInputBuilder.append(String.format("Integer.parseInt(arguments[%d]), ", i));
                case "double" -> methodInputBuilder.append(String.format("Double.parseDouble(arguments[%d]), ", i));
                case "string" -> methodInputBuilder.append(String.format("arguments[%d], ", i));
                case "int[]" -> {
                    methodInputBuilder.append(String.format("Arrays.stream(arguments[%d].replaceAll(\"\\\\\\\\[|\\\\\\\\]\", \"\").split(\", \"))" +
                                                            ".mapToInt(Integer::parseInt)" +
                                                            ".toArray(), ", i));
                    if (!imports.contains("import java.util.Arrays;"))
                        imports.add("import java.util.Arrays;");
                }
            }
        }
        String methodInputs = methodInputBuilder.toString();
        methodInputs = methodInputs.substring(0, methodInputs.length() - 2);
        return methodInputs;
    }

    private String addImportStatements(String baseCode) {
        StringBuilder baseCodeBuilder = new StringBuilder(baseCode);
        baseCodeBuilder.insert(0, "import java.util.Scanner;" + "\n");
        for (String importStatement : imports) {
            baseCodeBuilder.insert(0, importStatement + "\n");
        }
        baseCode = baseCodeBuilder.toString();
        return baseCode;
    }

    private String addToStringMethodIfReturnTypeArray(String baseCode) {
        if (returnType.contains("[]")){
            int methodNameIndex = baseCode.indexOf(methodName);
            baseCode = baseCode.substring(0, methodNameIndex) + "Arrays.toString(" + baseCode.substring(methodNameIndex);
            int soutIndex = baseCode.indexOf("System.out.println");
            int endOfSoutLineIndex = baseCode.indexOf(";", soutIndex);
            baseCode = baseCode.substring(0, endOfSoutLineIndex) + ")" + baseCode.substring(endOfSoutLineIndex);
        }
        return baseCode;
    }
}
