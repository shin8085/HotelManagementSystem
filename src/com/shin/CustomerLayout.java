package com.shin;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;

public class CustomerLayout {
    Database database=new Database();
    public Component getMainPanel(){
        JPanel panel_main=new JPanel(new BorderLayout());
        String[] tableTitle={"身份证号","姓名","性别","电话号码","房间号","入住时间","备注"};
        String[][] rowdata=new String[1][];
        try {
            ResultSet rowCount=database.QueryInfo("select count(*) from customer");
            ResultSet resultSet=database.QueryInfo("select * from customer,check_in where customer.idnum=check_in.idnum");
            int row=0;
            if(rowCount.next()){
                String data[][]=new String[rowCount.getInt("count(*)")][];
                rowdata=data;
            }
            while(resultSet.next()){
                rowdata[row]=new String[7];
                rowdata[row][0]=resultSet.getString("idnum");
                rowdata[row][1]=resultSet.getString("cname");
                rowdata[row][2]=resultSet.getString("sex");
                rowdata[row][3]=resultSet.getString("telphone");
                rowdata[row][4]=resultSet.getString("rid");
                rowdata[row][5]=resultSet.getString("citime");
                rowdata[row][6]=resultSet.getString("remark");
                row++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        DefaultTableModel model=new DefaultTableModel(rowdata,tableTitle){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table=new JTable(model);
        table.getTableHeader().setReorderingAllowed(false); //设置列不可移动
        DefaultTableCellRenderer cellRenderer=new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class,cellRenderer);
        //滚动列表
        JScrollPane jscrollpane = new JScrollPane(table);
        jscrollpane.setPreferredSize(new Dimension(0,100));
        JPanel panel1=new JPanel();
        panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
        panel1.add(jscrollpane);
        JPanel panel1_1=new JPanel();
        panel1_1.setMaximumSize(new Dimension(1000,50));
        panel1.add(panel1_1);
        panel_main.add(panel1);
        return panel_main;
    }
}
