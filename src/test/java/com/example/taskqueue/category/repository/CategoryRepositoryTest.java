//package com.example.taskqueue.category.repository;
//
//import com.example.taskqueue.category.entity.Category;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//public class CategoryRepositoryTest {
//
//    @Autowired
//    CategoryRepository categoryRepository;
//
//    @Test
//    void getCategory() throws Exception{
//        //given
//        Category category1 = new Category(1L,"study","red");
////        Category category2 = new Category(2L, "play","blue");
////        Category category3 = new Category(3L,"rest","orange");
//        categoryRepository.save(category1); // 얘도 repository 에서 save 사용?
//
//        //when
//        List<String> categoryList = categoryRepository.getCategoryList();
//        //then
//        Assertions.assertThat(categoryList.get(0)).isEqualTo("study");
//    }
//}
