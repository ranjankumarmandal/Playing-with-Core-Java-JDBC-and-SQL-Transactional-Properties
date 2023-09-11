import java.util.*;

public interface JdbcInterface {
    List<Person> getAllPerson();
    int updatePerson(int id, String name);
    Person addPersonWithPreparedStatement(Person person);
    Person addPerson(Person person);
}