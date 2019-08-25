import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BeyondbConnection {
    public static final String DBDRIVER = "com.mysql.jdbc.Driver";
    public static final String DBURL = "jdbc:mysql://localhost:3306/jeecg?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    public static final String DBUSER = "root";
    public static final String DBPASSWORD = "wyx831107";

    public static Connection getConnection() throws Exception{
       /* //创建Properties对象
        Properties pro=new Properties();
        //获取输入流
        InputStream in=
                BeyondbConnection.class.getClassLoader().getResourceAsStream("jdbc.properties");
        //加载输入流
        pro.load(in);

        String driver=pro.getProperty("driver");
        String jdbcUrl=pro.getProperty("jdbcUrl");
        String user=pro.getProperty("user");
        String password=pro.getProperty("password");*/
        //加载数据库驱动程序
        Class.forName(DBDRIVER);
        //通过DriverManager的getConnection()方法获取数据库连接
        Connection conn=DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
        return conn;
    }
}