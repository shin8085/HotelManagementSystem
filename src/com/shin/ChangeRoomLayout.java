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

public class ChangeRoomLayout {
    private Database database=new Database();
    private JTextField field1=new JTextField();
    private JTextField field2=new JTextField();
    public Component getMainPanel(){
        JPanel panel_main=new JPanel();
        JPanel panel1=new JPanel(new GridLayout(0,2,0,0));
        JLabel label1=new JLabel("身份证号",JLabel.CENTER);
        JPanel textField1=panelTextFiled(field1);
        JLabel label2=new JLabel("原房间",JLabel.CENTER);
        JPanel textField2=panelTextFiled(field2);
        field2.setEditable(false);
        JLabel label3=new JLabel("新房间",JLabel.CENTER);
        JComboBox comboBox=new JComboBox();
        field1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar()==KeyEvent.VK_ENTER){
                    try{
                        ResultSet resultSet=database.QueryInfo("select rid from check_in where idnum='"+field1.getText()+"'");
                        if(resultSet.next()){
                            String rid=resultSet.getString("rid");
                            field2.setText(rid);
                            ResultSet resultSet1=database.QueryInfo("select rid from room where rid not in (select rid from check_in) and rtype=(select rtype from room where rid='"+rid+"') and rid not in (select rid from book)");
                            comboBox.removeAllItems();
                            while(resultSet1.next()){
                                comboBox.addItem(resultSet1.getString("rid"));
                            }
                        }
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
            }
        });
        JPanel panel1_1=new JPanel();
        panel1_1.add(comboBox);
        comboBox.setPreferredSize(new Dimension(130,25));
        JLabel empty=new JLabel("");
        JPanel panel1_2=new JPanel();
        JButton commit=new JButton("提交");
        commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                comboBox.getSelectedIndex();
                database.UpdataInfo("update check_in set rid="+(String)comboBox.getSelectedItem()+" where idnum="+field1.getText());
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date=dateFormat.format(new Date());
                database.Insert("room_changes",new String[]{field1.getText(),field2.getText(),(String)comboBox.getSelectedItem(),date});
                javax.swing.JOptionPane.showMessageDialog(null,"提交成功!");
                new Tools().resetTabLayout(MainLayout.tabbedPane,new RoomInfoLayout().getMainPanel(),5);
                new Tools().resetTabLayout(MainLayout.tabbedPane,new CustomerLayout().getMainPanel(),6);
                new Tools().resetTabLayout(MainLayout.tabbedPane,new ChangeRoomLayout().getMainPanel(),2);
            }
        });
        panel1_2.add(commit);
        addComponent(panel1,new JComponent[]{label1,textField1,label2,textField2,label3,panel1_1,empty,panel1_2});
        panel1.setBorder(BorderFactory.createEmptyBorder(100,0,0,0));
        panel1.setPreferredSize(new Dimension(450,230));
        panel_main.add(panel1);
        return panel_main;
    }
    private void addComponent(JPanel panel,JComponent [] components){
        for(int i=0;i<components.length;i++){
            panel.add(components[i]);
        }
    }
    private JPanel panelTextFiled(JTextField textField){
        JPanel panel=new JPanel();
        textField.setColumns(20);
        panel.add(textField);
        return panel;
    }
}
