package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.AreaDao;
import com.usic.planificacion.model.entity.Area;

@Service
public class AreaServiceImpl implements AreaService{

    @Autowired
    private AreaDao dao;

    @Override
    public List<Area> findAll() {
        return dao.findAll();
    }

    @Override
    public Area findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Area entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Area area = dao.findById(idEntidad).orElse(null);
        area.setEstado("ELIMINADO");
        dao.save(area);
    }

    @Override
    public List<Area> listarAreasPorPei(Long idPei) {
        return dao.listarAreasPorPei(idPei);
    }

    @Override
    public List<Area> listarAreasPorPeiExeptoIdArea(Long idPei, Long idArea) {
        return dao.listarAreasPorPeiExeptoIdArea(idPei, idArea);
    }
    
}
