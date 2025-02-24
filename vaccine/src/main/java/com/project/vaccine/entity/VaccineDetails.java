package com.project.vaccine.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    private int dose_number;

    @Min(value = 0,message = "Date after must be at least 0")
    private int interval_days;

    private  boolean status = true;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "vaccine_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Vaccine vaccine;

    @Min(value = 0,message = "Price must be at least 0")
    private double price;

    @NotBlank(message = "Side effect is required")
    private String side_effect;

    private boolean recommended;

}
