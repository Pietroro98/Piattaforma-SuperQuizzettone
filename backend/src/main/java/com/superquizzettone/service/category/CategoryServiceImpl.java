package com.superquizzettone.service.category;

import com.superquizzettone.model.Category;
import com.superquizzettone.model.QuestionStatus;
import com.superquizzettone.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> listAll() {
        return categoryRepository.findByQuestionStatus(QuestionStatus.ACCEPTED);
    }
}
