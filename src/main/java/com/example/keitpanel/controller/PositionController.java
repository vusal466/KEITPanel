package com.example.keitpanel.controller;

import com.example.keitpanel.dto.request.PositionCreateRequest;
import com.example.keitpanel.dto.request.PositionUpdateRequest;
import com.example.keitpanel.dto.response.PositionResponse;
import com.example.keitpanel.services.PositionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/positions")
@Tag(name = "Position", description = "Vəzifə idarəetməsi")
public class PositionController {

    private final PositionService positionService;

    @GetMapping
    public List<PositionResponse> findAll(){
        return positionService.findAll();
    }

    @GetMapping("/{id}")
    public PositionResponse findById(@PathVariable Long id){
        return positionService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PositionResponse create(@Valid @RequestBody PositionCreateRequest request){
        return positionService.create(request);
    }

    @PutMapping
    public PositionResponse update(@PathVariable Long id, @Valid @RequestParam PositionUpdateRequest request){
       return positionService.update(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        positionService.delete(id);
    }


}
