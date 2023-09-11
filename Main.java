public class Main implements JdbcInterface {
    private Connection connection;

    @Override
    public List<Person> getAllPerson(){
        List<Person> list = new ArrayList<>();
        Statement statement  = null;

        try{
            statement =  connection.createStatement();
            ResultSet resultSet =statement.executeQuery("select * from person");
            while(resultSet.next()){
                Person p = new Person(resultSet.getString("name"), resultSet.getInt("id"));
                list.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }
}