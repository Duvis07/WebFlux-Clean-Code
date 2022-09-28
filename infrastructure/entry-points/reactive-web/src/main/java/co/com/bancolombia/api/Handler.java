package co.com.bancolombia.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Handler {

    public Mono < ServerResponse > listenGETUseCase ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body (exampleFluxMap (),Flux.class);
    }

    public Mono < ServerResponse > listenGETUseCaseFlux ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body (exampleMonoFluxReduce (),Mono.class);
    }

    public Mono < ServerResponse > listenGETUseCaseFilterFlux ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body (exampleFilterFlux (),Flux.class);
    }
    public Mono < ServerResponse > listenGETUseCaseMergeFlux ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body (exampleMergeFlux (),Flux.class);
    }

    public Mono < ServerResponse > listenGETUseCaseMergeWithFlux ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body (exampleMergeWithFlux (),Flux.class);
    }

    public Mono < ServerResponse > listenGETUseCaseMonoZip ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body (exampleMonoZip (),String.class);
    }

    public Mono < ServerResponse > listenGETUseCaseMonoWithZip ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body (exampleMonoWhithZip (),String.class);
    }




    private Mono < String > exampleMonoMap ( ) {
        Integer i = 5;
        Mono < Integer > mono = Mono.just ( i );
        return mono.map ( data -> castIntToString ( i ) );

    }

    private Mono < String > exampleMonoFlatMap ( ) {
        Integer i = 5;
        Mono < Integer > mono = Mono.just ( i );
        return mono.flatMap ( data -> castIntToStringReactivo ( i ) );

    }
//lista de flux
    private Flux < String > exampleFluxMap ( ) {
        List < Integer > list = Arrays.asList ( 1 , 2 , 3 , 4 , 5 );
        return Flux.fromIterable ( list )
                .log ( "flux" )
                .map ( data -> castIntToString ( data ) );

    }

//Convierte de flux a mono el flux itera cada objeto de la lista mientras el mono lo itera solo 1 una vez
    private Mono<String> exampleMonoFluxReduce(){
        List < Integer > list = Arrays.asList ( 1 , 2 , 3 , 4 , 5 );
        return Flux.fromIterable ( list )
                .log ( "flux-reduce" )
                .map ( this::castIntToString )
                .reduce((subtotal,datastrI)-> subtotal.concat("-").concat ( datastrI ))
                .log ("flux-reduce2");
    }


    //metodo no  reactivo
    private String castIntToString(Integer i) {
     return String.valueOf(i);
    }

    //metodo reactivo
    private Mono<String> castIntToStringReactivo(Integer i) {
     return Mono.just(String.valueOf(i));
    }

//Filtrar los elementos de la lista que tenga una a
    private Flux<String> exampleFilterFlux(){
        List < String > list = Arrays.asList ( "a" , "b" , "c" , "d" , "e", "ab" );
        return Flux.fromIterable ( list )
                .log ( "flux-filter" )
                .filter ( data -> data.contains ( "a" ) )
//                .map ( data -> data.concat ( " - " ) )
                .map ( data -> "Se encontro la letra a en la palabra: ".concat ( data ) );


    }
//merge aca podemos combinar los publicadores que deseemos
    private Flux<String> exampleMergeFlux() {
        Mono < String > mono1 = Mono.just ( "1" );
        Mono < String > mono2 = Mono.just ( "2" );
        Mono < String > mono3 = Mono.just ( "3" );
        Mono < String > mono4 = Mono.just ( "4" );
        Mono < String > mono5 = Mono.just ( "5" );
        Flux < String > flux1 = Flux.fromIterable ( Arrays.asList ( "a" , "b" , "c" , "d" , "e" ) );

        Flux<String>fluxFromMerge= Flux.merge (mono1,mono2,mono3,mono4,mono5,flux1)
                .log ();

        return fluxFromMerge;
    }
//mergeWith aca podemos combinar 2 publicadores
    private Flux<String> exampleMergeWithFlux() {
        Mono < String > mono1 = Mono.just ( "1" );
        Mono < String > mono2 = Mono.just ( "2" );
        Mono < String > mono3 = Mono.just ( "3" );
        Mono < String > mono4 = Mono.just ( "4" );
        Mono < String > mono5 = Mono.just ( "5" );
        Flux < String > flux1 = Flux.fromIterable ( Arrays.asList ( "a" , "b" , "c" , "d" , "e" ) );

        Flux<String>fluxFromMerge= mono1.mergeWith (flux1)
                .log ();

        return fluxFromMerge;
    }
//Podemos conbinar diferentes publicadores y de diferente tipo, string, int, etc
    private Mono<String>exampleMonoZip(){
        Mono < String > mono1 = Mono.just ( "1-" );
        Mono < String > mono2 = Mono.just ( "2-" );
        Mono < String > mono3 = Mono.just ( "3-" );
        Mono < String > mono4 = Mono.just ( "4" );
        Mono<Integer>monoInt1=Mono.just ( 1 );

        return Mono.zip (monoInt1,mono1,mono2,mono3,mono4)
                .map ( data -> String.valueOf ( data.getT1()).concat ( data.getT2 ())
                        .concat ( data.getT3 ()).concat ( data.getT4 () ))  ;

    }

    private Mono<String>exampleMonoWhithZip(){
        Mono < String > mono1 = Mono.just ( "1-" );
        Mono < String > mono2 = Mono.just ( "2-" );
        Mono < String > mono3 = Mono.just ( "3-" );
        Mono < String > mono4 = Mono.just ( "4" );
        Mono<Integer>monoInt1=Mono.just ( 1 );

        return mono1.zipWith (  monoInt1)
                .map ( data -> data.getT1 ().concat ( "-" ).concat ( String.valueOf ( data.getT2 () ) ) );
    }




}