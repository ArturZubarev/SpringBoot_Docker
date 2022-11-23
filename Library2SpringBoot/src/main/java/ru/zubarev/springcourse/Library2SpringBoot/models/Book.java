package ru.zubarev.springcourse.Library2SpringBoot.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    @NotEmpty(message = "Название книги не должно быть пустым")
    @Size(min = 2, max = 100, message = "Название книги должно быть от 2 до 100 символов длинной")
    private String name;
    @Column(name = "author")
    @NotEmpty(message = "Имя автора не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя автора быть от 2 до 100 символов длинной")
    private String author;
    @Column(name = "year_of_publication")
    @Min(value = 1500, message = "Год должен быть больше, чем 1500")
    private int yearOfPublication;

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Person getReader() {
        return reader;
    }

    public void setReader(Person reader) {
        this.reader = reader;
    }

    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;
    @Transient
    private boolean expired = false;
    @ManyToOne
    @JoinColumn(name = "person_id",
            referencedColumnName = "id")
    private Person reader;


    public Book() {
    }

    public Book(int id, String name, String author, int yearOfPublication, Person reader) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.reader = reader;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }
}