package com.usic.planificacion.model.entity;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usic.planificacion.config.AuditoriaConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "poa")
@Setter
@Getter
public class Poa extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPoa;

    private Integer gestion;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaFin;

    private String objetivoAnual;
    private Double presupuestoAnual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pei")
    private Pei pei;

    @OneToMany(mappedBy = "poa", fetch = FetchType.EAGER)
    private List<Evaluacion> evaluaciones;

    @OneToMany(mappedBy = "poa", fetch = FetchType.EAGER)
    private List<Actividad> actividades;

}
