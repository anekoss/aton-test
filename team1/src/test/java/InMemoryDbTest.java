import com.aton.app.InMemoryDb;
import com.aton.app.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryDbTest {
    private static final Map<Double, List<Person>> PERSON_VALUES;
    private static final Map<String, List<Person>> PERSON_NAMES;
    private static final Map<Long, Person> PERSON_ACCOUNTS;
    private static final InMemoryDb IN_MEMORY_DB;
    private static final Person EXIST_PERSON = new Person(9999L, "Маша", 0.0D);
    private static final Person NEW_PERSON = new Person(234678L, "Иван", 2035.34D);

    static {
        Person ivan = new Person(342L, "Иван", 3456.74D);
        Person oleg = new Person(1000L, "Oлег", 2035.34D);
        PERSON_ACCOUNTS = new HashMap<>(Map.of(EXIST_PERSON.account(), EXIST_PERSON, ivan.account(), ivan, oleg.account(), oleg));
        PERSON_NAMES = new HashMap<>(Map.of(EXIST_PERSON.name(), new ArrayList<>(List.of(EXIST_PERSON)), ivan.name(), new ArrayList<>(List.of(ivan
        )), oleg.name(), new ArrayList<>(List.of(oleg))));
        PERSON_VALUES = new HashMap<>(Map.of(EXIST_PERSON.value(), new ArrayList<>(List.of(EXIST_PERSON)), ivan.value(), new ArrayList<>(List.of(ivan
        )), oleg.value(), new ArrayList<>(List.of(oleg))));
        IN_MEMORY_DB = new InMemoryDb(PERSON_ACCOUNTS, PERSON_NAMES, PERSON_VALUES);
    }

    @AfterEach
    void clean() {
        PERSON_ACCOUNTS.remove(NEW_PERSON.account());
        PERSON_NAMES.computeIfAbsent(NEW_PERSON.name(), v -> new ArrayList<>()).remove(NEW_PERSON);
        PERSON_VALUES.computeIfAbsent(NEW_PERSON.value(), v -> new ArrayList<>()).remove(NEW_PERSON);
    }

    @Test
    void add_shouldCorrectlyAddPerson() {
        IN_MEMORY_DB.add(NEW_PERSON);
        assertAll(() -> {
            assertEquals(PERSON_ACCOUNTS.get(234678L), NEW_PERSON);
            assertTrue(PERSON_NAMES.get(NEW_PERSON.name()).contains(NEW_PERSON));
            assertTrue(PERSON_VALUES.get(NEW_PERSON.value()).contains(NEW_PERSON));
        });
    }

    @Test
    void add_shouldThrowExceptionIfPersonAccountExist() {
        IN_MEMORY_DB.add(NEW_PERSON);
        assertThrows(IllegalArgumentException.class, () -> IN_MEMORY_DB.add(NEW_PERSON));
    }

    @Test
    void remove_shouldCorrectlyRemovePerson() {
        IN_MEMORY_DB.remove(EXIST_PERSON);
        assertAll(() -> {
            assertNull(PERSON_ACCOUNTS.get(9999L));
            assertEquals(PERSON_NAMES.get("Маша"), List.of());
            assertEquals(PERSON_VALUES.get(0.0D), List.of());
        });
        IN_MEMORY_DB.add(EXIST_PERSON);
    }

    @Test
    void remove_shouldThrowExceptionIfPersonNoExist() {
        assertThrows(IllegalArgumentException.class, () -> IN_MEMORY_DB.remove(NEW_PERSON));
    }

    @Test
    void findByAccount_shouldCorrectlyReturnPerson() {
        Optional<Person> optionalPerson = IN_MEMORY_DB.findByAccount(EXIST_PERSON.account());
        assert optionalPerson.isPresent();
        assertEquals(optionalPerson.get(), EXIST_PERSON);
    }

    @Test
    void findByAccount_shouldReturnEmptyOptionalIfNoExist() {
        Long account = 11111L;
        assert IN_MEMORY_DB.findByAccount(account).isEmpty();
    }

    @Test
    void findByName_shouldCorrectlyReturnPersonsList() {
        String name = "Иван";
        IN_MEMORY_DB.add(NEW_PERSON);
        List<Person> persons = IN_MEMORY_DB.findByName(name);
        assert persons.size() == 2;
        assert persons.contains(NEW_PERSON);
    }

    @Test
    void findByName_shouldReturnEmptyListIfNoExist() {
        String name = "Eкатерина";
        assert IN_MEMORY_DB.findByName(name).isEmpty();
    }

    @Test
    void findByValue_shouldCorrectlyReturnPersonList() {
        IN_MEMORY_DB.add(NEW_PERSON);
        List<Person> persons = IN_MEMORY_DB.findByValues(NEW_PERSON.value());
        assert persons.size() == 2;
        assert persons.contains(NEW_PERSON);
    }

    @Test
    void findByValue_shouldReturnEmptyListIfNoExist() {
        assert IN_MEMORY_DB.findByValues(345.34D).isEmpty();
    }

    @Test
    void update_shouldCorrectlyUpdatePerson() {
        Double newValue = EXIST_PERSON.value() - 23.0D;
        Person updatedPerson = new Person(EXIST_PERSON.account(), EXIST_PERSON.name(), newValue);
        IN_MEMORY_DB.update(updatedPerson);
        assertAll(
                () -> {
                    assertEquals(PERSON_ACCOUNTS.get(updatedPerson.account()), updatedPerson);
                    assertEquals(PERSON_NAMES.get(updatedPerson.name()), List.of(updatedPerson));
                    assertEquals(PERSON_VALUES.get(updatedPerson.value()), List.of(updatedPerson));
                    assert PERSON_VALUES.get(EXIST_PERSON.value()).isEmpty();
                }
        );
    }

}
