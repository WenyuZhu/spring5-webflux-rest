package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domains.Category;
import guru.springframework.spring5webfluxrest.domains.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void getVendors() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().lastName("lastname1").firstName("firstname1").build(),
                Vendor.builder().lastName("lastname2").firstName("firstname2").build()));
        webTestClient.get()
                .uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorById() {
        BDDMockito.given(vendorRepository.findById("someid"))
                .willReturn(Mono.just(Vendor.builder().lastName("lastname").firstName("firstname").build()));
        webTestClient.get()
                .uri("/api/v1/vendors/someid")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void create() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().firstName("firstname").lastName("lastname").build()));
        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("firstname").lastName("lastname").build());
        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void update() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("firstname").lastName("lastname").build()));
        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("firstname").lastName("lastname").build());
        webTestClient.put()
                .uri("/api/v1/vendors/dwasdfdfswera")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void patchFirstNameChange() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("firstname1").lastName("lastname").build()));
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));
        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("firstname").lastName("lastname").build());
        webTestClient.patch()
                .uri("/api/v1/vendors/dwasdfdfswera")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        BDDMockito.verify(vendorRepository).save(any());
    }

    @Test
    public void patchLastNameChange() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("firstname").lastName("lastname").build()));
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));
        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("firstname").lastName("lastname1").build());
        webTestClient.patch()
                .uri("/api/v1/vendors/dwasdfdfswera")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        BDDMockito.verify(vendorRepository).save(any());
    }

    @Test
    public void patchWithoutChange() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("firstName").lastName("lastName").build()));
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("firstName").lastName("lastName").build()));
        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("firstName").lastName("lastName").build());
        webTestClient.patch()
                .uri("/api/v1/vendors/dwasdfdfswera")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        BDDMockito.verify(vendorRepository, Mockito.never()).save(any());
    }
}