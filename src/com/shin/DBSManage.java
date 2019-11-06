package com.shin;
import java.sql.*;

public class DBSManage {
    private Connection conn;
    private Statement stat;
    private String url="jdbc:mysql://127.0.0.1:3306/hms_database";
    private String user="root";
    private  String passwd="168168";

    public DBSManage() throws Exception {
        //连接数据库
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection(url,user,passwd);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
