package com.pvp.codingtournament.model.tournament;

import com.pvp.codingtournament.business.enums.Difficulty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@ApiModel(description = "Tournament data transfer object used for creating tournaments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentCreationDto {
    @ApiModelProperty(value = "Name of the tournament", example = "Best Tournament In The World")
    private String name;

    @ApiModelProperty(value = "Start date of the tournament", example = "2023-12-12")
    private LocalDate startDate;

    @ApiModelProperty(value = "End date of the tournament", example = "2023-12-28")
    private LocalDate endDate;

    @ApiModelProperty(value = "Description of the tournament", example = "Lorem ipsum...")
    private String description;

    @ApiModelProperty(value = "Prize for first place", example = "500")
    private int firstPlacePoints;

    @ApiModelProperty(value = "Prize for second place", example = "300")
    private int secondPlacePoints;

    @ApiModelProperty(value = "Prize for third place", example = "100")
    private int thirdPlacePoints;

    @ApiModelProperty(value = "Difficulty of the tournament", example = "Beginner, Intermediate, Expert")
    private Difficulty difficulty;
}
