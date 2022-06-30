package com.alassaneniang.springreactivemongocrud.utils;

import com.alassaneniang.springreactivemongocrud.dto.ProductDto;
import com.alassaneniang.springreactivemongocrud.entity.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class AppUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static ProductDto entityToDto(Product product) {
        modelMapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper
                .map(product, ProductDto.class);
    }

    public static Product dtoToEntity(ProductDto productDto) {
        modelMapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper
                .map(productDto, Product.class);
    }

}
