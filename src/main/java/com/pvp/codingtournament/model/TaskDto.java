package com.pvp.codingtournament.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javatuples.Tuple;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@ApiModel(description = "Task data transfer object")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    @ApiModelProperty(value = "ID of the task", hidden = true)
    private Long id;

    @ApiModelProperty(value = "Title of the task", example = "Travelling salesman")
    private String title;

    @ApiModelProperty(value = "Description of the task", example = "Lorem ipsum...")
    private String description;

    @ApiModelProperty(value = "Points given to the user if task is done", example = "5")
    private int points;

    @ApiModelProperty(value = "Collection of arrays where 0 index is the input and 1 index is corresponding output")
    private ArrayList<String[]> inputOutput;
}
