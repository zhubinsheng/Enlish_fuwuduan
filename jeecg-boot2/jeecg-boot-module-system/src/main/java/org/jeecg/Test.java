package org.jeecg;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class Test {
    private static int write;


    public static void main(String args[]) {
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("chi_sim");
        tesseract.setDatapath("D:\\Eliao\\Enlish_fuwuduan\\tessdata");
        try {
            String str = tesseract.doOCR(new File("D:\\Eliao\\Enlish_fuwuduan\\12.png"));
            System.out.println(str);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }




   /* public static void main(String args[]) {
        File file = new File("D:\\english\\闪记词库\\高中词汇\\北师大高中必修1.txt");
        txt2String(file);

       *//* String str = new String("Welcome|to|Runoob|123");

        System.out.println("- 分隔符返回值 :" );
        for (String retval: str.split("\\|")){
            System.out.println(retval);
        }

        System.out.println("");
        System.out.println("- 分隔符设置分割份数返回值 :" );
        for (String retval: str.split("-", 2)){
            System.out.println(retval);
        }

        System.out.println("");
        String str2 = new String("www.runoob.com");
        System.out.println("转义字符返回值 :" );
        for (String retval: str2.split("\\.", 3)){
            System.out.println(retval);
        }

        System.out.println("");
        String str3 = new String("acount=? and uu =? or n=?");
        System.out.println("多个分隔符返回值 :" );
        for (String retval: str3.split("and|or")){
            System.out.println(retval);
        }*//*
    }*/
    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            Map<String, String> map = new HashMap<String, String>();
         //   BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件

            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis,"GBK");
            BufferedReader br = new BufferedReader(isr);




            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                String[] content = s.split("丨");

                if (content != null && content.length == 2) {
                    System.out.println(content[0]);
                    System.out.println(content[1]);
                    map.put(content[0],content[1]);

                }
                write+= JDBCAdd.Insert(content[0],content[1]);


                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public static class JDBCAdd {


        public static int Insert(String... parms){
        Connection conn=null;
        PreparedStatement preStatement=null;//创建PreparedStatement对象
        try {
            //1、准备Connection连接数据库
            conn= BeyondbConnection.getConnection();
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
            //6、关闭数据库等
            /*JDBCTools.closeConnection(null, preStatement, conn);*/
        }
    }
    }

    public static class BeyondbConnection {
        public static final String DBDRIVER = "com.mysql.jdbc.Driver";
        public static final String DBURL = "jdbc:mysql://47.101.210.7:3306/vocabulary?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        public static final String DBUSER = "xr";
        public static final String DBPASSWORD = "tiske6Ry2ACASPX4";

        public static Connection getConnection() throws Exception{
           /* //创建Properties对象
            Properties pro=new Properties();
            //获取输入流
            InputStream in=
                    Test.BeyondbConnection.class.getClassLoader().getResourceAsStream("jdbc.properties");
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
}