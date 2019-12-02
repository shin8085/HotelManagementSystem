package com.shin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckoutLayout {

    Database database=new Database();
    JTextField field1=new JTextField();
    JTextField field2=new JTextField();
    JTextField field3=new JTextField();
    JRadioButton rb1;
    JRadioButton rb2;
    JButton commit;
    public Component getMainPanel(){
        JPanel panel_main=new JPanel();
        JPanel panel1=new JPanel(new GridLayout(0,2,0,0));
        JLabel label1=new JLabel("身份证号",JLabel.CENTER);
        JPanel textField1=panelTextFiled(field1);
        JLabel label2=new JLabel("房间号",JLabel.CENTER);
        JPanel textField2=panelTextFiled(field2);
        field2.setEditable(false);
        JLabel label3=new JLabel("费用",JLabel.CENTER);
        JPanel textField3=panelTextFiled(field3);
        field3.setEditable(false);
        field1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar()==KeyEvent.VK_ENTER){
                    ResultSet rids=database.QueryInfo("select rid from check_in where idnum="+field1.getText());
                    ResultSet deposit=database.QueryInfo("select dprice from deposit where idnum="+field1.getText());
                    try{
                        if(!rids.next()){
                            field2.setText("不存在");
                            field3.setText("不存在");
                            return;
                        }else {{
                            rids.previous();
                        }}
                        String rid="";
                        while(rids.next()){
                            rid+=rids.getString("rid");
                            if(!rids.isLast()){
                                rid+=",";
                            }
                        }

                        field2.setText(rid);
                        if(deposit.next()){
                            int price=0;
                            price=Integer.parseInt(deposit.getString("dprice"));
                            field3.setText(String.valueOf(price*2));
                        }
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
            }
        });
        ButtonGroup group=new ButtonGroup();
        rb1=new JRadioButton("现结");
        rb2=new JRadioButton("挂账");
        group.add(rb1);
        group.add(rb1);
        JPanel panel1_1=new JPanel();
        panel1_1.add(rb1);
        panel1_1.add(rb2);
        JPanel panel1_2=new JPanel();
        JLabel empty1=new JLabel("");
        JLabel empty2=new JLabel("");
        commit=new JButton("提交");
        panel1_2.add(commit);
        commit();
        new Tools().addComponent(panel1,new JComponent[]{label1,textField1,label2,textField2,label3,textField3,empty1,panel1_1,empty2,panel1_2});
        panel1.setBorder(BorderFactory.createEmptyBorder(100,0,0,0));
        panel1.setPreferredSize(new Dimension(450,260));
        panel_main.add(panel1);
        return panel_main;
    }
    public void commit(){
        commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String idnum=field1.getText();
                String rid=null;
                ResultSet rids=database.QueryInfo("select rid from check_in where idnum="+field1.getText());
                String price=field3.getText();
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date=dateFormat.format(new Date());
                try{
                    database.startTransaction();
                    //删除队员信息
                    database.UpdataInfo("delete from customer where idnum in (select men_idnum from follow where gid=(select gid from _group where cap_idnum="+idnum+"))");
                    //删除领队信息
                    database.UpdataInfo("delete from customer where idnum="+idnum);
                    while(rids.next()){
                        rid=rids.getString("rid");
                        database.Insert("check_out",new String[]{idnum,rid,date});
                        if(rb1.isSelected()){
                            database.Insert("check_out_normal",new String[]{idnum,rid,price});
                        }else if(rb2.isSelected()){
                            database.Insert("check_out_credit",new String[]{idnum,rid,price});
                        }
                    }
                    database.commitTransaction();
                }catch (Exception e){
                    database.rollbackTransaction();
                    e.printStackTrace();
                }
                javax.swing.JOptionPane.showMessageDialog(null,"提交成功!");
                new Tools().resetTabLayout(MainLayout.tabbedPane,new CheckInOrBookLayout().getMainPanel(),0);
                new Tools().resetTabLayout(MainLayout.tabbedPane,new RoomInfoLayout().getMainPanel(),5);
                new Tools().resetTabLayout(MainLayout.tabbedPane,new CustomerLayout().getMainPanel(),6);
                new Tools().resetTabLayout(MainLayout.tabbedPane,new CheckoutLayout().getMainPanel(),2);
            }
        });
    }

    private JPanel panelTextFiled(JTextField textField){
        JPanel panel=new JPanel();
        textField.setColumns(20);
        panel.add(textField);
        return panel;
    }
}
