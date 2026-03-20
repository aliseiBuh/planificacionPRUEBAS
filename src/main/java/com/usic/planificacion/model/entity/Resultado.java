package com.usic.planificacion.model.entity;

import com.usic.planificacion.config.AuditoriaConfig;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resultado")
@Setter
@Getter
public class Resultado extends AuditoriaConfig {

    private static final long serialVersionUID = 2629195288020321924L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResultado;

    private Integer codigo;

    private String denominacion;

    private Integer totalR;
    @Column(scale = 2)
    private Double totalDecimalR;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_indicador")
    private Indicador indicador;
}
