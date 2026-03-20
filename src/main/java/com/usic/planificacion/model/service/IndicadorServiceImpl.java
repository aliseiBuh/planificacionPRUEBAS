package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.InidicadorDao;
import com.usic.planificacion.model.entity.Indicador;

@Service
public class IndicadorServiceImpl implements IndicadorService{

    @Autowired
    private InidicadorDao dao;

    @Override
    public List<Indicador> findAll() {
        return dao.findAll();
    }

    @Override
    public Indicador findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Indicador entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Indicador indicador = dao.findById(idEntidad).orElse(null);
        indicador.setEstado("ELIMINADO");
        dao.save(indicador);
    }

    @Override
    public List<Indicador> listarIndicadorPorObjetivo(Long idObjetivo) {
        return dao.listarIndicadorPorObjetivo(idObjetivo);
    }

    @Override
    public List<Indicador> listarIndicadorPorObjetivoExeptoIdIndicador(Long idObjetivo, Long idIndicador) {
        return dao.listarIndicadorPorObjetivoExeptoIdIndicador(idObjetivo, idIndicador);
    }

    @Override
    public List<Indicador> listarIndicadoresPorIdUnidad(Long idUnidad) {
        // TODO Auto-generated method stub
        return dao.listarIndicadoresPorIdUnidad(idUnidad);
    }

    @Override
    public List<Indicador> findAlll() {
        return dao.findAlll();
    }
    
}
