package com.alassaneniang.springreactivemongocrud;

import com.alassaneniang.springreactivemongocrud.controller.ProductController;
import com.alassaneniang.springreactivemongocrud.dto.ProductDto;
import com.alassaneniang.springreactivemongocrud.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ProductController.class)
class SpringReactiveMongoCrudApplicationTests {

    @MockBean
    ProductService service;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void addProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(
                new ProductDto("102", "mobile", 1, 1000));
        when(service.saveProduct(productDtoMono))
                .thenReturn(productDtoMono);
        webTestClient
                .post()
                .uri("/products")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getProductsTest() {
        Flux<ProductDto> productDtoFlux = Flux.just(
                new ProductDto("102", "mobile", 1, 1000),
                new ProductDto("103", "TV", 1, 5000));
        when(service.getProducts())
                .thenReturn(productDtoFlux);
        Flux<ProductDto> responseBody = webTestClient
                .get()
                .uri("/products")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();
        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("102", "mobile", 1, 1000))
                .expectNext(new ProductDto("103", "TV", 1, 5000))
                .verifyComplete();
    }

    @Test
    void getProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(
                new ProductDto("102", "mobile", 1, 1000));
        when(service.getProduct(any()))
                .thenReturn(productDtoMono);
        Flux<ProductDto> responseBody = webTestClient
                .get()
                .uri("/products/102")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();
        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextMatches(productDto -> productDto.getName().equals("mobile"))
                .verifyComplete();
    }

    @Test
    void updateProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(
                new ProductDto("102", "mobile", 1, 1000));
        when(service.updateProduct(productDtoMono, "102"))
                .thenReturn(productDtoMono);
        webTestClient
                .put()
                .uri("/products/update/102")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void deleteProductTest() {
        given(service.deleteProduct(any()))
                .willReturn(Mono.empty());
        webTestClient
                .delete()
                .uri("/products/delete/102")
                .exchange()
                .expectStatus().isOk();
    }

}
