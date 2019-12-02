package com.shin;
import java.sql.*;

public class Database {
    private Connection conn;
    private Statement stat;
    private String url="jdbc:mysql://127.0.0.1:3306/hms_database?useSSL=false&serverTimezone=UTC";
    private String user="root";
    private  String passwd="168168";

    public Database(){
        //连接数据库
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection(url,user,passwd);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ResultSet QueryInfo(String sql){
        stat = null;
        ResultSet rs = null;
        try {
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rs = stat.executeQuery(sql);
        } catch (Exception ex) {
            System.err.println("executeQuery:" + ex.getMessage());
        }
        return rs;
    }
    public void UpdataInfo(String sql){
        stat = null;
        try {
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stat.executeUpdate(sql);
        } catch (Exception ex) {
            System.err.println("executeQuery:" + ex.getMessage());
        }
    }
    public void Insert(String target,String []values){
        String data="";
        for(int i=0;i<values.length;i++){
            if(i!=values.length-1)
                data+=values[i]+"','";
            else
                data+=values[i];
        }
        String sql="insert into "+target+" values('"+data+"')";
        UpdataInfo(sql);
    }
    //开启事务
    public void startTransaction(){
        try{
            conn.setAutoCommit(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //提交事务
    public void commitTransaction(){
        try{
            conn.commit();
            conn.setAutoCommit(true); //恢复默认状态
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //回滚事务
    public void rollbackTransaction(){
        try{
            conn.rollback();
            conn.setAutoCommit(true); //恢复默认状态
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
