package com.example.taskqueue.category.repository;

import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.category.service.CategoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
public class CategoryRepositoryTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    EntityManager em;

    @Test
    void getCategory() throws Exception{
        //given
        Category category1 = new Category(1L,"study","red");
//        Category category2 = new Category(2L, "play","blue");
//        Category category3 = new Category(3L,"rest","orange");
        categoryService.save(category1);

        //when
        List<String> categoryList = categoryRepository.getCategoryList();
        //then
        Assertions.assertThat(categoryList.get(0)).isEqualTo("study");
    }
}
