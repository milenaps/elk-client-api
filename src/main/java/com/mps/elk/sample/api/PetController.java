package com.mps.elk.sample.api;

import com.mps.elk.sample.model.Pet;
import com.mps.elk.sample.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService service;

    @GetMapping
    public List<Map> getPets(@RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) String species) throws IOException {
        return service.list(name, age, species);
    }

    @PostMapping
    public Pet savePet(@RequestBody Pet pet) throws IOException {
        return service.save(pet);
    }

    @DeleteMapping
    public boolean deletePet(@RequestParam String name, @RequestParam Integer age, @RequestParam String species) throws IOException {
        return service.delete(name, age, species);
    }
}
