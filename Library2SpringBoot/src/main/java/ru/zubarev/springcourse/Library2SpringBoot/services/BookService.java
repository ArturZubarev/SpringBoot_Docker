package ru.zubarev.springcourse.Library2SpringBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zubarev.springcourse.Library2SpringBoot.models.Book;
import ru.zubarev.springcourse.Library2SpringBoot.models.Person;
import ru.zubarev.springcourse.Library2SpringBoot.repositories.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAllBooks(boolean sortByYear) {
        if (sortByYear)
            return bookRepository.findAll(Sort.by("year"));
        else return bookRepository.findAll();
    }

    public List<Book> findByPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year")))
                    .getContent();
        else return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book findOneBook(int id) {
        Optional<Book> foundedBook = bookRepository.findById(id);
        return foundedBook.orElse(null);
    }

    public List<Book> SearchByBookName(String query) {
        return bookRepository.findBookByNameStartingWith(query);
    }

    @Transactional
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(Book updatedBook, int id) {
        Book bookToBeUpdated = bookRepository.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setReader(bookToBeUpdated.getReader());
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    public Person getBookReader(int id) {
        return bookRepository.findById(id).map(Book::getReader).orElse(null);
    }

    @Transactional
    public void release(int id) {//Метод возвращает книгу
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setReader(null);
                    book.setTakenAt(null);
                });
    }

    @Transactional
    public void assignBook(int id, Person selectedPerson) {
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setReader(selectedPerson);
                    book.setTakenAt(new Date());
                }
        );

    }
}
