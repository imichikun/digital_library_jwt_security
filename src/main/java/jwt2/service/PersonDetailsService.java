package jwt2.service;

import jakarta.transaction.Transactional;
import jwt2.model.Person;
import jwt2.repository.PersonRepository;
import jwt2.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Transactional
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> possiblePerson = personRepository.findByUsername(username);

        if (possiblePerson.isEmpty())
            throw new UsernameNotFoundException("User is not found !");

        return new PersonDetails(possiblePerson.get());
    }
}