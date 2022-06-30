package com.alassaneniang.springreactivemongocrud.service;

import com.alassaneniang.springreactivemongocrud.dto.ProductDto;
import com.alassaneniang.springreactivemongocrud.repository.ProductRepository;
import com.alassaneniang.springreactivemongocrud.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;

    public Flux<ProductDto> getProducts() {
        return repository
                .findAll()
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProduct(String id) {
        return repository
                .findById(id)
                .map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductsInRange(double min, double max) {
        return repository
                .findByPriceBetween(Range.closed(min, max))
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono
                .map(AppUtils::dtoToEntity)
                .flatMap(repository::insert)
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
        return repository.findById(id)
                .flatMap(product -> productDtoMono.map(AppUtils::dtoToEntity)
                        .doOnNext(element -> element.setId(id)))
                .flatMap(repository::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<Void> deleteProduct(String id) {
        return repository
                .deleteById(id);
    }

}
