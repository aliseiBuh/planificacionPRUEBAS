package com.usic.planificacion.model.entity;

import java.util.List;

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
@Table(name = "area")
@Setter
@Getter
public class Area extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArea;

    private Integer codigo;
    private String nombre;
    private Integer ponderacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pei")
    private Pei pei;
    // @ManyToMany(mappedBy = "areas", fetch = FetchType.LAZY)
    // private Set<Pei> pei = new HashSet<>();

    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    private List<Politica> politicas;
}
