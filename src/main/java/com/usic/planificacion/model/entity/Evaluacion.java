package com.usic.planificacion.model.entity;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usic.planificacion.config.AuditoriaConfig;

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
@Table(name = "evaluacion")
@Setter
@Getter
public class Evaluacion extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvaluacion;

        @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fecha;

    private String responsable;
    private String descripcionResultado;
    private Double porcentajeAvance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_poa")
    private Poa poa;

}
