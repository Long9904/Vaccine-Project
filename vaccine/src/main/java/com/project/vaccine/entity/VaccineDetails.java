package com.project.vaccine.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1,message = "Dose number must be at least 1")
    private int doseNumber;

    @Min(value = 0,message = "Day must be at least 0")
    private int intervalDays;

    private  boolean status = true;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "vaccine_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Vaccine vaccine;

    @Min(value = 0,message = "Price must be at least 0")
    private double price;

    private boolean recommended;

}
