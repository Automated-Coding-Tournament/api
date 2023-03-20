package com.pvp.codingtournament.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "User data transfer object for editing user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDto {
    @ApiModelProperty(value = "Name of the user", example = "John")
    private String name;
    @ApiModelProperty(value = "Surname of the user", example = "Doe")
    private String surname;
    @ApiModelProperty(value = "Email of the user", example = "JohnDoe@gmail.com")
    private String email;
    @ApiModelProperty(value = "Phone number of the user", example = "+398 658 12356")
    private String phoneNumber;
}
