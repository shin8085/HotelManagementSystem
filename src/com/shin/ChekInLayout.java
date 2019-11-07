package com.shin;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;


//客户界面
public class ChekInLayout {

    private JTabbedPane tabbedPane;

    public ChekInLayout(){
        tabbedPane=new JTabbedPane();
    }

    public Component getMainPanel(){
        tabbedPane.addTab("散客",getPerson());
        tabbedPane.addTab("团体",getGroup());
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        return tabbedPane;
    }

    private Component getPerson(){
        JPanel panel_main=new JPanel();
        panel_main.setLayout(new BoxLayout(panel_main,BoxLayout.Y_AXIS));

        //客户信息
        String[] tableTitle={"身份","身份证号","姓名","性别","电话号码","删除"};
        String[][] rowdata={{"开房者","","","","","———"}};
        DefaultTableModel model=new DefaultTableModel(rowdata,tableTitle){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column==0||column==5){
                    return false;
                }
                else{
                    return true;
                }
            }
        };
        JTable table=new JTable(model);
        table.getTableHeader().setReorderingAllowed(false); //设置列不可移动
        table.getColumnModel().getColumn(5).setMaxWidth(50);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(table.getSelectedColumn()==5&&table.getSelectedRow()!=0){
                    model.removeRow(table.getSelectedRow());
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });


        DefaultTableCellRenderer cellRenderer=new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Color background;
                Component renderer=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if(column==5&&row!=0){
                    background=Color.red;
                }
                else if(column==0){
                    background=Color.cyan;
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
        //cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class,cellRenderer);
        //滚动列表
        JScrollPane jscrollpane = new JScrollPane(table);
        jscrollpane.setPreferredSize(new Dimension(0,100));
        JButton button_add=new JButton("增加");
        button_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                model.insertRow(1,new Object[]{"跟随入住", "","","","","X"});
            }
        });
        JButton button_reset=new JButton("重置");
        button_reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                model.setRowCount(0);
                model.insertRow(0,new Object[]{"开房者", "","","","","———"});
            }
        });
        JPanel panel1=new JPanel();
        panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
        panel1.add(jscrollpane);
        JPanel panel1_1=new JPanel();
        panel1_1.setMaximumSize(new Dimension(1000,50));
        panel1_1.add(button_add);
        panel1_1.add(button_reset);
        panel1.add(panel1_1);
        Border etched=BorderFactory.createEtchedBorder();
        Border border=BorderFactory.createTitledBorder(etched,"顾客信息");
        panel1.setBorder(border);

        //客房选择
        JPanel panel2=new JPanel();
        panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
        JPanel panel2_1=new JPanel();
        JPanel panel2_2=new JPanel(new GridLayout(0,2,0,0));
        panel2_2.setBorder(BorderFactory.createEmptyBorder(0,300,0,300));
        JRadioButton rb1=new JRadioButton("单人间");
        JRadioButton rb2=new JRadioButton("双人房");
        JRadioButton rb3=new JRadioButton("豪华套间");
        panel2_1.add(rb1);
        panel2_1.add(rb2);
        panel2_1.add(rb3);
        JLabel price_k=new JLabel("价格",JLabel.CENTER);
        JLabel price_v=new JLabel("",JLabel.CENTER);
        JLabel room_num=new JLabel("房间号",JLabel.CENTER);
        JPanel panel2_2_1=new JPanel();
        JComboBox comboBox=new JComboBox();
        comboBox.setPreferredSize(new Dimension(200,30));
        panel2_2_1.add(comboBox);
        panel2_2.add(price_k);
        panel2_2.add(price_v);
        panel2_2.add(room_num);
        panel2_2.add(panel2_2_1);
        border=BorderFactory.createTitledBorder(etched,"客房信息");
        panel2.setBorder(border);
        panel2.setPreferredSize(new Dimension(0,50));
        panel2.add(panel2_1);
        panel2.add(panel2_2);
        ButtonGroup group=new ButtonGroup();
        group.add(rb1);
        group.add(rb2);
        group.add(rb3);

        //VIP
        JPanel panel3=new JPanel();
        JPanel panel3_1=new JPanel(new GridLayout(0,2,0,0));
        JLabel LvipCard=new JLabel("卡号",JLabel.CENTER);
        JTextField TvipCard=new JTextField(20);
        JLabel discount_k=new JLabel("折扣",JLabel.CENTER);
        JLabel discount_v=new JLabel("9.5折",JLabel.CENTER);
        JLabel idcard_k=new JLabel("身份证号",JLabel.CENTER);
        JLabel idcard_v=new JLabel("",JLabel.CENTER);
        JLabel name_k=new JLabel("姓名",JLabel.CENTER);
        JLabel name_v=new JLabel("",JLabel.CENTER);
        addComponent(panel3_1,new JComponent[]{LvipCard,TvipCard,discount_k,discount_v,idcard_k,idcard_v,name_k,name_v});
        panel3.add(panel3_1,BorderLayout.CENTER);
        border=BorderFactory.createTitledBorder(etched,"贵宾卡");
        panel3.setBorder(border);

        JButton commit=new JButton("提交");

        panel_main.add(panel1);
        panel_main.add(panel2);
        panel_main.add(panel3);
        panel_main.add(commit);
        return panel_main;
    }

    private Component getGroup(){
        JPanel panel_main=new JPanel();
        panel_main.setLayout(new BoxLayout(panel_main,BoxLayout.Y_AXIS));

        //客户信息
        String[] tableTitle={"身份","身份证号","姓名","性别","电话号码","删除"};
        String[][] rowdata={{"领队","","","","","———"}};
        DefaultTableModel model=new DefaultTableModel(rowdata,tableTitle){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column==0||column==5){
                    return false;
                }
                else{
                    return true;
                }
            }
        };
        JTable table=new JTable(model);
        table.getTableHeader().setReorderingAllowed(false); //设置列不可移动
        table.getColumnModel().getColumn(5).setMaxWidth(50);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(table.getSelectedColumn()==5&&table.getSelectedRow()!=0){
                    model.removeRow(table.getSelectedRow());
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });


        DefaultTableCellRenderer cellRenderer=new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Color background;
                Component renderer=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if(column==5&&row!=0){
                    background=Color.red;
                }
                else if(column==0){
                    background=Color.cyan;
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
        //cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class,cellRenderer);
        //滚动列表
        JScrollPane jscrollpane = new JScrollPane(table);
        jscrollpane.setPreferredSize(new Dimension(0,100));
        JButton button_add=new JButton("增加");
        button_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                model.insertRow(1,new Object[]{"团员", "","","","","X"});
            }
        });
        JButton button_reset=new JButton("重置");
        button_reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                model.setRowCount(0);
                model.insertRow(0,new Object[]{"领队", "","","","","———"});
            }
        });
        JPanel panel1=new JPanel();
        panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
        panel1.add(jscrollpane);
        JPanel panel1_1=new JPanel();
        panel1_1.setMaximumSize(new Dimension(1000,50));
        panel1_1.add(button_add);
        panel1_1.add(button_reset);
        panel1.add(panel1_1);
        Border etched=BorderFactory.createEtchedBorder();
        Border border=BorderFactory.createTitledBorder(etched,"顾客信息");
        panel1.setBorder(border);

        //客房选择
        JPanel panel2=new JPanel();
        panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
        JPanel panel2_1=new JPanel(new GridLayout(0,3,0,0));
        JLabel head1=new JLabel("类型",JLabel.CENTER);
        JLabel head2=new JLabel("数量",JLabel.CENTER);
        JLabel head3=new JLabel("房号",JLabel.CENTER);
        JLabel label1=new JLabel("单人间（80/天）",JLabel.CENTER);
        JPanel panel2_1_1=new JPanel();
        JPanel panel2_1_2=new JPanel();
        JTextField num1=new JTextField(10);
        JTextField set1=new JTextField(30);
        panel2_1_1.add(num1);
        panel2_1_2.add(set1);
        JLabel label2=new JLabel("双人间（120/天）",JLabel.CENTER);
        JPanel panel2_1_3=new JPanel();
        JPanel panel2_1_4=new JPanel();
        JTextField num2=new JTextField(10);
        JTextField set2=new JTextField(30);
        panel2_1_3.add(num2);
        panel2_1_4.add(set2);
        JLabel label3=new JLabel("豪华套间（200/天）",JLabel.CENTER);
        JPanel panel2_1_5=new JPanel();
        JPanel panel2_1_6=new JPanel();
        JTextField num3=new JTextField(10);
        JTextField set3=new JTextField(30);
        panel2_1_5.add(num3);
        panel2_1_6.add(set3);
        panel2_1.add(head1);
        panel2_1.add(head2);
        panel2_1.add(head3);
        panel2_1.add(label1);
        panel2_1.add(panel2_1_1);
        panel2_1.add(panel2_1_2);
        panel2_1.add(label2);
        panel2_1.add(panel2_1_3);
        panel2_1.add(panel2_1_4);
        panel2_1.add(label3);
        panel2_1.add(panel2_1_5);
        panel2_1.add(panel2_1_6);

        panel2.add(panel2_1);
        border=BorderFactory.createTitledBorder(etched,"客房信息");
        panel2.setBorder(border);
        panel2.setPreferredSize(new Dimension(0,50));


        JButton commit=new JButton("提交");

        panel_main.add(panel1);
        panel_main.add(panel2);
        panel_main.add(commit);
        return panel_main;
    }

    private void addComponent(JPanel panel,JComponent [] components){
        for(int i=0;i<components.length;i++){
            panel.add(components[i]);
        }
    }

}
