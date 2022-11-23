package ru.zubarev.springcourse.Library2SpringBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zubarev.springcourse.Library2SpringBoot.models.Book;
import ru.zubarev.springcourse.Library2SpringBoot.models.Person;
import ru.zubarev.springcourse.Library2SpringBoot.services.BookService;
import ru.zubarev.springcourse.Library2SpringBoot.services.PersonService;


import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {
    private final PersonService personService;
    private final BookService bookService;
    @Autowired
    public BookController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }



    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
        if (page == null || booksPerPage == null)
            model.addAttribute("books", bookService.findAllBooks(sortByYear));
        else
            model.addAttribute("books", bookService.findByPagination(page, booksPerPage, sortByYear));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findOneBook(id));
        Person bookOwner = bookService.getBookReader(id);
        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else model.addAttribute("people", personService.findAll());
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book Book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";
        bookService.saveBook(Book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOneBook(id));
        return "books/edit";
    }

    @PatchMapping("/{id}/edit")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";
        bookService.updateBook(book, id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        bookService.assignBook(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", bookService.SearchByBookName(query));
        return "books/search";
    }
}



