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
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/get/{id}")
    public ResponseEntity<TournamentDto> getTournamentById(@PathVariable("id") Long tournamentId){
        return ResponseEntity.ok(tournamentService.getTournamentById(tournamentId));
    }

    @PostMapping("/create")
    public ResponseEntity<TournamentDto> createTournament(@RequestBody TournamentCreationDto tournamentCreationDto, @RequestParam("tasks") List<Long> taskIds){
        return ResponseEntity.ok(tournamentService.createTournament(tournamentCreationDto, taskIds));
    }

    @PostMapping("/register/{id}")
    public ResponseEntity<String> registerUserToTournament(@PathVariable("id") Long tournamentId){
        tournamentService.registerUserToTournament(tournamentId);
        return ResponseEntity.ok("Registration to tournament successful!");
    }

    @PostMapping("/finish/{id}")
    public ResponseEntity<String> finishUserParticipationInTournament(@PathVariable("id") Long tournamentId){
        tournamentService.finishUserParticipationInTournament(tournamentId);
        return ResponseEntity.ok("You have successfully cashed out your points!");
    }

    @PostMapping("/deduce/points/{id}")
    public ResponseEntity<Integer> deduceUserParticipationPoints(@PathVariable("id") Long tournamentId){
        return ResponseEntity.ok(tournamentService.deduceUserParticipationPoints(tournamentId));
    }
}
