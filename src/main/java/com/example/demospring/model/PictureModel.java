package com.example.demospring.model;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import jakarta.persistence.Entity;
 
@Entity
@Table(name = "pictures")
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PictureModel extends AuditModel {
    @Id 
    @Column(name = "pk")
    private String pk;

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @Column(columnDefinition = "description")
    private String description;
}