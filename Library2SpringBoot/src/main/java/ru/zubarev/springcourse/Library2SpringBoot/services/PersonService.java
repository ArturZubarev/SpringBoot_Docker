package ru.zubarev.springcourse.Library2SpringBoot.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zubarev.springcourse.Library2SpringBoot.models.Book;
import ru.zubarev.springcourse.Library2SpringBoot.models.Person;
import ru.zubarev.springcourse.Library2SpringBoot.repositories.PersonRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    public List<Person> findAll(){
        return personRepository.findAll();
    }
    public Person findOnePerson(int id){
        Optional<Person> foundedPerson=personRepository.findById(id);
        return foundedPerson.orElse(null);
    }
    @Transactional
    public void save(Person person){
        personRepository.save(person);
    }
    @Transactional
    public void update(Person updatedPerson,int id){
        updatedPerson.setId(id);
        personRepository.save(updatedPerson);

    }
    @Transactional
    public void delete(int id){
        personRepository.deleteById(id);
    }
    public Optional<Person> getPersonByName(String name){
        return personRepository.findByName(name);
    }
    public List<Book> getBooksByPersonId(int id){
        Optional<Person> person=personRepository.findById(id);
        if(person.isPresent()){
            Hibernate.initialize(person.get().getBooks());
            /* Ниже с помощью итератора проходим по списку книг, потэтому они дб погдргужены.
            Также, ниже идет проверка на просрочку срока возврата книги
             */
            person.get().getBooks().forEach(book->{
                long diffInMillies=Math.abs(book.getTakenAt().getTime()
                -new Date().getTime());
                if (diffInMillies>864000000)//если разница больше этой единицы, то книга считается не возвращенной вовремя
                    book.setExpired(true);
            });
            return person.get().getBooks();
        }
        else return Collections.emptyList();
    }

}
