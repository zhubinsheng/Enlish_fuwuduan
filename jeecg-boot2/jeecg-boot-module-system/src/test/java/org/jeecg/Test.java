import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Test {
    private static int write;

    public static void main(String args[]) {
        File file = new File("G:\\闪记词库\\初中词汇\\人教版8年级上.txt");
txt2String(file);

       /* String str = new String("Welcome|to|Runoob|123");

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
        }*/
    }
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
               if(!s.equals("")) {
                   String[] content = s.split("丨");


                   if (content != null && content.length == 2) {
                       System.out.println(content[0]);
                       System.out.println(content[1]);
                       map.put(content[0], content[1]);

                   }
                   if(content.length!=2){
                       System.out.println("词库格式出错了");
                       System.out.println(content[0]);
                   }
                   write += JDBCAdd.Insert(content[0], content[1]);


                   result.append(System.lineSeparator() + s);
               }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
}