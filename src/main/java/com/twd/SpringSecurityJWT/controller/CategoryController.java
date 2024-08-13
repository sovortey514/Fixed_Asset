package com.twd.SpringSecurityJWT.controller;

import com.twd.SpringSecurityJWT.dto.CategoryRequest;
import com.twd.SpringSecurityJWT.dto.ReqRes;
import com.twd.SpringSecurityJWT.entity.Category;
import com.twd.SpringSecurityJWT.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/createCategory")
    public ResponseEntity<ReqRes> saveCategory(@RequestBody CategoryRequest categoryRequest) {
        ReqRes resp = new ReqRes();
        try {
            if (categoryService.existsByName(categoryRequest.getName())) {
                resp.setMessage("Category with this name already exists");
                resp.setStatusCode(400);
            } else {
                Category categoryToSave = new Category();
                categoryToSave.setName(categoryRequest.getName());
                categoryToSave.setDescription(categoryRequest.getDescription());
                categoryToSave.setCreatedBy(categoryRequest.getCreateBy());
                categoryToSave.setCreatedAt(LocalDateTime.now());
                Category savedCategory = categoryService.save(categoryToSave);
                if (savedCategory != null && savedCategory.getId() != null) {
                    resp.setCategory(savedCategory);
                    resp.setMessage("Category Saved Successfully");
                    resp.setStatusCode(200);
                }
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

   
    @GetMapping("/categories")
    public ResponseEntity<ReqRes> getAllCategories() {
        ReqRes resp = new ReqRes();
        try {
            List<Category> categories = categoryService.findAll();
            resp.setCategories(categories);
            resp.setMessage("Categories retrieved successfully");
            resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ReqRes> getCategoryById(@PathVariable Integer id) {
    ReqRes resp = new ReqRes();
            try {
                // System.out.println("Received request to get category with ID: " + id);
                Optional<Category> categoryOpt = categoryService.findById(id);
                if (categoryOpt.isPresent()) {
                    Category category = categoryOpt.get();
                    resp.setCategory(category);
                    resp.setMessage("Category retrieved successfully");
                    resp.setStatusCode(200);
                } else {
                    resp.setMessage("Category not found");
                    resp.setStatusCode(404);
                }
            } catch (Exception e) {
                resp.setStatusCode(500);
                resp.setError(e.getMessage());
            }
        return ResponseEntity.ok(resp);
   }

   @PutMapping("/categories/{id}")
   public ResponseEntity<ReqRes> updateCategory(
           @PathVariable Integer id,
           @RequestBody Category updateCategory) {
       ReqRes resp = new ReqRes();
       try {
           Optional<Category> categoryOpt = categoryService.findById(id);
           if (categoryOpt.isPresent()) {
               Category category = categoryOpt.get();
               category.setName(updateCategory.getName());
               category.setDescription(updateCategory.getDescription());
               category.setUpdatedBy(updateCategory.getUpdatedBy());
               category.setUpdatedAt(LocalDateTime.now());
               Category savedCategory = categoryService.save(category);
               resp.setCategory(savedCategory);
               resp.setMessage("Category updated successfully");
               resp.setStatusCode(200);
           } else {
               resp.setMessage("Category not found");
               resp.setStatusCode(404);
           }
       } catch (Exception e) {
           resp.setStatusCode(500);
           resp.setError(e.getMessage());
       }
       return ResponseEntity.ok(resp);
   }
   
   @DeleteMapping("/categories/{id}")
    public ResponseEntity<ReqRes> deleteCategory(@PathVariable Integer id) {
        ReqRes resp = new ReqRes();
        try {
            boolean deleted = categoryService.deleteById(id);
            System.out.println("Test Delete " + id + deleted);
            if (deleted) {
                resp.setMessage("Category deleted successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Category not found");
                resp.setStatusCode(404);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}




