package com.pvp.codingtournament.business.utils;

import com.pvp.codingtournament.model.AnalysisResults;

import java.io.IOException;
import java.util.ArrayList;

public interface CodeRunner {
    public void setCode(String code);
    public void setInputsAndOutputs(ArrayList<String[]> inputsAndOutputs);
    public AnalysisResults runCode() throws IOException, InterruptedException;
}
