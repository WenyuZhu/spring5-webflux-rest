package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domains.Category;
import guru.springframework.spring5webfluxrest.domains.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {
    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("/api/v1/vendors")
    public Flux<Vendor> getVendors(){
        return vendorRepository.findAll();
    }

    @GetMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id){
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("api/v1/vendors")
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendor){
        return vendorRepository.saveAll(vendor).then();
    }

    @PutMapping("api/v1/vendors/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor){
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("api/v1/vendors/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor){
        Vendor prevVendor = vendorRepository.findById(id).block();
        if(!prevVendor.getFirstName().equals(vendor.getFirstName()) || !prevVendor.getLastName().equals(vendor.getLastName())){
            if(!prevVendor.getFirstName().equals(vendor.getFirstName())){
                prevVendor.setFirstName(vendor.getFirstName());
            }
            if(!prevVendor.getLastName().equals(vendor.getLastName())){
                prevVendor.setFirstName(vendor.getFirstName());
            }
            vendorRepository.save(prevVendor);
        }
        return Mono.just(prevVendor);
    }
}
