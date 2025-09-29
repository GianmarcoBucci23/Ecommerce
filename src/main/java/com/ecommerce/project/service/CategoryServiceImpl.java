package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty())
            throw new APIException("Non ci sono categorie presenti ");

        List<CategoryDTO> categoryDTOList = categories.stream()
                .map(category -> modelMapper.map(category,CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOList);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDB != null)
            throw new APIException("la categoria con il nome " + category.getCategoryName() + " esiste già!!!");
        Category categoryToSave = categoryRepository.save(category);
        return modelMapper.map(categoryToSave,CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category categoryToDelete = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category","categoryId",categoryId));


        categoryRepository.delete(categoryToDelete);

        return modelMapper.map(categoryToDelete,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category categoryFromDB = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category","categoryId",categoryId));

        Category category = modelMapper.map(categoryDTO,Category.class);
        category.setCategoryId(categoryId);
        Category categoryToUpdate = categoryRepository.save(category);
        return modelMapper.map(categoryToUpdate,CategoryDTO.class);

    }



}
