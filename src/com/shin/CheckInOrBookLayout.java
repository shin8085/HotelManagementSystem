package com.shin;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;


//客户界面
public class CheckInOrBookLayout {

    private JTabbedPane tabbedPane;
    private Database database;

    public CheckInOrBookLayout(){
        tabbedPane=new JTabbedPane();
        try{
            database=new Database();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Component getMainPanel(){
        Person person=new Person();
        Group group=new Group();
        tabbedPane.addTab("散客",person.getMainPanel());
        tabbedPane.addTab("团体",group.getMainPanel());
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        return tabbedPane;
    }

    class Person{
        private JPanel panel_main;
        private JPanel panel1;
        private JPanel panel2;
        private JPanel panel3;
        private JPanel panel4;
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
        private JLabel idcard_y;
        private JTextField TvipCard; //卡号输入框
        private JRadioButton rb_book; //预定
        private JRadioButton rb_check_in; //直接入住
        private JButton commit; //页面提交按钮
        Person(){
            panel_main=new JPanel();
            panel_main.setLayout(new BoxLayout(panel_main,BoxLayout.Y_AXIS));
            initInfoTable();
            initRoomChoice();
            initVipInfo();
            bookOrNow();
            commit();
            panel_main.add(panel1);
            panel_main.add(panel2);
            panel_main.add(panel3);
            panel_main.add(panel4);
            panel_main.add(commit);
        }
        public Component getMainPanel(){
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
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(table.getSelectedColumn()==5&&table.getSelectedRow()!=0){
                        model.removeRow(table.getSelectedRow());
                    }
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
            JLabel deposit_k=new JLabel("押金",JLabel.CENTER);
            JLabel deposit_v=new JLabel("",JLabel.CENTER);
            JLabel room_num=new JLabel("房间号",JLabel.CENTER);
            JPanel panel2_2_1=new JPanel();
            comboBox=new JComboBox();
            comboBox.setPreferredSize(new Dimension(200,30));
            try{
                ResultSet resultSet=database.QueryInfo("select rid from room where rtype='单人间' and rid not in (select rid from check_in) and rid not in (select rid from book)");
                while(resultSet.next()){
                    comboBox.addItem(resultSet.getString("rid"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            panel2_2_1.add(comboBox);
            panel2_2.add(price_k);
            panel2_2.add(price_v);
            panel2_2.add(deposit_k);
            panel2_2.add(deposit_v);
            panel2_2.add(room_num);
            panel2_2.add(panel2_2_1);
            border=BorderFactory.createTitledBorder(etched,"客房信息");
            panel2.setBorder(border);
            panel2.setPreferredSize(new Dimension(0,100));
            panel2.add(panel2_1);
            panel2.add(panel2_2);
            ButtonGroup group=new ButtonGroup();
            group.add(rb1);
            group.add(rb2);
            group.add(rb3);
            rb1.setSelected(true);
            price_v.setText("100");
            deposit_v.setText("50");
            ActionListener listener1=new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JRadioButton rb=(JRadioButton)actionEvent.getSource();
                    String type="";
                    if(rb==rb1){
                        price_v.setText("100");
                        deposit_v.setText("50");
                        type ="单人间";
                    }
                    else if(rb==rb2){
                        price_v.setText("140");
                        deposit_v.setText("70");
                        type="双人间";
                    }
                    else if(rb==rb3){
                        price_v.setText("220");
                        deposit_v.setText("110");
                        type="豪华套间";
                    }
                    Vector<String> results=new Vector<>();
                    try{
                        ResultSet resultSet=database.QueryInfo("select rid from room where rtype='"+type+"' and rid not in (select rid from check_in) and rid not in (select rid from book)");
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
            new Tools().addComponent(panel3_1,new JComponent[]{LvipCard,TvipCard,discount_k,discount_v,idcard_k,idcard_v,name_k,name_v});
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
        private void bookOrNow(){
            panel4=new JPanel();
            rb_book=new JRadioButton("预定");
            rb_check_in=new JRadioButton("入住");
            panel4.add(rb_check_in);
            panel4.add(rb_book);
            rb_check_in.setSelected(true);
            ButtonGroup group=new ButtonGroup();
            group.add(rb_book);
            group.add(rb_check_in);
            border=BorderFactory.createTitledBorder(etched,"预定/入住");
            panel4.setBorder(border);
        }
        private void commit(){
            commit=new JButton("提交");
            commit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    String roomType,price,roomNum,deposit=null;
                    if(rb1.isSelected()){
                        roomType="单人房";
                        price="100";
                        deposit="50";
                    }else if(rb2.isSelected()){
                        roomType="双人房";
                        price="140";
                        deposit="70";
                    }else if(rb3.isSelected()){
                        roomType="豪华套间";
                        price="220";
                        deposit="110";
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
                    if(rb_check_in.isSelected()){
                        //直接入住
                        for(int i=0;i<rowCount;i++) {
                            if (i == 0) {
                                database.Insert("customer", new String[]{data[i][0], data[i][1], data[i][2], data[i][3], remark});
                                database.Insert("deposit", new String[]{data[i][0], deposit});
                            } else {
                                database.Insert("customer", new String[]{data[i][0], data[i][1], data[i][2], data[i][3], "无"});
                            }
                            database.Insert("check_in", new String[]{data[i][0], roomNum, date});
                        }
                        new Tools().resetTabLayout(MainLayout.tabbedPane,new CustomerLayout().getMainPanel(),6);
                    }else{
                        //预定
                        for(int i=0;i<rowCount;i++){
                            if(i==0){
                                database.Insert("customer",new String[]{data[i][0],data[i][1],data[i][2],data[i][3],remark});
                                database.Insert("deposit",new String[]{data[i][0],deposit});
                            }else{
                                database.Insert("customer",new String[]{data[i][0],data[i][1],data[i][2],data[i][3],"无"});
                            }
                            database.Insert("book",new String[]{data[i][0],roomNum,date});
                        }
                        new Tools().resetTabLayout(MainLayout.tabbedPane,new BookInfoLayout().getMainPanel(),4);
                    }
                    new Tools().resetTabLayout(MainLayout.tabbedPane,new RoomInfoLayout().getMainPanel(),5);
                    MainLayout.tabbedPane.setSelectedIndex(0);
                    javax.swing.JOptionPane.showMessageDialog(null,"提交成功!");
                    new Tools().resetTabLayout(tabbedPane,new Person().getMainPanel(),0);
                }
            });
        }
    }

    class Group{
        private JPanel panel_main;
        private JPanel panel1;
        private JPanel panel2;
        private JPanel panel3;
        private JPanel panel4;
        JTable table;
        private Database database=new Database();
        JButton checkIn;
        JButton book;
        Border etched=BorderFactory.createEtchedBorder();
        Border border;
        private JCheckBox checkBox_201;
        private JCheckBox checkBox_202;
        private JCheckBox checkBox_203;
        private JCheckBox checkBox_204;
        private JCheckBox checkBox_301;
        private JCheckBox checkBox_302;
        private JCheckBox checkBox_303;
        private JCheckBox checkBox_304;
        private JCheckBox checkBox_401;
        private JCheckBox checkBox_402;
        private JCheckBox checkBox_403;
        private JCheckBox checkBox_404;
        private String [] roomId=new String[]{"201","202","203","204","301","302","303","304","401","402","403","404"};
        JLabel group_id;
        private JLabel deposit_k;
        private JLabel deposit_v;
        private JLabel count_k;
        private JLabel count_v;
        private Map<String,JCheckBox> map=new HashMap<String,JCheckBox>(){};
        Group(){
            panel_main=new JPanel();
            panel_main.setLayout(new BoxLayout(panel_main,BoxLayout.Y_AXIS));
            initInfoTable();
            initGroupId();
            initRoomChoose();
            initDeposit();
            commit();
            book();
            panel_main.add(panel1);
            panel_main.add(panel2);
            panel_main.add(panel3);
            panel_main.add(panel4);
            JPanel checkInAndBook=new JPanel();
            checkInAndBook.setMaximumSize(new Dimension(2000,50));
            checkInAndBook.add(checkIn);
            checkInAndBook.add(book);
            panel_main.add(checkInAndBook);
        }
        public Component getMainPanel(){
            return panel_main;
        }
        private void initInfoTable(){
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
            table=new JTable(model);
            table.getTableHeader().setReorderingAllowed(false); //设置列不可移动
            table.getColumnModel().getColumn(5).setMaxWidth(50);
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(table.getSelectedColumn()==5&&table.getSelectedRow()!=0){
                        model.removeRow(table.getSelectedRow());
                    }
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
            panel1=new JPanel();
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
        private void initGroupId(){
            panel2=new JPanel();
            JLabel label=new JLabel("团队编号:");
            group_id=new JLabel();
            try {
                ResultSet resultSet=database.QueryInfo("select count(*) from _group");
                if(resultSet.next()){
                    int groupNum=resultSet.getInt("count(*)");
                    if(groupNum==0){
                        group_id.setText("1");
                    }
                    else{
                        group_id.setText(Integer.toString(groupNum+1));
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            panel2.add(label);
            panel2.add(group_id);
            panel2.setMaximumSize(new Dimension(2000,80));
            border=BorderFactory.createTitledBorder(etched,"团队编号");
            panel2.setBorder(border);
        }
        private void initRoomChoose(){
            //客房选择
            panel3 =new JPanel();
            panel3.setLayout(new BoxLayout(panel3,BoxLayout.Y_AXIS));
            JPanel panel2_1=new JPanel(new GridLayout(0,2,0,0));
            JLabel head1=new JLabel("类型",JLabel.CENTER);
            JLabel head2=new JLabel("房号",JLabel.CENTER);
            JLabel label1=new JLabel("单人间（80/天）",JLabel.CENTER);
            mapPut(map, roomId);
            try{
                ResultSet room_id=database.QueryInfo("select rid from room where rid in (select rid from check_in) union (select rid from book)");
                while(room_id.next()){
                    map.get(room_id.getString("rid")).setEnabled(false);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            JPanel panel2_1_1=new JPanel();
            new Tools().addComponent(panel2_1_1,new JComponent[]{map.get("201"),map.get("202"),map.get("203"),map.get("204")});
            JLabel label2=new JLabel("双人间（120/天）",JLabel.CENTER);
            JPanel panel2_1_2=new JPanel();
            new Tools().addComponent(panel2_1_2,new JComponent[]{map.get("301"),map.get("302"),map.get("303"),map.get("304")});
            JLabel label3=new JLabel("豪华套间（200/天）",JLabel.CENTER);
            JPanel panel2_1_3=new JPanel();
            new Tools().addComponent(panel2_1_3,new JComponent[]{map.get("401"),map.get("402"),map.get("403"),map.get("404")});
            new Tools().addComponent(panel2_1,new JComponent[]{head1,head2,label1,panel2_1_1,label2,panel2_1_2,label3,panel2_1_3});
            panel3.add(panel2_1);
            border=BorderFactory.createTitledBorder(etched,"客房信息");
            panel3.setBorder(border);
            panel3.setPreferredSize(new Dimension(0,50));
            for(int i=0;i<roomId.length;i++){
                JCheckBox checkBox=map.get(roomId[i]);
                checkBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        int price1=Integer.parseInt(deposit_v.getText());
                        int rid=Integer.parseInt(checkBox.getText());
                        int tmp=0;
                        if(rid<300){
                            tmp=40;
                        }else if(rid<400){
                            tmp=60;
                        }else if(rid<500){
                            tmp=100;
                        }
                        if(checkBox.isSelected()){
                            price1+=tmp;
                        }else{
                            price1-=tmp;
                        }
                        count_v.setText(Integer.toString(2*price1));
                        deposit_v.setText(Integer.toString(price1));
                    }
                });
            }
        }
        private void initDeposit(){
            panel4=new JPanel();
            count_k=new JLabel("总额：");
            count_v=new JLabel("0");
            deposit_k=new JLabel("押金：");
            deposit_v=new JLabel("0");
            panel4.add(count_k);
            panel4.add(count_v);
            panel4.add(deposit_k);
            panel4.add(deposit_v);
            panel4.setMaximumSize(new Dimension(2000,50));
            border=BorderFactory.createTitledBorder(etched,"押金详情");
            panel4.setBorder(border);
        }
        private void commit(){
            checkIn =new JButton("提交");
            checkIn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    int rowCount=table.getRowCount();
                    database.Insert("_group",new String[]{group_id.getText(),(String)table.getValueAt(0,1),Integer.toString(rowCount)});
                    database.Insert("customer",new String[]{(String)table.getValueAt(0,1),(String)table.getValueAt(0,2),(String)table.getValueAt(0,3),(String)table.getValueAt(0,4),"领队"});
                    database.Insert("deposit",new String[]{(String)table.getValueAt(0,1),deposit_v.getText()});
                    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date=dateFormat.format(new Date());
                    for(int i=0;i<roomId.length;i++){
                        if(map.get(roomId[i]).isSelected()){
                            database.Insert("check_in",new String[]{(String)table.getValueAt(0,1),roomId[i],date});;
                        }
                    }
                    for(int i=1;i<rowCount;i++){
                        database.Insert("customer",new String[]{(String)table.getValueAt(i,1),(String)table.getValueAt(i,2),(String)table.getValueAt(i,3),(String)table.getValueAt(i,4),"团员"});
                        database.Insert("follow",new String[]{group_id.getText(),(String)table.getValueAt(i,1)});
                    }
                    javax.swing.JOptionPane.showMessageDialog(null,"提交成功!");
                    new Tools().resetTabLayout(MainLayout.tabbedPane,new RoomInfoLayout().getMainPanel(),5);
                    new Tools().resetTabLayout(MainLayout.tabbedPane,new CustomerLayout().getMainPanel(),6);
                    MainLayout.tabbedPane.setSelectedIndex(0);
                    new Tools().resetTabLayout(tabbedPane,new Group().getMainPanel(),1);
                }
            });
        }
        private void book(){
            book=new JButton("预约");
            book.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    int rowCount=table.getRowCount();
                    database.Insert("_group",new String[]{group_id.getText(),(String)table.getValueAt(0,1),Integer.toString(rowCount)});
                    database.Insert("customer",new String[]{(String)table.getValueAt(0,1),(String)table.getValueAt(0,2),(String)table.getValueAt(0,3),(String)table.getValueAt(0,4),"领队"});
                    database.Insert("deposit",new String[]{(String)table.getValueAt(0,1),deposit_v.getText()});
                    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date=dateFormat.format(new Date());
                    for(int i=0;i<roomId.length;i++){
                        if(map.get(roomId[i]).isSelected()){
                            database.Insert("book",new String[]{(String)table.getValueAt(0,1),roomId[i],date});
                        }
                    }
                    for(int i=1;i<rowCount;i++){
                        database.Insert("customer",new String[]{(String)table.getValueAt(i,1),(String)table.getValueAt(i,2),(String)table.getValueAt(i,3),(String)table.getValueAt(i,4),"团员"});
                        database.Insert("follow",new String[]{group_id.getText(),(String)table.getValueAt(i,1)});
                    }
                    javax.swing.JOptionPane.showMessageDialog(null,"提交成功!");
                    new Tools().resetTabLayout(MainLayout.tabbedPane,new BookInfoLayout().getMainPanel(),4);
                    new Tools().resetTabLayout(MainLayout.tabbedPane,new CustomerLayout().getMainPanel(),6);
                    MainLayout.tabbedPane.setSelectedIndex(0);
                    new Tools().resetTabLayout(tabbedPane,new Group().getMainPanel(),1);
                }
            });
        }
    }
    private void mapPut(Map<String ,JCheckBox> map,String [] s){
        for(int i=0;i<s.length;i++){
            map.put(s[i],new JCheckBox(s[i]));
        }
    }

}
