package com.shin;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookInfoLayout {

    Database database=new Database();

    public Component getMainPanel(){
        JPanel panel_main=new JPanel(new BorderLayout());
        String[] tableTitle={"身份证号","姓名","性别","电话号码","房间","预定时间","备注","入住","删除"};
        String[][] rowdata=new String[1][];
        try {
            ResultSet rowCount=database.QueryInfo("select count(distinct idnum) as count from book where idnum not in (select idnum from customer where remark='跟随入住')");
            ResultSet resultSet=database.QueryInfo("select * from customer where idnum in (select idnum from book) and remark<>'跟随入住'");
            int row=0;
            if(rowCount.next()){
                String data[][]=new String[rowCount.getInt("count")][];
                rowdata=data;
            }
            while(resultSet.next()){
                ResultSet bookinfo=database.QueryInfo("select rid,btime from book where idnum="+resultSet.getString("idnum"));
                rowdata[row]=new String[9];
                rowdata[row][0]=resultSet.getString("idnum");
                rowdata[row][1]=resultSet.getString("cname");
                rowdata[row][2]=resultSet.getString("sex");
                rowdata[row][3]=resultSet.getString("telphone");
                String rid="";
                while(bookinfo.next()){
                    rid+=bookinfo.getString("rid");
                    if(!bookinfo.isLast()){
                        rid+=",";
                    }else{
                        rowdata[row][5]=bookinfo.getString("btime");
                    }

                }
                rowdata[row][4]=rid;
                rowdata[row][6]=resultSet.getString("remark");
                rowdata[row][7]="√";
                rowdata[row][8]="X";
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
        table.getColumnModel().getColumn(7).setMaxWidth(50);
        table.getColumnModel().getColumn(8).setMaxWidth(50);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String idnum=(String)table.getValueAt(table.getSelectedRow(),0);
                String rid=(String)table.getValueAt(table.getSelectedRow(),4);
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date=dateFormat.format(new Date());
                if(table.getSelectedColumn()==7){
                    //入住
                    model.removeRow(table.getSelectedRow());
                    database.UpdataInfo("delete from book where idnum="+idnum);
                    String [] rids=rid.split(",");
                    for (String r : rids) {
                        database.Insert("check_in", new String[]{idnum, r, date});
                    }
                    new Tools().resetTabLayout(MainLayout.tabbedPane,new CustomerLayout().getMainPanel(),6);
                }
                else if(table.getSelectedColumn()==8){
                    //取消预约
                    //散客 customer book deposit
                    //团队 customer _group follow book deposit
                    if(table.getValueAt(table.getSelectedRow(),6).equals("领队")){
                        database.UpdataInfo("delete from customer where idnum in (select men_idnum from follow where gid=(select gid from _group where cap_idnum="+idnum+"))");
                    }else{
                        database.UpdataInfo("delete from customer where idnum in (select idnum from book where rid=(select rid from book where idnum="+idnum+"))");
                    }
                    database.UpdataInfo("delete from customer where idnum="+idnum);
                    model.removeRow(table.getSelectedRow());
                }
                new Tools().resetTabLayout(MainLayout.tabbedPane,new RoomInfoLayout().getMainPanel(),5);
                MainLayout.tabbedPane.setSelectedIndex(4);
            }
        });

        DefaultTableCellRenderer cellRenderer=new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Color background;
                Component renderer=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if(column==8){
                    background=Color.red;
                }
                else if(column==7){
                    background=Color.green;
                }
                else{
                    background=Color.white;
                }
                renderer.setBackground(background);
                return renderer;
            }

            @Override
            public void setHorizontalAlignment(int alignment) {
                super.setHorizontalAlignment(JLabel.CENTER);
            }
        };
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
