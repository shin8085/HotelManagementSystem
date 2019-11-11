package com.shin;

import com.sun.jdi.BooleanType;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class RoomInfoLayout {
    private JPanel panel_main;
    private Database database=new Database();
    public Component getMainPanel(){
        panel_main=new JPanel();
        JPanel panel=new JPanel(new GridLayout(3,4,3,3));
        try{
            ResultSet resultSet=database.QueryInfo("select rid,rtype from room");
            while(resultSet.next()){
                JButton button=new JButton("<html>"+resultSet.getString("rid")+"<br>"+resultSet.getString("rtype")+"</html>");
                ResultSet resultSet1=database.QueryInfo("select * from check_in where rid="+resultSet.getString("rid"));
                if(resultSet1.next()){
                    button.setBackground(Color.red);
                }
                else{
                    button.setBackground(Color.green);
                }
                button.setPreferredSize(new Dimension(100,100));
                panel.add(button);
                panel.setBorder(BorderFactory.createEmptyBorder(110,0,0,0));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        panel_main.add(panel);
        return panel_main;
    }
}
