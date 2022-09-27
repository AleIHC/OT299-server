package com.alkemy.ong.data.gateways;

import com.alkemy.ong.data.entities.CategoriesEntity;
import com.alkemy.ong.data.entities.NewEntity;
import com.alkemy.ong.data.repositories.CategoriesRepository;
import com.alkemy.ong.data.repositories.NewRepository;
import com.alkemy.ong.domain.exceptions.ResourceNotFoundException;
import com.alkemy.ong.domain.news.New;
import com.alkemy.ong.domain.news.NewGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public class DefaultNewGateway implements NewGateway {

    private final NewRepository newRepository;
    private final CategoriesRepository categoriesRepository;

    @Override
    public void deleteById(Long id) {
        NewEntity news = newRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("New", "id", id));
        newRepository.deleteById(news.getId());
    }

    @Override
    public List<New> findAll() {
        return newRepository.findAll()
                .stream()
                .map(this::toModel)
                .collect(toList());
    }

    @Override
    public New findById(Long id) {
        NewEntity newEntity = newRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("New","id", id));
        return toModel(newEntity);
    }

    @Override
    public New save(New news) {
        CategoriesEntity categoriesEntity = categoriesRepository.findById(news.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category", "id", news.getCategoryId()));

        return toModel((newRepository.save(toEntity(news, categoriesEntity))));
    }


    private New toModel(NewEntity newEntity) {
        return New.builder()
                .id(newEntity.getId())
                .name(newEntity.getName())
                .content(newEntity.getContent())
                .image(newEntity.getImage())
                .categoryId(newEntity.getCategoryId().getId())
                .createdAt(newEntity.getCreatedAt())
                .updatedAt(newEntity.getUpdatedAt())
                .deleted(newEntity.isDeleted())
                .build();
    }
    public NewEntity toEntity (New news, CategoriesEntity categories){
        return NewEntity.builder()
                .name(news.getName())
                .content(news.getContent())
                .image(news.getImage())
                .categoryId(categories)
                .createdAt(news.getCreatedAt())
                .updatedAt(news.getUpdatedAt())
                .deleted(news.isDeleted())
                .build();
    }
}