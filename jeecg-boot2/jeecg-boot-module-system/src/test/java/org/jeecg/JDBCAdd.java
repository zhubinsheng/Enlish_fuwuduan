

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCAdd {
    
    
    public static int Insert (String... parms) throws SQLException {
    Connection conn=null;
    PreparedStatement preStatement=null;//创建PreparedStatement对象
    try {
        //1、准备Connection连接数据库
        conn=BeyondbConnection.getConnection();
        //2、准备sql语句
        //sql语句不再采用拼接方式，应用占位符问号的方式写sql语句。
        String sql="insert into CETFOUR(word, meaning) values(?,?)";
        //3、准备prepareStatement
        //对占位符设置值，占位符顺序从1开始，第一个参数是占位符的位置，第二个参数是占位符的值。
        preStatement=conn.prepareStatement(sql);
        //4、占位符设置值
        for(int i=0;i<parms.length;i++){
            preStatement.setObject(i+1, parms[i]);
        }
        //5、执行sql
        return preStatement.executeUpdate();
        
    } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
        return 0;
    }finally{
        conn.close();
        //6、关闭数据库等
        /*JDBCTools.closeConnection(null, preStatement, conn);*/
    }
}
}