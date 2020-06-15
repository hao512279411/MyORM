import dao.UuserDao;
import domain.TestDomain;
import domain.UserDomain;
import orm.SqlSession;

public class Test {
    public static void main(String[] args) {

//        ConnectionPool connectionPool =ConnectionPool.newInstance();
//        Connection conn = connectionPool.getConnection();
//        try {
//            PreparedStatement ps = conn.prepareStatement("insert into test values(null,?) ");
//            ps.setString(1,"周响应");
//            int i=ps.executeUpdate();
//            System.out.println(i);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        //==================================================================================

//        SqlSession sqlSession=new SqlSession();
//        sqlSession.insert("insert into test values(null,?) ","罗晶晶");


//        SqlUtle sqlUtle=new SqlUtle();
//        sqlUtle.SqlParsing("insert into test values(null,#{uname})");
//
//        Map map=new HashMap<String, String>();
//        map.put("uname","张三");

        TestDomain testDomain =new TestDomain();
        testDomain.setId(1);
//        testDomain.setUname("张三");
//        testDomain.setSex("女");

        SqlSession sqlSession =new SqlSession();
//        sqlSession.insert("insert into test values(null,#{uname},#{sex}) ",testDomain);
//        sqlSession.insert("delete from test where uname=#{uname} ",testDomain);

//        TestDomain select = sqlSession.select("select * from test where uname=#{uname} ", testDomain, TestDomain.class);
//        List<Map> select = sqlSession.select("select * from user", null, HashMap.class);

//        List<UserDomain> select = sqlSession.select("select * from user", null, UserDomain.class);
//        System.out.println(select);

        UuserDao userDao = sqlSession.getMapper(UuserDao.class);
        UserDomain user = new UserDomain();
        user.setName("张三");
        int a=userDao.delete(user);
        System.out.println(a);



    }
}
