package com.milano.milanopastas.mapper;

import com.milano.milanopastas.dto.ProductDTO;
import com.milano.milanopastas.model.Category;
import com.milano.milanopastas.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    default Product toEntity(ProductDTO dto) {
        if (dto == null) return null;
        Product p = new Product();
        p.setId(dto.getId());
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        try {
            if (dto.getCategory() != null)
                p.setCategory(Category.valueOf(dto.getCategory()));
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Categoría inválida recibida: " + dto.getCategory());
            p.setCategory(Category.OTROS);
        }
        p.setUnit(dto.getUnit());
        p.setImageUrl(dto.getImageUrl());
        p.setActive(dto.isActive());
        return p;
    }

    ProductDTO toDTO(Product entity);

    @Named("enumToString")
    static String enumToString(Category category) {
        return category != null ? category.name() : null;
    }

    @Named("stringToEnum")
    static Category stringToEnum(String category) {
        try {
            return category != null ? Category.valueOf(category) : null;
        } catch (IllegalArgumentException e) {
            return null; // evita que explote si el string no coincide
        }
    }
}
