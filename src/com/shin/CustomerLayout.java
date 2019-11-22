package com.shin;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;

public class CustomerLayout {
    private Database database=new Database();
    public Component getMainPanel(){
        JPanel panel_main=new JPanel(new BorderLayout());
        String[] tableTitle={"身份证号","姓名","性别","电话号码","房间号","入住时间","队伍编号","备注"};
        String[][] rowdata=new String[1][];
        try {
            ResultSet rowCount1=database.QueryInfo("select count(distinct idnum) from check_in");
            ResultSet rowCount2=database.QueryInfo("select count(*) from follow where gid in (select gid from _group where cap_idnum in (select idnum from check_in))");
            ResultSet resultSet=database.QueryInfo("select * from customer where customer.idnum not in (select idnum from book) and idnum not in (select men_idnum from follow where gid in(select gid from _group where cap_idnum not in (select idnum from check_in)))");
            int row=0;
            if(rowCount1.next()){
                String data[][]=new String[rowCount1.getInt("count(distinct idnum)")][];
                rowdata=data;
                if(rowCount2.next()){
                    String data2[][]=new String[rowCount1.getInt("count(distinct idnum)")+rowCount2.getInt("count(*)")][];
                    rowdata=data2;
                }
            }
            while(resultSet.next()){
                String rid="";
                String citime="";
                String groupId="无";
                ResultSet ridAndCitime;
                if(resultSet.getString("remark").equals("团员")){
                    ResultSet rgid=database.QueryInfo("select gid from follow where men_idnum="+resultSet.getString("idnum"));
                    if(rgid.next()){
                        groupId=rgid.getString("gid");
                    }
                    ridAndCitime=database.QueryInfo("select rid,citime from check_in where idnum=(select cap_idnum from _group where gid=(select gid from follow where men_idnum="+resultSet.getString("idnum")+"))");
                }else{
                    ResultSet rgid=database.QueryInfo("select gid from _group where cap_idnum="+resultSet.getString("idnum"));
                    if(rgid.next()){
                        groupId=rgid.getString("gid");
                    }
                    ridAndCitime=database.QueryInfo("select rid,citime from check_in where idnum="+resultSet.getString("idnum"));
                }
                while(ridAndCitime.next()){
                    rid+=ridAndCitime.getString("rid");
                    if(!ridAndCitime.isLast()){
                        rid+=",";
                    }else{
                        citime=ridAndCitime.getString("citime");
                    }
                }
                rowdata[row]=new String[8];
                rowdata[row][0]=resultSet.getString("idnum");
                rowdata[row][1]=resultSet.getString("cname");
                rowdata[row][2]=resultSet.getString("sex");
                rowdata[row][3]=resultSet.getString("telphone");
                rowdata[row][4]=rid;
                rowdata[row][5]=citime;
                rowdata[row][6]=groupId;
                rowdata[row][7]=resultSet.getString("remark");
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
        TableRowSorter<TableModel> tableRowSorter=new TableRowSorter<TableModel>(model);
        table.setRowSorter(tableRowSorter);
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
