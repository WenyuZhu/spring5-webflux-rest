package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domains.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
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

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryController categoryController;
    CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void getAllCategories() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Cat1").build(),
                        Category.builder().description("Cat2").build()));
        webTestClient.get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void getCategoryById() {
        BDDMockito.given(categoryRepository.findById("someid"))
                .willReturn(Mono.just(Category.builder().description("Cat1").build()));
        webTestClient.get()
                .uri("/api/v1/categories/someid/")
                .exchange()
                .expectBody(Category.class);
    }


    @Test
    public void create() {
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().description("some description").build()));
        Mono<Category> catToSave = Mono.just(Category.builder().description("category1").build());
        webTestClient.post()
                .uri("/api/v1/categories")
                .body(catToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }


    @Test
    public void update() {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().description("some description").build()));
        Mono<Category> catToSave = Mono.just(Category.builder().description("category1").build());
        webTestClient.put()
                .uri("/api/v1/categories/dwasdfdfswera")
                .body(catToSave, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}