package com.example.brands;

import com.google.common.collect.ImmutableList;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BrandRepository {

    private final ImmutableList<Brand> brands;

    public BrandRepository(List<Brand> brands){
        this.brands = ImmutableList.copyOf(brands);
    }

    public List<Brand> findAll(int size){
        return this.brands.stream()
                .limit(size)
                .collect(Collectors.toList());
    }

    public Optional<Brand> findById(Long id){
        return this.brands.stream()
                .filter(b -> b.getId().equals(id)).findFirst();
    }
}
