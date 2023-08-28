package com.example.taskqueue.category.repository;

import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 특정 유저의 카테고리 목록을 반환한다.
     * @param user 유저 정보
     * @return 카테고리 목록
     */
    @Query("select c " +
            "from Category c " +
            "where c.user = :user " +
            "and c.deleted = false")
    List<Category> findCategoriesByUser(@Param("user") User user);

    /**
     * 특정 유저의 카테고리를 모두 삭제한다.
     * @param user 유저 정보
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Category c " +
            "set c.deleted = true " +
            "where c.user = :user")
    void deleteAllByUser(@Param("user") User user);

}
