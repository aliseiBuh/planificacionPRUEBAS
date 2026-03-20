package com.usic.planificacion.model.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usic.planificacion.config.AuditoriaConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "proyecto_inversion")
@Setter
@Getter
public class ProyectoInversion extends AuditoriaConfig{

    private static final long serialVersionUID = 2629195288020321924L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProyectoInversion;

    private String nombre;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaFin;

    
}
