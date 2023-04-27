package com.pvp.codingtournament.business.utils;

import java.util.ArrayList;

public interface BaseTaskCodeBuilder {
    void setMethodName(String methodName);
    void setReturnType(String returnType);
    void setMethodArguments(ArrayList<String> methodArguments);
    void setMethodArgumentTypes(ArrayList<String> methodArgumentTypes);
    void setLanguage(String language);
    String build();
}
