package com.shin;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


//客户界面
public class ChekInLayout {

    private JTabbedPane tabbedPane;
    private Database database;

    public ChekInLayout(){
        tabbedPane=new JTabbedPane();
        try{
            database=new Database();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Component getMainPanel(){
        Person person=new Person();
        tabbedPane.addTab("散客",person.getPanelMain());
        tabbedPane.addTab("团体",getGroup());
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        return tabbedPane;
    }

    class Person{
        private JPanel panel_main;
        private JPanel panel1;
        private JPanel panel2;
        private JPanel panel3;
        private Border etched=BorderFactory.createEtchedBorder();
        private Border border;
        private JTable table;
        private JButton button_reset;
        private JRadioButton rb1; //单人间
        private JRadioButton rb2; //双人间
        private JRadioButton rb3; //豪华套间
        private JComboBox comboBox; //房间号下拉框
        private JLabel idcard_k;
        private JLabel idcard_v;
        private JTextField TvipCard; //卡号输入框
        private JButton commit; //页面提交按钮
        Person(){
            panel_main=new JPanel();
            panel_main.setLayout(new BoxLayout(panel_main,BoxLayout.Y_AXIS));
            initInfoTable();
            initRoomChoice();
            initVipInfo();
            commit();
            panel_main.add(panel1);
            panel_main.add(panel2);
            panel_main.add(panel3);
            panel_main.add(commit);
        }
        public Component getPanelMain(){
            return panel_main;
        }
        private void initInfoTable(){
            //客户信息
            panel1=new JPanel();
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

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    if(column==1&&aValue.toString().length()!=18){
                        javax.swing.JOptionPane.showMessageDialog(null,"身份证号非法!");
                        return;
                    }
                    if(column==2&&aValue.toString().length()>10){
                        javax.swing.JOptionPane.showMessageDialog(null,"姓名非法!");
                        return;
                    }
                    if(column==3&&!aValue.toString().equals("男")&&!aValue.toString().equals("女")){
                        javax.swing.JOptionPane.showMessageDialog(null,"性别非法!");
                        return;
                    }
                    if(column==4&&aValue.toString().length()>12){
                        javax.swing.JOptionPane.showMessageDialog(null,"电话号码非法!");
                    }
                    super.setValueAt(aValue,row,column);
                }
            };
            table=new JTable(model);
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
            button_reset=new JButton("重置");
            button_reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    model.setRowCount(0);
                    model.insertRow(0,new Object[]{"开房者", "","","","","———"});
                }
            });
            panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
            panel1.add(jscrollpane);
            JPanel panel1_1=new JPanel();
            panel1_1.setMaximumSize(new Dimension(1000,50));
            panel1_1.add(button_add);
            panel1_1.add(button_reset);
            panel1.add(panel1_1);
            border=BorderFactory.createTitledBorder(etched,"顾客信息");
            panel1.setBorder(border);
        }
        private void initRoomChoice(){
            //客房选择
            panel2=new JPanel();
            panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
            JPanel panel2_1=new JPanel();
            JPanel panel2_2=new JPanel(new GridLayout(0,2,0,0));
            panel2_2.setBorder(BorderFactory.createEmptyBorder(0,300,0,300));
            rb1=new JRadioButton("单人间");
            rb2=new JRadioButton("双人房");
            rb3=new JRadioButton("豪华套间");
            panel2_1.add(rb1);
            panel2_1.add(rb2);
            panel2_1.add(rb3);
            JLabel price_k=new JLabel("价格",JLabel.CENTER);
            JLabel price_v=new JLabel("",JLabel.CENTER);
            JLabel room_num=new JLabel("房间号",JLabel.CENTER);
            JPanel panel2_2_1=new JPanel();
            comboBox=new JComboBox();
            comboBox.setPreferredSize(new Dimension(200,30));
            try{
                ResultSet resultSet=database.QueryInfo("select rid from room where rtype='单人间' and rid not in (select rid from check_in)");
                while(resultSet.next()){
                    comboBox.addItem(resultSet.getString("rid"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
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
            rb1.setSelected(true);
            price_v.setText("100");
            ActionListener listener1=new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JRadioButton rb=(JRadioButton)actionEvent.getSource();
                    String type="";
                    if(rb==rb1){
                        price_v.setText("100");
                        type ="单人间";
                    }
                    else if(rb==rb2){
                        price_v.setText("140");
                        type="双人间";
                    }
                    else if(rb==rb3){
                        price_v.setText("220");
                        type="豪华套间";
                    }
                    Vector<String> results=new Vector<>();
                    try{
                        ResultSet resultSet=database.QueryInfo("select rid from room where rtype='"+type+"' and rid not in (select rid from check_in)");
                        comboBox.removeAllItems();
                        while(resultSet.next()){
                            comboBox.addItem(resultSet.getString("rid"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            rb1.addActionListener(listener1);
            rb2.addActionListener(listener1);
            rb3.addActionListener(listener1);
        }
        private void initVipInfo(){
            //VIP
            panel3=new JPanel();
            JPanel panel3_1=new JPanel(new GridLayout(0,2,0,0));
            JLabel LvipCard=new JLabel("卡号",JLabel.CENTER);
            TvipCard=new JTextField(20);
            JLabel discount_k=new JLabel("折扣",JLabel.CENTER);
            JLabel discount_v=new JLabel("9.5折",JLabel.CENTER);
            idcard_k=new JLabel("身份证号",JLabel.CENTER);
            idcard_v=new JLabel("",JLabel.CENTER);
            JLabel name_k=new JLabel("姓名",JLabel.CENTER);
            JLabel name_v=new JLabel("",JLabel.CENTER);
            addComponent(panel3_1,new JComponent[]{LvipCard,TvipCard,discount_k,discount_v,idcard_k,idcard_v,name_k,name_v});
            panel3.add(panel3_1,BorderLayout.CENTER);
            border=BorderFactory.createTitledBorder(etched,"贵宾卡");
            panel3.setBorder(border);
            TvipCard.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyChar()==KeyEvent.VK_ENTER){
                        String vid=TvipCard.getText();
                        try{
                            ResultSet resultSet=database.QueryInfo("select * from vip_card where vid='"+vid+"'");
                            if(resultSet.next()){
                                if(resultSet.getString("idnum")==table.getValueAt(0,1)){
                                    idcard_v.setText(resultSet.getString("idnum"));
                                    name_v.setText(resultSet.getString("cname"));
                                }
                                else
                                    TvipCard.setText("该会员卡非开房者所有");
                            }
                            else{
                                TvipCard.setText("会员卡不存在");
                            }
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                }
            });
        }
        private void commit(){
            commit=new JButton("提交");
            commit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    String roomType,price,roomNum;
                    if(rb1.isSelected()){
                        roomType="单人房";
                        price="100";
                    }else if(rb2.isSelected()){
                        roomType="双人房";
                        price="140";
                    }else if(rb3.isSelected()){
                        roomType="豪华套间";
                        price="220";
                    }
                    roomNum=(String)comboBox.getSelectedItem();
                    int rowCount=table.getRowCount();
                    int collumnCount=table.getColumnCount();
                    String remark="无";
                    if(idcard_k.getText()==table.getValueAt(0,1)) remark="vip";
                    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Boolean flag=true;
                    String [][] data=new String[rowCount][];
                    for(int i=0;i<rowCount;i++){
                        data[i]=new String[4];
                    }
                    for(int i=0;i<rowCount;i++){
                        String idnum=(String)table.getValueAt(i,1);
                        try{
                            ResultSet resultSet=database.QueryInfo("select count(*) from customer where idnum='"+idnum+"'");
                            if(resultSet.next()){
                                int n=resultSet.getInt("count(*)");
                                System.out.println(n);
                                if(n>0){
                                    javax.swing.JOptionPane.showMessageDialog(null,"身份证号已存在!");
                                    return;
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        String name=(String)table.getValueAt(i,2);
                        String sex=(String)table.getValueAt(i,3);
                        String telphone=(String)table.getValueAt(i,4);
                        if(idnum.length()==0||name.length()==0||sex.length()==0||telphone.length()==0){
                            javax.swing.JOptionPane.showMessageDialog(null,"客户信息输入有误!");
                            return;
                        }else{
                            data[i][0]=idnum;
                            data[i][1]=name;
                            data[i][2]=sex;
                            data[i][3]=telphone;
                        }
                    }
                    String date=dateFormat.format(new Date());
                    for(int i=0;i<rowCount;i++){
                        if(i==0){
                            database.Insert("customer",new String[]{data[i][0],data[i][1],data[i][2],data[i][3],remark});
                        }else{
                            database.Insert("customer",new String[]{data[i][0],data[i][1],data[i][2],data[i][3],"无"});
                        }
                        database.Insert("check_in",new String[]{data[i][0],roomNum,date});
                    }
                    javax.swing.JOptionPane.showMessageDialog(null,"提交成功!");
                    reset();
                }
            });
        }
        private void reset(){
            panel_main.removeAll();
            panel_main.repaint();
            initInfoTable();
            initRoomChoice();
            initInfoTable();
            TvipCard.setText("");
            panel_main.add(panel1);
            panel_main.add(panel2);
            panel_main.add(panel3);
            panel_main.add(commit);
            panel_main.revalidate();
        }
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

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if(column==1&&aValue.toString().length()!=18){
                    javax.swing.JOptionPane.showMessageDialog(null,"身份证号非法!");
                    return;
                }
                if(column==2&&aValue.toString().length()>10){
                    javax.swing.JOptionPane.showMessageDialog(null,"姓名非法!");
                    return;
                }
                if(column==3&&!aValue.toString().equals("男")&&!aValue.toString().equals("女")){
                    javax.swing.JOptionPane.showMessageDialog(null,"性别非法!");
                    return;
                }
                if(column==4&&aValue.toString().length()>12){
                    javax.swing.JOptionPane.showMessageDialog(null,"电话号码非法!");
                }
                super.setValueAt(aValue,row,column);
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
        try{
            ResultSet resultSet=database.QueryInfo("select rid from room where rtype='单人间' and rid not in (select rid from check_in)");
            while(resultSet.next()){
                comboBox.addItem(resultSet.getString("rid"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
        rb1.setSelected(true);
        price_v.setText("100");
        ActionListener listener1=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JRadioButton rb=(JRadioButton)actionEvent.getSource();
                String type="";
                if(rb==rb1){
                    price_v.setText("100");
                    type ="单人间";
                }
                else if(rb==rb2){
                    price_v.setText("140");
                    type="双人间";
                }
                else if(rb==rb3){
                    price_v.setText("220");
                    type="豪华套间";
                }
                Vector<String> results=new Vector<>();
                try{
                    ResultSet resultSet=database.QueryInfo("select rid from room where rtype='"+type+"' and rid not in (select rid from check_in)");
                    comboBox.removeAllItems();
                    while(resultSet.next()){
                        comboBox.addItem(resultSet.getString("rid"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        rb1.addActionListener(listener1);
        rb2.addActionListener(listener1);
        rb3.addActionListener(listener1);

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
        TvipCard.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar()==KeyEvent.VK_ENTER){
                    String vid=TvipCard.getText();
                    try{
                        ResultSet resultSet=database.QueryInfo("select * from vip_card where vid='"+vid+"'");
                        if(resultSet.next()){
                            if(resultSet.getString("idnum")==table.getValueAt(0,1)){
                                idcard_v.setText(resultSet.getString("idnum"));
                                name_v.setText(resultSet.getString("cname"));
                            }
                            else
                                TvipCard.setText("该会员卡非开房者所有");
                        }
                        else{
                            TvipCard.setText("会员卡不存在");
                        }
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
            }
        });

        JButton commit=new JButton("提交");
        commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String roomType,price,roomNum;
                if(rb1.isSelected()){
                    roomType="单人房";
                    price="100";
                }else if(rb2.isSelected()){
                    roomType="双人房";
                    price="140";
                }else if(rb3.isSelected()){
                    roomType="豪华套间";
                    price="220";
                }
                roomNum=(String)comboBox.getSelectedItem();
                int rowCount=table.getRowCount();
                int collumnCount=table.getColumnCount();
                String remark="无";
                if(idcard_k.getText()==table.getValueAt(0,1)) remark="vip";
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Boolean flag=true;
                for(int i=0;i<rowCount;i++){
                    String idnum=(String)table.getValueAt(i,1);
                    String name=(String)table.getValueAt(i,2);
                    String sex=(String)table.getValueAt(i,3);
                    String telphone=(String)table.getValueAt(i,4);
                    String date=dateFormat.format(new Date());
                    database.Insert("customer",new String[]{idnum,name,sex,telphone,remark});
                    database.Insert("check_in",new String[]{idnum,roomNum,date});
                }
                javax.swing.JOptionPane.showMessageDialog(null,"提交成功!");
                button_reset.doClick();
                rb1.setSelected(true);
            }
        });

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
