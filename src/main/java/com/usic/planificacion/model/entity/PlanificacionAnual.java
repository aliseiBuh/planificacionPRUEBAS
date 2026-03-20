// package com.usic.planificacion.model.entity;


// import com.usic.planificacion.config.AuditoriaConfig;
// import jakarta.persistence.*;
// import lombok.Getter;
// import lombok.Setter;

// @Entity
// @Table(name = "porcentaje_area")
// @Setter
// @Getter

// public class PlanificacionAnual extends AuditoriaConfig {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long idPlanificacionAnual;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "id_area")
//     private Area area;

//     private Integer gestion;

//     @Column(scale = 2)
//     private Double porcentajeFinal;

//     private Integer cantidadIndicadores; // numero de indicadores considerados en la planificacion anual

// }
