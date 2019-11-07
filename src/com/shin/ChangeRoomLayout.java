package com.shin;

import javax.swing.*;
import java.awt.*;

public class ChangeRoomLayout {

    public Component getMainPanel(){
        JPanel panel_main=new JPanel();
        JPanel panel1=new JPanel(new GridLayout(0,2,0,0));
        JLabel label1=new JLabel("身份证号",JLabel.CENTER);
        JPanel textField1=panelTextFiled();
        JLabel label2=new JLabel("原房间",JLabel.CENTER);
        JPanel textField2=panelTextFiled();
        JLabel label3=new JLabel("新房间",JLabel.CENTER);
        JComboBox comboBox=new JComboBox();
        JPanel panel1_1=new JPanel();
        panel1_1.add(comboBox);
        comboBox.setPreferredSize(new Dimension(130,25));
        JLabel empty=new JLabel("");
        JPanel panel1_2=new JPanel();
        JButton commit=new JButton("提交");
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
    private JPanel panelTextFiled(){
        JPanel panel=new JPanel();
        JTextField textField=new JTextField(20);
        panel.add(textField);
        return panel;
    }
}
