package de.holisticon.reactive.data;

import de.holisticon.worldcongressdemo.entity.DataItemEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataItemReactiveRepository extends ReactiveMongoRepository<DataItemEntity, String> {
}
