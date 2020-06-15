package orm;


import annotations.Delete;
import annotations.Insert;
import connection.ConnectionPool;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Mybatis的主要类 里面有各种链接数据库并且执行SQL语句的方法
public class SqlSession {
    //创建连接池
    private ConnectionPool connectionPool= ConnectionPool.newInstance();

    //1、增删改
    public int insert(String sql,Object obj){
        Connection conn =connectionPool.getConnection();
        PreparedStatement ps = null;
        try {
//            //通过工具 获取 sql和参数
//            SqlAndPara sqlAndPara = SqlUtle.SqlParsing(sql);
//            //SQL传入 数据库
//            ps = conn.prepareStatement(sqlAndPara.getSql());
//            //将参数放入 ? 中
//            if (obj !=null){
//                SqlUtle.setObject(ps,sqlAndPara.getParam(),obj);
//            }
            //提取出来的方法 将传入完整的SQL 到数据库
            ps = this.setSqlAndParam(sql, obj, conn);


            return ps.executeUpdate();

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return 0;
    }
    //2、查
    public <T>T select(String sql,Object obj,Class clazz){
        @SuppressWarnings("DuplicatedCode") Connection conn =connectionPool.getConnection();
        @SuppressWarnings("DuplicatedCode") PreparedStatement ps = null;
        try {
            //提取出来的方法 将传入完整的SQL 到数据库
            ps = this.setSqlAndParam(sql, obj, conn);
            //获取返回值
            ResultSet resultSet = ps.executeQuery();
            Object returmObject = SqlUtle.getReturmObject(resultSet, clazz);
            return (T) returmObject;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

//============================================================================================================
    //获取连接池 并且将sql 和参数传入sql
    private PreparedStatement setSqlAndParam(String sql,Object obj,Connection conn) throws Exception {
            //通过工具 获取 sql和参数
            SqlAndPara sqlAndPara = SqlUtle.SqlParsing(sql);
            //SQL传入 数据库
            PreparedStatement ps = conn.prepareStatement(sqlAndPara.getSql());
            //将参数放入 ? 中
            if (obj != null) {
                SqlUtle.setObject(ps, sqlAndPara.getParam(), obj);
            }
            return ps;
    }

//=============================================================================================================
    //动态代理
    public <T>T getMapper(Class clazz){
        ClassLoader classLoader = clazz.getClassLoader();
        Class[] interfaces = new Class[] {clazz};
        InvocationHandler h = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // proxy代理对象
                // method被代理的那个方法
                // args被代理方法的参数

                //从方法上获取SQL
                //1、先获取注解
                Annotation[] annotationsa = method.getAnnotations();
                System.out.println(annotationsa.length);
                Annotation annotations = method.getAnnotations()[0];
                //2、获取注解的Class类型
                Class type  = annotations.annotationType();
                //3、通过注解类 获取里面的value方法
                Method value = type.getMethod("value");
                //4、通过方法的返回值 获取到SQL
                String sql = (String) value.invoke(annotations);
                if (type  == Insert.class){
                    System.out.println("这是一个Insert 注解");
                    int insert = insert(sql, args[0]);
                    return insert;
                }else if(type  == Delete.class){
                    System.out.println("这是一个Delete 注解");
                    int insert = insert(sql, args[0]);
                    return insert;
                }





                return null;
            }
        };


        return  (T)Proxy.newProxyInstance(classLoader,interfaces,h);
    }



}
