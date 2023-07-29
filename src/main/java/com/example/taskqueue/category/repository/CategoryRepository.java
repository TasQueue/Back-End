package com.example.taskqueue.category.repository;

import com.example.taskqueue.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

//    @Query("select f.followUserId from Follow f where f.user.id = :userId")
//    List<Category> getFollowList(@Param("userId") Long userId);

    /**
     * Category 전체 리스트 조회 쿼 -> user 와 조인?
     */
    @Query("select c.id from Category c where ")
    List<String> getCategoryList();
}