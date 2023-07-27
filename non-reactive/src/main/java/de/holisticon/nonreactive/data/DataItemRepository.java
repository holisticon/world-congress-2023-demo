package de.holisticon.nonreactive.data;

import de.holisticon.worldcongressdemo.entity.DataItemEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataItemRepository extends MongoRepository<DataItemEntity, String> {
}
