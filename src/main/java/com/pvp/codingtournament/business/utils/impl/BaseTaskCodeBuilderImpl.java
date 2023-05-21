package com.pvp.codingtournament.business.utils.impl;

import com.pvp.codingtournament.business.Constants;
import com.pvp.codingtournament.business.utils.BaseTaskCodeBuilder;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BaseTaskCodeBuilderImpl implements BaseTaskCodeBuilder {
    private final Map<String, String> javaVariableTypes;
    private final Map<String, String> cSharpVariableTypes;
    private String methodName = "methodNamePlaceholder";
    private String returnType = "returnTypePlaceholder";
    private String language;
    private ArrayList<String> methodArguments = new ArrayList<>();
    private ArrayList<String> methodArgumentTypes = new ArrayList<>();
    private ArrayList<String> imports = new ArrayList<>();

    public BaseTaskCodeBuilderImpl(){
        javaVariableTypes = new HashMap<>();
        javaVariableTypes.put("int", "int");
        javaVariableTypes.put("double", "double");
        javaVariableTypes.put("string", "String");
        javaVariableTypes.put("int[]", "int[]");
        javaVariableTypes.put("double[]", "double[]");
        javaVariableTypes.put("string[]", "String[]");

        cSharpVariableTypes = new HashMap<>();
        cSharpVariableTypes.put("int", "int");
        cSharpVariableTypes.put("double", "double");
        cSharpVariableTypes.put("string", "string");
        cSharpVariableTypes.put("int[]", "int[]");
        cSharpVariableTypes.put("double[]", "double[]");
        cSharpVariableTypes.put("string[]", "string[]");
    }

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
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String build() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String baseCode = switch (language) {
            case "java" -> Constants.javaTaskBaseCode;
            case "python" -> Constants.pythonTaskBaseCode;
            default -> "";
        };

        String className = "Solution";
        adaptMethodArgumentTypesToLanguage();
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
        String arguments = "";
        if (!language.equals("python")){
            for (int i = 0; i < methodArguments.size(); i++) {
                methodArgumentBuilder.append(String.format("%s %s, ", methodArgumentTypes.get(i), methodArguments.get(i)));
            }
            arguments = methodArgumentBuilder.toString();
            arguments = arguments.substring(0, arguments.length() - 2);
        } else if (language.equals("python")){
            for (int i = 0; i < methodArguments.size(); i++) {
                methodArgumentBuilder.append(String.format("%s, ", methodArguments.get(i)));
            }
            arguments = methodArgumentBuilder.toString();
            arguments = arguments.substring(0, arguments.length() - 2);
        }
        return arguments;
    }

    private void adaptMethodArgumentTypesToLanguage() {
        switch (language) {
            case "java" ->{
                methodArgumentTypes = (ArrayList<String>) methodArgumentTypes.stream().map(type -> javaVariableTypes.get(type.toLowerCase())).collect(Collectors.toList());
                returnType = javaVariableTypes.get(returnType.toLowerCase());
            }
            case "c#" ->{
                methodArgumentTypes = (ArrayList<String>) methodArgumentTypes.stream().map(type -> cSharpVariableTypes.get(type.toLowerCase())).collect(Collectors.toList());
                returnType = cSharpVariableTypes.get(returnType.toLowerCase());
            }
        }
    }

    private String buildMethodInputs(ArrayList<String> methodInput) {
        String methodInputs = "";
        switch (language){
            case "java":
                methodInputs = buildJavaMethodInputs(methodInput);
                break;
            case "python":
                methodInputs = buildPythonMethodInputs(methodInput);
        }
        methodInputs = methodInputs.substring(0, methodInputs.length() - 2);
        return methodInputs;
    }

    private String buildPythonMethodInputs(ArrayList<String> methodInput) {
        StringBuilder methodInputBuilder = new StringBuilder();
        for (int i = 0; i < methodInput.size(); i++) {
            String inputType = methodInput.get(i);
            switch (inputType.toLowerCase()) {
                case "int" -> methodInputBuilder.append(String.format("int(arguments[%d]), ", i));
                case "double" -> methodInputBuilder.append(String.format("float(arguments[%d]), ", i));
                case "string" -> methodInputBuilder.append(String.format("arguments[%d], ", i));
                case "int[]", "double[]", "string[]" -> {
                    methodInputBuilder.append(String.format("json.loads(arguments[%d]), ", i));
                    if (!imports.contains("import json"))
                        imports.add("import json");
                }
            }
        }
        return methodInputBuilder.toString();
    }

    private String buildJavaMethodInputs(ArrayList<String> methodInput) {
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
                case "double[]" -> {
                    methodInputBuilder.append(String.format("Arrays.stream(arguments[%d].replaceAll(\"\\\\\\\\[|\\\\\\\\]\", \"\").split(\", \"))" +
                            ".mapToDouble(Double::parseDouble)" +
                            ".toArray(), ", i));
                    if (!imports.contains("import java.util.Arrays;"))
                        imports.add("import java.util.Arrays;");
                }
                case "string[]" -> {
                    methodInputBuilder.append(String.format("arguments[%d].replaceAll(\"\\\\\\\\[|\\\\\\\\]\", \"\").split(\", \"), ", i));
                    if (!imports.contains("import java.util.Arrays;"))
                        imports.add("import java.util.Arrays;");
                }
            }
        }
        return methodInputBuilder.toString();
    }

    private String addImportStatements(String baseCode) {
        StringBuilder baseCodeBuilder = new StringBuilder(baseCode);
        switch (language){
            case "java":
                baseCodeBuilder.insert(0, "import java.util.Scanner;" + "\n");
                break;
            case "python":
                baseCodeBuilder.insert(0, "import sys" + "\n");
        }
        for (String importStatement : imports) {
            baseCodeBuilder.insert(0, importStatement + "\n");
        }
        baseCode = baseCodeBuilder.toString();
        return baseCode;
    }

    private String addToStringMethodIfReturnTypeArray(String baseCode) {
        if (returnType.contains("[]") && !language.equals("python")){
            int methodNameIndex = baseCode.indexOf(methodName);
            baseCode = baseCode.substring(0, methodNameIndex) + "Arrays.toString(" + baseCode.substring(methodNameIndex);
            int soutIndex = baseCode.indexOf("System.out.println");
            int endOfSoutLineIndex = baseCode.indexOf(";", soutIndex);
            baseCode = baseCode.substring(0, endOfSoutLineIndex) + ")" + baseCode.substring(endOfSoutLineIndex);
        }
        return baseCode;
    }
}
