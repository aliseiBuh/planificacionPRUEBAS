package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.PresupuestoDao;
import com.usic.planificacion.model.entity.Presupuesto;

@Service
public class PresupuestoServiceImpl implements PresupuestoService{

    @Autowired
    private PresupuestoDao presupuestoDao;

    @Override
    public List<Presupuesto> findAll() {
        return presupuestoDao.findAll();
    }

    @Override
    public Presupuesto findById(Long idEntidad) {
        return presupuestoDao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Presupuesto entidad) {
        presupuestoDao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Presupuesto presupuesto = presupuestoDao.findById(idEntidad).orElse(null);
        presupuesto.setEstado("ELIMINADO");
        presupuestoDao.save(presupuesto);
    }
    
}
