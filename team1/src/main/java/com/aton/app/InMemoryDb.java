package com.aton.app;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class InMemoryDb {
    private final Map<Long, Person> personAccounts;
    private final Map<String, List<Person>> personNames;
    private final Map<Double, List<Person>> personValues;

    public void add(Person person) {
        if (personAccounts.get(person.account()) != null) {
            throw new IllegalArgumentException();
        }
        personAccounts.put(person.account(), person);
        personNames.computeIfAbsent(person.name(), v -> new ArrayList<>()).add(person);
        personValues.computeIfAbsent(person.value(), v -> new ArrayList<>()).add(person);
    }

    public void remove(Person person) {
        if (personAccounts.remove(person.account()) == null) {
            throw new IllegalArgumentException();
        }
        personNames.computeIfAbsent(person.name(), v -> new ArrayList<>()).remove(person);
        personValues.computeIfAbsent(person.value(), v -> new ArrayList<>()).remove(person);
    }

    public Optional<Person> findByAccount(Long account) {
        return Optional.ofNullable(personAccounts.get(account));
    }

    public List<Person> findByName(String name) {
        return personNames.getOrDefault(name, List.of());
    }

    public List<Person> findByValues(Double value) {
        return personValues.getOrDefault(value, List.of());
    }

    public void update(Person person) {
        Person prevPerson = findByAccount(person.account()).orElseThrow(IllegalArgumentException::new);
        remove(prevPerson);
        add(person);
    }


}
