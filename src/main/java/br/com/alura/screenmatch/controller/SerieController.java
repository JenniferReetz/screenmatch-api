package br.com.alura.screenmatch.controller;


import br.com.alura.screenmatch.dto.SerieDto;
import br.com.alura.screenmatch.model.SerieService;
import br.com.alura.screenmatch.repositorio.SerieRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    @Autowired
    private SerieService servico;
    @Autowired
    private SerieRepositorio repositorio;

    @GetMapping
    public List<SerieDto> obterSeries(){
        return servico.obterTodasAsSeries();
    }
    @GetMapping("/top5")
    public List<SerieDto> obterTop5Series(){
        return servico.obterTop5series();
    }
    @GetMapping("/lancamentos")
    public List<SerieDto> obterLancamentos(){
        return servico.obterLancamentos();
    }
    @GetMapping("/{id}")
    public SerieDto obterPorId(@PathVariable  Long id){
    return servico.obterPorId(id);
    }

}
