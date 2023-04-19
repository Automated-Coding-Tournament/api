package com.pvp.codingtournament.model;

import com.pvp.codingtournament.business.enums.Difficulty;
import com.pvp.codingtournament.business.enums.TournamentStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@ApiModel(description = "Tournament data transfer object")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDto {
    @ApiModelProperty(value = "ID of the tournament", hidden = true)
    private Long id;

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

    @ApiModelProperty(value = "Status of the tournament", example = "Registration, Started, Ended")
    private TournamentStatus status;

    @ApiModelProperty(value = "User, who created the tournament")
    private UserDto creatorUser;
}
