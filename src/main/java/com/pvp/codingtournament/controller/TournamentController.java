package com.pvp.codingtournament.controller;

import com.pvp.codingtournament.business.service.TournamentService;
import com.pvp.codingtournament.model.tournament.TournamentCreationDto;
import com.pvp.codingtournament.model.tournament.TournamentDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/tournament")
@RestController
public class TournamentController {
    private final TournamentService tournamentService;

    @ApiOperation("Gets all tournaments")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request successful")
    })
    @GetMapping("/get/all")
    public ResponseEntity<List<TournamentDto>> getAllTournaments(){
        return ResponseEntity.ok(tournamentService.findAllTournaments());
    }

    @PostMapping("/create")
    public ResponseEntity<TournamentDto> createTournament(@RequestBody TournamentCreationDto tournamentCreationDto, @RequestParam("tasks") List<Long> taskIds){
        return ResponseEntity.ok(tournamentService.createTournament(tournamentCreationDto, taskIds));
    }
}
