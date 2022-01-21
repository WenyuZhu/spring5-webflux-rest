package guru.springframework.spring5webfluxrest.repositories;

import guru.springframework.spring5webfluxrest.domains.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
