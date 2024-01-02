package com.devtiro.books.repositories;

import com.devtiro.books.domain.BooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<BooksEntity,String> {



}
