package connection;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

//连接池，继承适配器
public class ConnectionPool  {
    //1、将一定数量的连接词 放入list集合中
    private  List<MyConnection> connectionList=new ArrayList();
    private static ConnectionPool connectionPool;
    private ConnectionPool(){}

    //单列模式 返回线程池
    public static ConnectionPool newInstance(){
        if (connectionPool==null){
            synchronized (ConnectionPool.class){
                if (connectionPool==null){
                    connectionPool= new ConnectionPool();
                }
            }
        }
        return connectionPool;
    }


    {
        for (int i = 0; i < 10; i++) {
            connectionList.add(new MyConnection());
        }
    }


    //2、给外部获取conn
    public Connection getConnection(){
        //遍历连接池
        for (int i = 0; i < connectionList.size(); i++) {
            //寻找空闲的链接
            if (!connectionList.get(i).isState()){
                synchronized (ConnectionPool.class) {
                    if (!connectionList.get(i).isState()) {
                        return connectionList.get(i).getConnection();
                    }
                }
            }
        }
        return null;
    }




}
