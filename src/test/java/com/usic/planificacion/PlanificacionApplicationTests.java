package com.usic.planificacion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.usic.planificacion.model.dao.InidicadorDao;
import com.usic.planificacion.model.dao.MetaDao;
import com.usic.planificacion.model.entity.Indicador;
import com.usic.planificacion.model.entity.Meta;
import com.usic.planificacion.model.entity.Resultado;

@SpringBootTest
class PlanificacionApplicationTests {

	@Test
	void contextLoads() {

	}
@Autowired 
private InidicadorDao inidicadorDao;
@Autowired
private MetaDao metaDao;
@Test  
void pruebaDivisionResultadoMeta() {
    // Meta meta = new Meta();
    // Indicador indicador = new Indicador();
    // meta.setResultado(0);
    // indicador.setTotal(25);
    Meta meta= metaDao.findById((long) 6).orElseThrow();
    meta.getResultado();
    Indicador indicador= inidicadorDao.findById((long)2).orElseThrow();
    
   double prueba1 = 100.0 * meta.getResultado() / indicador.getTotal();
    
    assertEquals(20, prueba1);
    System.out.println("meta resultado-----------------------------------"+meta.getResultado());
        System.out.println("indicador   TOTAL-----------------------------------"+ indicador.getTotal());

    System.out.println("----------------exito-------" + prueba1);
}

}
