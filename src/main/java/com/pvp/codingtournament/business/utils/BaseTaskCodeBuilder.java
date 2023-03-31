package com.pvp.codingtournament.business.utils;

import java.util.ArrayList;

public interface BaseTaskCodeBuilder {
    public void setMethodName(String methodName);
    public void setReturnType(String returnType);
    public void setMethodArguments(ArrayList<String> methodArguments);
    public void setMethodArgumentTypes(ArrayList<String> methodArgumentTypes);
    public String build();
}
