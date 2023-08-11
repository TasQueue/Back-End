package com.example.taskqueue.category.service;

import com.example.taskqueue.category.controller.dto.CategoryUpdateDto;
import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.category.repository.CategoryRepository;
import com.example.taskqueue.exception.notfound.CategoryNotFoundException;
import com.example.taskqueue.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 전체를 조회한다
     * @param user 현재 유저
     * @return 해당 유저의 category 리스트
     */
    public List<Category> findByUser(User user){
        if(!ObjectUtils.isEmpty(categoryRepository.findCategoriesByUser(user))){
            return categoryRepository.findCategoriesByUser(user);
        }
        else return Collections.emptyList();
    }

    /***
     * 카테고리 단건 조회
     * @param categoryId 카테고리 아이디
     * @return category
     */
    public Category findById(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

    /**
     * 카테고리를 생성한다
     * @param category 저장할 카테고리 정보
     * @return 저장한 카테고리 id
     */
    public Long save(Category category){ return categoryRepository.save(category).getId(); }


    /**
     * 카테고리를 삭제한다
     * @param category 삭제할 카테고리 정보
     */
    public void delete(Category category){ categoryRepository.delete(category);}

    /**
     * 카테고리를 정보를 수정한다.
     * @param category 카테고리 정보
     * @param categoryUpdateDto 카테고리 변경 정보
     */
    public void updateCategory(Category category, CategoryUpdateDto categoryUpdateDto) {
        category.updateName(categoryUpdateDto.getName());
        category.updateColor(categoryUpdateDto.getColor());
    }

}
