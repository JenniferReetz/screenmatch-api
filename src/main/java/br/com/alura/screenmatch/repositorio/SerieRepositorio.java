package br.com.alura.screenmatch.repositorio;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepositorio extends JpaRepository<Serie, Long> {

   Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

   List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

   List<Serie> findTop5ByOrderByAvaliacaoDesc();
   List<Serie> findByGenero(Categoria categoria);

   List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqualOrderByAvaliacaoDesc(int totalTemporadas, double avaliacao);
}
