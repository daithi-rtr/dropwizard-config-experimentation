package com.example.brands;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/brands")
@Produces(MediaType.APPLICATION_JSON)
public class BrandResource {

    private final int defaultSize;
    private final BrandRepository brandRepository;

    public BrandResource(int defaultSize, BrandRepository brandRepository){
        this.defaultSize = defaultSize;
        this.brandRepository = brandRepository;
    }

    @GET
    public List<Brand> getBrands(@QueryParam("size") Optional<Integer> size){
        return brandRepository.findAll(size.orElse(defaultSize));
    }

    @GET
    @Path("{id}")
    public Brand getById(@PathParam("id") Long id){
        return brandRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Failed to find dress with id %d", id)));
    }

}
