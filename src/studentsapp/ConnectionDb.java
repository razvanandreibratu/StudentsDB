package studentsapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb
{
    private static ConnectionDb instance;
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/students";
    private String username = "java";
    private String password = "password";

    private ConnectionDb() throws SQLException
    {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch(ClassNotFoundException ex) {
            System.out.println("Database connection failed..." + ex.getMessage());
        }

    }
    public Connection getConnection()
    {
        return connection;
    }
    public static ConnectionDb getInstance() throws SQLException
    {
        if (instance == null)
        {
            instance = new ConnectionDb();
        } else if (instance.getConnection().isClosed())
        {
            instance = new ConnectionDb();
        }
        return instance;
    }

}
