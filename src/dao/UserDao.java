package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UserDao {

    public void insert(String uname){
        String sql="insert into test values(null,?) ";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc","root","root");
            PreparedStatement pre = connection.prepareStatement(sql);
            pre.setString(1,uname);

            int i = pre.executeUpdate();
            pre.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
