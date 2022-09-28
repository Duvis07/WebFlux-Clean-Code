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


    private Flux<String> exampleFilterFlux(){
        List < String > list = Arrays.asList ( "a" , "b" , "c" , "d" , "e", "ab" );
        return Flux.fromIterable ( list )
                .log ( "flux-filter" )
                .filter ( data -> data.contains ( "a" ) )
//                .map ( data -> data.concat ( " - " ) )
                .map ( data -> "Se encontro la letra a en la palabra: ".concat ( data ) );


    }

}