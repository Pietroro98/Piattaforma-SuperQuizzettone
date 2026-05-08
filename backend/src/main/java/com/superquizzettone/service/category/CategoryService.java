package com.superquizzettone.service.category;
import com.superquizzettone.dto.CategoryDTO;
import com.superquizzettone.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listAll();
    Category insert(CategoryDTO categoryDTO);
}
