package com.twd.SpringSecurityJWT.service;

import com.twd.SpringSecurityJWT.entity.Category;
import com.twd.SpringSecurityJWT.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Integer id) {
        // System.out.println("Finding category with ID: " + id);
        return categoryRepository.findById(id);
    }
    
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return categoryRepository.findByName(name).isPresent();
    }
    
    

    public boolean deleteById(Integer id) {
        boolean checkId = categoryRepository.existsById(id);
        if(checkId) {
            categoryRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
