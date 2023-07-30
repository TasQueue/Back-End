package com.example.taskqueue.category.service;

import com.example.taskqueue.category.entity.Category;
import com.example.taskqueue.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 전체를 조회한다
     * @param userId 현재 유저
     * @return 해당 유저의 category 리스트
     */
    public List<Long> findAllById(Long userId){ return categoryRepository.getCategoryList(userId);}

    /***
     * 카테고리 조회
     * @param categoryId 카테고리 아이디
     * @return category
     */
    public Category findById(Long categoryId){ return categoryRepository.findById(categoryId).orElseThrow(()->{
        return new IllegalArgumentException("category Id를 찾을 수 없습니다.");
    });}

    /**
     * 카테고리를 생성한다
     * @param category 저장할 카테고리 정보
     * @return 저장한 카테고리 id
     */
    public Long save(Category category){ return categoryRepository.save(category).getId(); }

    /**
     * 카테고리 수정한다
     * @param id 수정할 카테고리 id, name 수정 내용
     * @return 수정한 카테고리 id
     */
//    public Long modify(Long id, String name) {
//        Category category = categoryRepository.findById(id).orElseThrow();
//        category.updateName(name);
//
//        return id;
//    }

    /**
     * 카테고리를 삭제한다
     * @param category 삭제할 카테고리 정보
     */
    public void delete(Category category){ categoryRepository.delete(category);}

}
