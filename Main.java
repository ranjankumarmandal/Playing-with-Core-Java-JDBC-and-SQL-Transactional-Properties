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

    @Override
    public int updatePerson(int id, String name){
        Boolean autocommit  = null;
        
        try {
            autocommit = connection.getAutoCommit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        int result =0 ;

        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("update person set name =? where id = ?");
            ps.setString(1, name);
            ps.setInt(2, id);
            result = ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            try {
                connection.setAutoCommit(autocommit);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    @Override
    public Person addPersonWithPreparedStatement(Person person){
        PreparedStatement ps  = null;
        try{
            ps =  connection.prepareStatement("insert into person (name, id) VALUES ( ?, ?) ;");
            ps.setString(1, person.getName());
            ps.setInt(2, person.getId());
//            ps.setInt(3, person.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return person;

    }

    @Override
    public Person addPerson(Person person) {
        Statement statement  = null;
        try{
            statement =  connection.createStatement();
            boolean result = statement.execute("insert into person (name, id) VALUES ('" + person.getName() +"' ," +person.getId() +");");
            logger.info("the result of the above query is {}" ,result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return person;

    }

    public void createTablePerson(){
        Statement statement =null;

        try {
             statement = connection.createStatement();
            statement.execute("create table if not exists person (name varchar(30), id int )");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}