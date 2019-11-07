package com.shin;

import javax.swing.*;
import java.awt.*;

public class CheckoutLayout {

    public Component getMainPanel(){
        JPanel panel_main=new JPanel();
        JPanel panel1=new JPanel(new GridLayout(0,2,0,0));
        JLabel label1=new JLabel("身份证号",JLabel.CENTER);
        JPanel textField1=panelTextFiled();
        JLabel label2=new JLabel("房间号",JLabel.CENTER);
        JPanel textField2=panelTextFiled();
        JLabel label3=new JLabel("费用",JLabel.CENTER);
        JPanel textField3=panelTextFiled();
        JRadioButton rb1=new JRadioButton("现结");
        JRadioButton rb2=new JRadioButton("挂账");
        ButtonGroup group=new ButtonGroup();
        group.add(rb1);
        group.add(rb1);
        JPanel panel1_1=new JPanel();
        panel1_1.add(rb1);
        panel1_1.add(rb2);
        JPanel panel1_2=new JPanel();
        JLabel empty1=new JLabel("");
        JLabel empty2=new JLabel("");
        JButton commit=new JButton("提交");
        panel1_2.add(commit);
        addComponent(panel1,new JComponent[]{label1,textField1,label2,textField2,label3,textField3,empty1,panel1_1,empty2,panel1_2});
        panel1.setBorder(BorderFactory.createEmptyBorder(100,0,0,0));
        panel1.setPreferredSize(new Dimension(450,260));
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
