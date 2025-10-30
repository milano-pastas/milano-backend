package com.milano.milanopastas.mapper;

import com.milano.milanopastas.dto.ProductDTO;
import com.milano.milanopastas.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product entity);
    Product toEntity(ProductDTO dto);
}
