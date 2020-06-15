package dao;


import annotations.Delete;
import annotations.Insert;
import annotations.Update;
import domain.TestDomain;
import domain.UserDomain;

import javax.xml.ws.Action;

//需要被代理的对象
public interface UuserDao {

    @Insert("insert into user values(null,#{name},#{age},#{sex})")
    public int insert(UserDomain testDomain);

    @Delete("delete from user where name=#{name}")
    public int delete(UserDomain testDomain);

}
