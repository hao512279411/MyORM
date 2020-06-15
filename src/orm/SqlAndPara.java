package orm;

import java.util.List;

public class SqlAndPara {
    String sql;
    List param;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List getParam() {
        return param;
    }

    public void setParam(List param) {
        this.param = param;
    }
}
