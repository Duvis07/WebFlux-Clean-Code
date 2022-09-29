package co.com.bancolombia.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Handler {

    public Mono< ServerResponse > listenGETUseCase ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body ( exampleFluxMap ( ) , Flux.class );
    }

    public Mono< ServerResponse > listenGETUseCaseFlux ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body ( exampleMonoFluxReduce ( ) , Mono.class );
    }

    public Mono< ServerResponse > listenGETUseCaseFilterFlux ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body ( exampleFilterFlux ( ) , Flux.class );
    }

    public Mono< ServerResponse > listenGETUseCaseMergeFlux ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body ( exampleMergeFlux ( ) , Flux.class );
    }

    public Mono< ServerResponse > listenGETUseCaseMergeWithFlux ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body ( exampleMergeWithFlux ( ) , Flux.class );
    }

    public Mono< ServerResponse > listenGETUseCaseMonoZip ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body ( exampleMonoZip ( ) , String.class );
    }

    public Mono< ServerResponse > listenGETUseCaseMonoWithZip ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body ( exampleMonoWhithZip ( ) , String.class );
    }

    public Mono< ServerResponse > listenGETUseCaseDefaultEmpy ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body ( exampleDefaultEmpy ( ) , String.class );

    }

    public Mono< ServerResponse > listenGETUseCaseSwitchEmpy ( ServerRequest serverRequest ) {

        return ServerResponse.ok ( ).body ( exampleSwitchIfEmpy ( ) , String.class );
    }

    public Mono< ServerResponse > listenGETUseCaseOnErrorResume ( ServerRequest serverRequest ) {
        int reqI = Integer.parseInt ( serverRequest.queryParam ( "reqI" ).orElse ( "-1" ) );

        return ServerResponse.ok ( ).body ( exampleOnErrorResume ( reqI ) , Integer.class );
    }

    public Mono< ServerResponse > listenGETUseCaseOnErrorContinue ( ServerRequest serverRequest ) {
        int reqI = Integer.parseInt ( serverRequest.queryParam ( "reqI" ).orElse ( "-1" ) );

        return ServerResponse.ok ( ).body ( exampleOnErrorContinue ( reqI ) , Integer.class );
    }


    private Mono< String > exampleMonoMap ( ) {
        Integer i = 5;
        Mono< Integer > mono = Mono.just ( i );
        return mono.map ( data -> castIntToString ( i ) );

    }

    //Recibe función con objeto al interior del Mono y
    //devuelve un Object el cuál puede ser tomado “secuencialmente” en el
    //próximo paso como si no estuviera dentro de un mono
    private Mono< String > exampleMonoFlatMap ( ) {
        Integer i = 5;
        Mono< Integer > mono = Mono.just ( i );
        return mono.flatMap ( data -> castIntToStringReactivo ( i ) );

    }

    //Recibe función con objeto al interior del Mono y
    //devuelve un Mono<Object>
    private Flux< String > exampleFluxMap ( ) {
        List< Integer > list = Arrays.asList ( 1 , 2 , 3 , 4 , 5 );
        return Flux.fromIterable ( list )
                .log ( "flux" )
                .map ( data -> castIntToString ( data ) );

    }

    //Convierte de flux a mono el flux itera cada objeto de la lista mientras el mono lo itera solo 1 una vez
    private Mono< String > exampleMonoFluxReduce ( ) {
        List< Integer > list = Arrays.asList ( 1 , 2 , 3 , 4 , 5 );
        return Flux.fromIterable ( list )
                .log ( "flux-reduce" )
                .map ( this::castIntToString )
                .reduce ( ( subtotal , datastrI ) -> subtotal.concat ( "-" ).concat ( datastrI ) )
                .log ( "flux-reduce2" );
    }


    //metodo no  reactivo
    private String castIntToString ( Integer i ) {
        return String.valueOf ( i );
    }

    //metodo reactivo
    private Mono< String > castIntToStringReactivo ( Integer i ) {
        return Mono.just ( String.valueOf ( i ) );
    }

    //Filtrar los elementos de la lista que tenga una a
    // Valida el contenido del Mono o Flux para
//que a partir de este punto del pipeline sólo se puedan procesar los items
//que cumplan con el filtro
    private Flux< String > exampleFilterFlux ( ) {
        List< String > list = Arrays.asList ( "a" , "b" , "c" , "d" , "e" , "ab" );
        return Flux.fromIterable ( list )
                .log ( "flux-filter" )
                .filter ( data -> data.contains ( "a" ) )
//                .map ( data -> data.concat ( " - " ) )
                .map ( data -> "Se encontro la letra a en la palabra: ".concat ( data ) );


    }

    //merge aca podemos combinar los publicadores que deseemos
    // Crea un publicador Flux
//combinando la información de todos los publicadores ingresados por
//parámetros, los cuales pueden ser Flux o Mono. El Flux resultante se
//puede utilizar normalmente como cualquier otro flux.
//● El método merge es sólo para la clase Flux
    private Flux< String > exampleMergeFlux ( ) {
        Mono< String > mono1 = Mono.just ( "1" );
        Mono< String > mono2 = Mono.just ( "2" );
        Mono< String > mono3 = Mono.just ( "3" );
        Mono< String > mono4 = Mono.just ( "4" );
        Mono< String > mono5 = Mono.just ( "5" );
        Flux< String > flux1 = Flux.fromIterable ( Arrays.asList ( "a" , "b" , "c" , "d" , "e" ) );

        Flux< String > fluxFromMerge = Flux.merge ( mono1 , mono2 , mono3 , mono4 , mono5 , flux1 )
                .log ( );

        return fluxFromMerge;
    }

    //mergeWith aca podemos combinar 2 publicadores
    //Este método crea un Flux combinando la
//información de 2 publicadores, es decir, el publicador que invoca el
//método y el publicador ingresado por parámetro. Los publicadores
//pueden ser Mono o Flux y puede ser invocado por cualquiera de estos.
    private Flux< String > exampleMergeWithFlux ( ) {
        Mono< String > mono1 = Mono.just ( "1" );
        Mono< String > mono2 = Mono.just ( "2" );
        Mono< String > mono3 = Mono.just ( "3" );
        Mono< String > mono4 = Mono.just ( "4" );
        Mono< String > mono5 = Mono.just ( "5" );
        Flux< String > flux1 = Flux.fromIterable ( Arrays.asList ( "a" , "b" , "c" , "d" , "e" ) );

        Flux< String > fluxFromMerge = mono1.mergeWith ( flux1 )
                .log ( );

        return fluxFromMerge;
    }

    //Podemos conbinar diferentes publicadores y de diferente tipo, string, int, etc
    // crea un publicador del mismo tipo
//que lo invocó.
//● Este método recibe por parámetros N publicadores con la información
//interna de cualquier tipo y las combina en un solo publicador con la
//información separada por tuplas en el orden que se ingresaron en los
//parámetros.
//● Todos los publicadores deben ser del mismo tipo, es decir, Mono o Flux.
    private Mono< String > exampleMonoZip ( ) {
        Mono< String > mono1 = Mono.just ( "1-" );
        Mono< String > mono2 = Mono.just ( "2-" );
        Mono< String > mono3 = Mono.just ( "3-" );
        Mono< String > mono4 = Mono.just ( "4" );
        Mono< Integer > monoInt1 = Mono.just ( 1 );

        return Mono.zip ( monoInt1 , mono1 , mono2 , mono3 , mono4 )
                .map ( data -> String.valueOf ( data.getT1 ( ) ).concat ( data.getT2 ( ) )
                        .concat ( data.getT3 ( ) ).concat ( data.getT4 ( ) ) );

    }

    //Este método recibe un parámetro y devuelve un publicador del mismo
//tipo con sólo dos tuplas donde la T1 es el publicador que invocó el
//método y T2 el que se ingresó por parámetro
    private Mono< String > exampleMonoWhithZip ( ) {
        Mono< String > mono1 = Mono.just ( "1-" );
        Mono< String > mono2 = Mono.just ( "2-" );
        Mono< String > mono3 = Mono.just ( "3-" );
        Mono< String > mono4 = Mono.just ( "4" );
        Mono< Integer > monoInt1 = Mono.just ( 1 );

        return mono1.zipWith ( monoInt1 )
                .map ( data -> data.getT1 ( ).concat ( "-" ).concat ( String.valueOf ( data.getT2 ( ) ) ) );
    }


    //si no  se encuentra la letra filtrada se retorna un valor por defecto un mensaje
    // Recibe un objeto el cual se va a
    //tomar en el siguiente paso del pipeline, es decir, recibe un objeto y lo
    //va a retornar como flux(eseObjecto) y se activa siempre que en el paso
    //anterior se haya detectado o retornado un subscriptor vacío
    private Mono< String > exampleDefaultEmpy ( ) {
        Flux< String > list = Flux.fromIterable ( Arrays.asList ( "o" , "ab" , "ac" , "d" , "e" ) );
        return list
                .filter ( data -> data.contains ( "z" ) )
                .defaultIfEmpty ( "No se encontro la letra z" )
                .reduce ( ( subtotal , data ) -> subtotal.concat ( "-" ).concat ( data ) );

    }

    // switchIfEmpty  retorna un objeto y en switchIfEmpty  retorna un publicador del mismo proceso asincrono
    // Este método
//También se activa cuando en el paso anterior del pipeline se detecta un
//subscriptor vacío pero con la diferencia es que en este caso se recibe
//por parámetro un nuevo publicador, es decir, nos da la oportunidad de
//que podamos llevar a cabo un nuevo proceso
    private Mono< String > exampleSwitchIfEmpy ( ) {
        Flux< String > list = Flux.fromIterable ( Arrays.asList ( "a" , "ab" , "ac" , "d" , "e" ) );
        return list
                .filter ( data -> data.contains ( "a" ) )
                .reduce ( ( subtotal , data ) -> subtotal.concat ( "-" ).concat ( data ) )
                .switchIfEmpty ( exampleDefaultEmpy ( ) );

    }

    //onErrorResume detiene un flujo y crea un flujo alterno
    // Este método se lanza automáticamente
//cuando ocurre un error durante el flujo del proceso y se debe programar
//un objeto de salida al finalizar la función que será el retornado en el
//pipeline ya que al entrar en este método, el proceso se detiene.
    private Mono< Integer > exampleOnErrorResume ( int reqI ) {
        return Flux.range ( 20 , 30 )
                .doOnNext ( i -> System.out.println ( "input: " + i ) )
                .map ( i -> i / reqI )
                .reduce ( ( subtotal , i ) -> subtotal + i )
                .onErrorResume ( e -> {
                    System.out.println ( "Ocurrio un error: " + e.getMessage ( ) );
                    return Mono.just ( - 1 );
                } );
    }

    //onErrorContinue va continuar con el siguiente iterador del flux solo se puede utilizar en flux
    //onErrorResume se puede utilizar en flux y en mono porque encuentra un error y lo detiene
    //: Este método también se lanza cuando
//ocurre un error durante el flujo del proceso pero se diferencia al
//anterior en que al finalizar la función no se debe retornar nada, esto es
//porque este método se usa como monitor y no como un método que
//puede alterar el proceso, es decir, se puede monitorear cuando haya
//ocurrido un error pero el proceso va a continuar con el siguiente paso.
    private Mono< Integer > exampleOnErrorContinue ( int reqI ) {
        return Flux.range ( 1 , 5 )
                .doOnNext ( i -> System.out.println ( "input: " + i ) )
                .map ( i -> i == reqI ? i / 0 : i )
                .onErrorContinue ( ( error , i ) -> {
                    System.out.println ( "Ocurrio un error: " + error.getMessage ( ) );
                    System.out.println ( "Valor que genero el error: " + i );
                } )
                .reduce ( ( subtotal , i ) -> subtotal + i );

    }


}