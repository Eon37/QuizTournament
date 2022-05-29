package com.company.Quiz_Tournament.api.QuizImage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;



public interface QuizImageRepository extends CrudRepository<QuizImage, Long> {

    @Query(value = "select * from Quiz_Image qi where qi.type = :type order by random() limit 1", nativeQuery = true)
    QuizImage findRandomImage(@Param("type") int type);
}
