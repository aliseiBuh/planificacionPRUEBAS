package com.usic.planificacion.model.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usic.planificacion.config.AuditoriaConfig;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pei")
@Setter
@Getter
public class Pei extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPei;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaFin;

    private Integer gestionInicio;
    private Integer gestionFin;

    private String objetivoGeneral;

    @Column(length = 2000)
    private String mision;
    @Column(length = 2000)
    private String vision;

    @OneToMany(mappedBy = "pei", fetch = FetchType.EAGER)
    private List<Poa> poas;

    @OneToMany(mappedBy = "pei", fetch = FetchType.EAGER)
    private List<Area> areas;

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    // @ManyToMany(fetch = FetchType.LAZY)
    // @JoinTable(name = "pei_area", 
    // joinColumns = @JoinColumn(name = "id_pei"), 
    // inverseJoinColumns = @JoinColumn(name = "id_area"))
    // private Set<Area> areas;

}
