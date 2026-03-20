package com.usic.planificacion.model.entity;

import com.usic.planificacion.config.AuditoriaConfig;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "porcentaje_area2")
@Setter
@Getter
public class PorcentajeArea extends AuditoriaConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPorcentajeArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area")
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_indicador")
    private Indicador indicador;

    private Integer gestion;

    @Column(scale = 2)
    private Double porcentajeFinal;

    private Integer cantidadIndicadores; // Para auditoría
}