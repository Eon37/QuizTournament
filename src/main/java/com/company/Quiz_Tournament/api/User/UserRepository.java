package com.company.Quiz_Tournament.api.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select u from Users u where u.email = :email")
    User findByEmail(@Param("email") String email);
}
