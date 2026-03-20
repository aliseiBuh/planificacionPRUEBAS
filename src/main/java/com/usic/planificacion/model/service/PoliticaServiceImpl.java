package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.PoliticaDao;
import com.usic.planificacion.model.entity.Politica;

@Service
public class PoliticaServiceImpl implements PoliticaService{

    @Autowired
    private PoliticaDao dao;

    @Override
    public List<Politica> findAll() {
        return dao.findAll();
    }

    @Override
    public Politica findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Politica entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Politica politica = dao.findById(idEntidad).orElse(null);
        politica.setEstado("ELIMINADO");
        dao.save(politica);
    }

    @Override
    public List<Politica> listarPoliticaPorArea(Long idArea) {
        return dao.listarPoliticaPorArea(idArea);
    }

    @Override
    public List<Politica> listarPoliticaPorIdPei(Long idPei) {
        return dao.listarPoliticaPorIdPei(idPei);
    }
    
}
