package com.pvp.codingtournament.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pvp.codingtournament.business.enums.RoleEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "User data transfer object")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @ApiModelProperty(value = "ID of the user", hidden = true)
    private Long id;
    @ApiModelProperty(value = "Name of the user", example = "John")
    private String name;
    @ApiModelProperty(value = "Surname of the user", example = "Doe")
    private String surname;
    @ApiModelProperty(value = "Username of the user", example = "LoremIpsum")
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(value = "Password of the user", example = "MostSecurePasswordInTheWorld")
    String password;
    @ApiModelProperty(value = "Email of the user", example = "JohnDoe@gmail.com")
    private String email;
    @ApiModelProperty(value = "Phone number of the user", example = "+398 658 12356")
    private String phoneNumber;
    @ApiModelProperty(value = "Current held points of the user", required = false, notes = "Not needed for user creation")
    private int points;
    @ApiModelProperty(value = "Level of the user", required = false, notes = "Not needed for user creation")
    private String level;
}
