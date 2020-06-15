package orm;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlUtle {

    //可以分析 sql 将里面的#{}转换成 ? 并且将 #{}里的值按顺序放入list集合
    public static SqlAndPara SqlParsing(String sql){
        //insert into test values(#{AA},#{AA})
        StringBuffer sb=new StringBuffer();
        List<String> parameters=new ArrayList();
        int sou = sql.indexOf("#{");
        int wei = sql.indexOf("}");
        while (wei>sou) {
            System.out.println(sou + "-------------" + wei);
            parameters.add(sql.substring(sou + 2, wei));
            sb.append(sql.substring(0, sou));
            sb.append("?");
            sql = sql.substring(wei + 1);
            sou = sql.indexOf("#{");
            wei = sql.indexOf("}");
        }
        sb.append(sql);
        SqlAndPara sqlAndPara=new SqlAndPara();
        sqlAndPara.setSql(sb.toString());
        sqlAndPara.setParam(parameters);
        System.out.println("解析完成 SQL 是："+sb+"\n参数是："+parameters);
        return sqlAndPara;
    }


    //将传送过来的实体类或者集合 放入SQL 语句中
    public static void setObject( PreparedStatement ps,List<String> params,Object obj) throws Exception {

        if (obj.getClass() == String.class){ //传入的是一个字符串
            ps.setString(1,(String) obj);
        }else if (obj.getClass() == int.class || obj.getClass() == Integer.class){
            ps.setInt(1,(Integer)obj);
        }else if (obj instanceof Map ){ //Map集合
            Map map = (Map) obj;
            for (int i = 0; i < params.size(); i++) {
                System.out.println("当前遍历第："+i);
                ps.setObject(i+1,map.get(params.get(i)));
            }
        }else { //实体类
            for (int i = 0; i <params.size() ; i++) {
                Class objClass = obj.getClass();
                //获取当前参数 对应实体类里get方法
                Method method = objClass.getMethod(getMethodGetName(params.get(i)));
                Object param = method.invoke(obj);
                System.out.println("获取到的值："+param);
                if (param==null){
                    ps.setObject(i+1,null);
                }else {
                    ps.setObject(i+1,param);
                }

            }
        }
    }


    //将返回值 放入 传过来的集合类型中
    public static Object getReturmObject(ResultSet resultSet,Class clazz) throws Exception {
        List<Object> list =new ArrayList();
        Object obj=clazz.newInstance();
      if (obj instanceof Map){ //传过来的是map集合

          while (resultSet.next()){
              obj=clazz.newInstance();
              Map map =(Map)obj;
              //循环将每列的参数放入集合中
              for (int i = 1; i < resultSet.getMetaData().getColumnCount()+1; i++) {
                  //获取列明
                  String columnName = resultSet.getMetaData().getColumnName(i);
                  //获取值
                    map.put(columnName,resultSet.getObject(columnName));
              }
              list.add(map);
          }

      }else {//传过来的是对象类型

          while (resultSet.next()){
              obj=clazz.newInstance();
              //循环将每列的参数放入集合中
              for (int i = 1; i < resultSet.getMetaData().getColumnCount()+1; i++) {
                  //获取列明
                  String columnName = resultSet.getMetaData().getColumnName(i);
                  ///获取列类型
                  String columnTypeName = resultSet.getMetaData().getColumnClassName(i);

                  Class aClass = Class.forName(columnTypeName);
                  //通过列明反射获取set方法
                  String methodSetName = getMethodSetName(columnName);
                  //获取set方法
                  Method method = clazz.getMethod(methodSetName,aClass);
                  //执行set方法
                  method.invoke(obj,resultSet.getObject(columnName));

                  list.add(obj);
//                  System.out.println("当前对象的值是："+(TestDomain)obj);
              }
          }
      }
        return list;
    }



    //利用参数获取当前方法的get方法名
    private static String getMethodGetName(String params){
        StringBuffer sb =new StringBuffer("get");
        String head = params.substring(0,1).toUpperCase();
        String tail = params.substring(1);
        sb.append(head);
        sb.append(tail);
        System.out.println("当前的方法名是："+sb);
        return sb.toString();
    }

    //利用参数获取当前方法的set方法名
    private static String getMethodSetName(String params){
        StringBuffer sb =new StringBuffer("set");
        String head = params.substring(0,1).toUpperCase();
        String tail = params.substring(1);
        sb.append(head);
        sb.append(tail);
        System.out.println("当前set的方法名是："+sb);
        return sb.toString();
    }

}
