package com.usic.planificacion.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.usic.planificacion.config.AuditoriaConfig;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "politica")
@Setter
@Getter
public class Politica extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPolitica;

    private Integer codigo;
    private Integer ponderacion;

    @Column(length = 2000)
    private String descripcion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area")
    private Area area;

    @JsonIgnore
    @OneToMany(mappedBy = "politica", fetch = FetchType.LAZY)
    private List<Objetivo> objetivos;
}
