package guru.springframework.spring5webfluxrest.repositories;

import guru.springframework.spring5webfluxrest.domains.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
