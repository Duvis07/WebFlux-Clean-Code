package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class RouterRest {
@Bean
public RouterFunction < ServerResponse > routerFunction ( Handler handler ) {
    return route ( GET ( "/api/usecase/path" ) , handler :: listenGETUseCase )
            .andRoute ( GET ( "/api/usecase/path/flux" ) , handler :: listenGETUseCaseFlux )
            .andRoute ( GET ( "/api/usecase/path/flux/filter" ) , handler :: listenGETUseCaseFilterFlux )
            .andRoute ( GET ( "/api/usecase/path/flux/merge" ) , handler :: listenGETUseCaseMergeFlux )
            .andRoute ( GET ( "/api/usecase/path/flux/mergeWith" ) , handler :: listenGETUseCaseMergeWithFlux )
            .andRoute ( GET ( "/api/usecase/path/flux/zip" ) , handler :: listenGETUseCaseMonoZip )
            .andRoute ( GET ( "/api/usecase/path/flux/zipWith" ) , handler :: listenGETUseCaseMonoWithZip );
}
}
