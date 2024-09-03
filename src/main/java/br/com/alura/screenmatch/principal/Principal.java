package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repositorio.SerieRepositorio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private final List<DadosSerie> dadosSeries = new ArrayList<>();
    private List<Serie> series = new  ArrayList<>();
    private Optional<Serie> serieBusca;

    public Principal(SerieRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0){
            var menu = """
                    ◢■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■◣
                    ¦01 • Buscar séries               ¦
                    ¦02 • Buscar episódios            ¦
                    ¦03 • Listar séries buscadas      ¦
                    ¦04 • Buscar série por título     ¦
                    ¦05 • Buscar séries por ator      ¦
                    ¦06 • Buscar as top 5 séries      ¦
                    ¦07 • Buscar séries por categoria ¦
                    ¦08 • Filtrar séries              ¦
                    ¦09 • Buscar episódios pelo trecho¦
                    ¦10 • Buscar os top episódios     ¦
                    ¦11 • Buscar episódios após data  ¦
                    ¦00 • Sair                        ¦
                    ◥■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■◤
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();


            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarAsTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosAposData();
                    break;
                case 0:
                    System.out.println("• • • Saindo");
                    break;
                default:
                    System.out.println("• • • Opção inválida");
            }

    }
    }

    private void buscarEpisodiosAposData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            System.out.print("• Digite o ano de limite de lançamento");
            var anoLancamento = leitura.nextInt();
            Serie serie = serieBusca.get();
            leitura.nextLine();
            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios =repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));

        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("• Qual o nome do trecho do episódio para a busca?");
        var trecho = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trecho);
        episodiosEncontrados.forEach(e->
                        System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                e.getSerie().getTitulo(), e.getTemporada(),
                e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void filtrarSeriesPorTemporadaEAvaliacao() {

        System.out.println("• Você quer filtrar séries até quantas temporadas? ");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("• Séries filtradas: \n");
        filtroSeries.forEach(s ->
                System.out.println("• "+s.getTitulo() + " Avaliação: " + s.getAvaliacao()));

    }


    private void buscarSerieWeb() {

        DadosSerie dados = getDadosSerie();
        try {
            Serie serie = new Serie(dados);
//        dadosSeries.add(dados);
           repositorio.save(serie);
           System.out.println(dados);
       }catch (Exception e)
       {

           System.out.println("• Não foi possível encontrar a série.\n"+
                 "["+ e +"]"
                   +"\n• Verifique se não há algum erro de digitação.");
       }
    }

    private SerieRepositorio repositorio;

    private DadosSerie getDadosSerie() {
        System.out.println("• Digite o nome da série para busca →");
        var nomeSerie = leitura.nextLine();

            var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
            DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
            return dados;



    }
//
    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("• Escolha uma série pelo nome →");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                     .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }else {
            System.out.println("• • • Serie não encontrada");
        }
    }
//
    private void listarSeriesBuscadas(){
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
    private void buscarSeriePorTitulo() {
        System.out.println("• Escolha uma série pelo nome →");
        var nomeSerie = leitura.nextLine();
        serieBusca= repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieBusca.isPresent()){
            System.out.println("• Dados da série : \n"+ serieBusca.get());
        }else {
            System.out.println("• • • Serie não encontrada");
        }
    }
    private void buscarSeriesPorAtor() {
        System.out.println("• Qual o nome do ator de uma série para a busca?");
        var nomeAtor = leitura.nextLine();
        System.out.println("• Avaliações apartir de qual valor?");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        seriesEncontradas.forEach(s ->
                System.out.println("• "+s.getTitulo() + " Avaliação: " + s.getAvaliacao()));
    }
    private void buscarAsTop5Series(){
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s-> System.out.println("• "+s.getTitulo() + " Avaliação: " + s.getAvaliacao()));
    }
    private void buscarSeriesPorCategoria() {
        System.out.println("• Deseja buscar séries por qual gênero/categoria?");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesCategoria = repositorio.findByGenero(categoria);
        System.out.println("• Séries da categoria/gênero: " + nomeGenero);
        seriesCategoria.forEach(System.out::println);
    }
}
