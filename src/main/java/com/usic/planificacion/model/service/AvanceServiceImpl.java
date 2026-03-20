package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.AvanceDao;
import com.usic.planificacion.model.entity.Avance;

@Service
public class AvanceServiceImpl implements AvanceService{

    @Autowired
    private AvanceDao avanceDao;
    @Override
    public List<Avance> findAll() {
        return avanceDao.findAll();
    }

    @Override
    public Avance findById(Long idEntidad) {
        return avanceDao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Avance entidad) {
        avanceDao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Avance avance = avanceDao.findById(idEntidad).orElse(null);
        avance.setEstado("ELIMINADO");
        avanceDao.save(avance);
    }

    @Override
    public List<Avance> listarAvancesPorIdUnidad(Long IdUnidad) {
        // TODO Auto-generated method stub
        return avanceDao.listarAvancesPorIdUnidad(IdUnidad);
    }
    
}
