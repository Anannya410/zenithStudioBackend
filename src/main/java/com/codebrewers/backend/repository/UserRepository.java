package com.codebrewers.backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.codebrewers.backend.dao.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
}
