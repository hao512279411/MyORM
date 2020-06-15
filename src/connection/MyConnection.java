package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//链接类
public class MyConnection extends ConnectionAdapter{
    //链接属性
    private Connection connection;
    //状态  默认false代表未使用 true代表被使用
    private boolean state =false;
    //创建类的时候自动创建链接
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jtdb","root","root");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //获取当前conn的状态
    public boolean isState(){
        return state;
    }

    //使外界获取conn
    public Connection getConnection(){
        this.state=true;
        return this.connection;
    }

    //获取传输器
    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {

        return connection.prepareStatement(sql);
    }

    //重写关闭传输器  将此conn换成空闲
    @Override
    public void close() throws SQLException {
        this.state=false;
    }
}
