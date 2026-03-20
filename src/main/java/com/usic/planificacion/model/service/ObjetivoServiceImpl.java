package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.ObjetivoDao;
import com.usic.planificacion.model.entity.Objetivo;

@Service
public class ObjetivoServiceImpl implements ObjetivoService{

    @Autowired
    private ObjetivoDao dao;

    @Override
    public List<Objetivo> findAll() {
        return dao.findAll();
    }

    @Override
    public Objetivo findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Objetivo entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Objetivo objetivo = dao.findById(idEntidad).orElse(null);
        objetivo.setEstado("ELIMINADO");
        dao.save(objetivo);
    }

    @Override
    public List<Objetivo> listarObjetivoPorPolitica(Long idPolitica) {
        return dao.listarObjetivoPorPolitica(idPolitica);
    }

    @Override
    public List<Objetivo> listarObjetivoPorIdPei(Long idPolitica) {
        // TODO Auto-generated method stub
        return dao.listarObjetivoPorIdPei(idPolitica);
    }

    @Override
    public List<Objetivo> listarObjetivoActivos() {
        return dao.listarObjetivoActivos();
    }
    
}
