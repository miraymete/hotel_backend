package com.example.jwtsecurity.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ürün")
public class Product {

    @Schema(description = "Ürün adı", example = "Kitap") // Swagger açıklaması
    private String name;

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
