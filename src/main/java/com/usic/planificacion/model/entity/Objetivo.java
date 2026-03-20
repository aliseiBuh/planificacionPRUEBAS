package com.usic.planificacion.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.usic.planificacion.config.AuditoriaConfig;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "objetivo")
@Setter
@Getter
public class Objetivo extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idObjetivo;

    private Integer codigo;
    private Integer ponderacion;
    private String descripcion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_politica")
    private Politica politica;

    @JsonIgnore
    @OneToMany(mappedBy = "objetivo", fetch = FetchType.LAZY)
    private List<Indicador> indicadores;

}
