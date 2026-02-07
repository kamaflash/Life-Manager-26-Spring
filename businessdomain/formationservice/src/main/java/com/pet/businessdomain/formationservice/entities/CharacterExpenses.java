package com.pet.businessdomain.formationservice.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterExpenses {
    private double housing;
    private double food;
    private double transport;
    private double education;
    private double other;
}
