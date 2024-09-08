package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.dto.EpisodioDto;
import br.com.alura.screenmatch.dto.SerieDto;
import br.com.alura.screenmatch.repositorio.SerieRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {



    @Autowired
    private SerieRepositorio repositorio;

    public List<SerieDto> obterTodasAsSeries(){
        return converteDados(repositorio.findAll());

    }

    public List<SerieDto> obterTop5series() {
      return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());

    }
    private List<SerieDto> converteDados (List<Serie> series){
        return series.stream().map(s -> new SerieDto(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getPoster(),s.getSinopse())
        ).collect(Collectors.toList());
    }

    public List<SerieDto> obterLancamentos() {
        return converteDados(repositorio.LancamentosMaisRecentes());
    }

    public SerieDto obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);
        Serie s = serie.get();
        if (serie.isPresent()){
            return new SerieDto(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getPoster(),s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDto> obterTodasAsTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);
        Serie s = serie.get();
        if (serie.isPresent()){
            return s.getEpisodios().stream()
                    .map( e -> new EpisodioDto(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDto> obterTemporadasPorNumero(Long id, Long numero) {
        return repositorio.obterEpisodiosPorTemporada(id, numero)
                .stream().map( e -> new EpisodioDto(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDto> obterSeriesPorCategoria(String nomeGenero) {
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        return converteDados(repositorio.findByGenero(categoria));
    }
}
