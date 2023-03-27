package com.pvp.codingtournament.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "User submitted code analysis data object")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResults {
    @ApiModelProperty(value = "Boolean value which determines if the user passed the task", example = "true")
    private Boolean passed;
    @ApiModelProperty(value = "Total test cases of the task", example = "5")
    private int totalTestCases;
    @ApiModelProperty(value = "Amount of passed test cases of the task", example = "3")
    private int passedTestCases;
}
