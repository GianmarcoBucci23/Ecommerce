package com.ecommerce.project.controller;


import com.ecommerce.project.configuration.AppConstans;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategory(@RequestParam(name = "pageNumber",defaultValue = AppConstans.PAGE_NUMBER ,required = false) Integer pageNumber,
                                                           @RequestParam(name = "pageSize", defaultValue = AppConstans.PAGE_SIZE ,required = false) Integer pageSize,
                                                           @RequestParam(name = "sortBy", defaultValue = AppConstans.SORT_CATEGORIES_BY,required = false) String sortBy,
                                                           @RequestParam(name = "sortOrder", defaultValue = AppConstans.SORT_ORDER, required = false) String sortOrder){
        CategoryResponse categories = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO categorySavedDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(categorySavedDTO, HttpStatus.CREATED);
    }

    @PutMapping("/public/categories/{categoryId}")
    //@RequestMapping(value = "/public/categories/{categoryId}",method = RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO,@PathVariable Long categoryId){

        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    //@RequestMapping(value = "/admin/categories/{categoryId}", method = RequestMethod.DELETE)
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){

        CategoryDTO deletedCategoryDTo = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategoryDTo, HttpStatus.OK);
    }

}
