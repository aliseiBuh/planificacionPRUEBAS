package com.usic.planificacion.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usic.planificacion.model.dao.ResultadoDao;
import com.usic.planificacion.model.entity.Resultado;

@Service
public class ResultadoServiceImpl implements ResultadoService{

    @Autowired
    private ResultadoDao dao;

    @Override
    public List<Resultado> findAll() {
        return dao.findAll();
    }

    @Override
    public Resultado findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Resultado entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Resultado resultado = dao.findById(idEntidad).orElse(null);
        resultado.setEstado("ELIMINADO");
        dao.save(resultado);
    }

    @Override
    public Resultado resultadoPorIdIndicador(Long IdIndicador) {
        return dao.resultadoPorIdIndicador(IdIndicador);
    }

    @Override
    public List<Resultado> findAlll() {
        return dao.findAlll();
    }

   
}
