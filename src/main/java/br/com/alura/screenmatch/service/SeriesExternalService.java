package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SeriesExternalService {

    private final String API_KEY = "6585022c";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public DadosSerie consultarDadosSerie(String nomeSerie){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("t", nomeSerie.toLowerCase());
        params.add("apikey", API_KEY);
        URI uri = montarUri(params);
        var json = consumoApi.obterDados(uri);
        DadosSerie dadosSerie = conversor.converterDados(json, DadosSerie.class);
        return dadosSerie;
    }

    public List<DadosTemporada> consultarDadosTemporada(DadosSerie dadosSerie){
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("t", dadosSerie.titulo().toLowerCase());
            params.add("season", String.valueOf(i));
            params.add("apikey", API_KEY);
            URI uri = montarUri(params);
            var json = consumoApi.obterDados(uri);
            DadosTemporada dadosTemporada = conversor.converterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        return temporadas;
    }

    private URI montarUri(MultiValueMap<String, String> params){
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("www.omdbapi.com")
                .path("/")
                .queryParams(params)
                .build()
                .toUri();
        return uri;
    }
}
