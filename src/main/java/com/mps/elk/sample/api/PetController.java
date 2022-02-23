package com.mps.elk.sample.api;

import com.mps.elk.sample.model.Pet;
import com.mps.elk.sample.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService service;

    @GetMapping
    public List<Pet> getPets(@RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) String species) throws IOException {
        return service.list(name, age, species);
    }

    @PostMapping
    public Pet createPet(@RequestBody Pet pet) {
        return service.create(pet);
    }

    @PutMapping
    public Pet updatePet(@RequestBody Pet pet) {
        return service.update(pet);
    }

    @DeleteMapping
    public boolean deletePet(@RequestParam String name, @RequestParam Integer age, @RequestParam String species) {
        Pet pet = new Pet();
        pet.setName(name);
        pet.setAge(age);
        pet.setSpecies(species);
        return service.delete(pet);
    }
}