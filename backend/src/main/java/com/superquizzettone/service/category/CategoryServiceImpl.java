package com.superquizzettone.service.category;

import com.superquizzettone.dto.CategoryDTO;
import com.superquizzettone.model.Category;
import com.superquizzettone.model.QuestionStatus;
import com.superquizzettone.repository.category.CategoryRepository;
import com.superquizzettone.web.api.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public Category insert(CategoryDTO categoryDTO) {
        if (categoryDTO.getQuestionStatus() == null)
            throw new BadRequestException("Lo status non può essere null");

        if (categoryDTO.getName() == null || categoryDTO.getName().isEmpty())
            throw new BadRequestException("Il nome della categoria non può essere nullo o vuoto");
        return CategoryDTO.buildModelFromDTO(categoryDTO);
    }
}
