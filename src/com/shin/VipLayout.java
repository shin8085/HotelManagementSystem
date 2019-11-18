package com.shin;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

//贵宾卡界面
public class VipLayout {
    private JTabbedPane tabbedPane;
    private Database database=new Database();

    private JTextField field1=new JTextField(20);
    private JTextField field2=new JTextField(20);
    private JTextField field3=new JTextField(20);
    private JTextField field4=new JTextField(20);
    private JTextField field5=new JTextField(20);


    public VipLayout(){
        tabbedPane=new JTabbedPane();
    }

    public Component getMainPanel(){
        tabbedPane.addTab("办理",getManage());
        tabbedPane.addTab("已办理用户",getManaged());
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        return tabbedPane;
    }

    //办理VIP
    private Component getManage(){
        JPanel panel_main=new JPanel();
        JPanel panel1=new JPanel(new GridLayout(0,2,0,0));
        panel1.setBorder(BorderFactory.createEmptyBorder(165,350,165,350));
        JLabel label1=new JLabel("卡号",JLabel.CENTER);
        JPanel textField1=panelTextFiled(field1);
        JLabel label2=new JLabel("身份证号",JLabel.CENTER);
        JPanel textField2=panelTextFiled(field2);
        JLabel label3=new JLabel("姓名",JLabel.CENTER);
        JPanel textField3=panelTextFiled(field3);
        JLabel label4=new JLabel("性别",JLabel.CENTER);
        JPanel textField4=panelTextFiled(field4);
        JLabel label5=new JLabel("联系电话",JLabel.CENTER);
        JPanel textField5=panelTextFiled(field5);
        addComponent(panel1,new JComponent[]{label1,textField1,label2,textField2,label3,textField3,label4,textField4,label5,textField5});
        setId();
        JButton commit=new JButton("提交");
        commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date=dateFormat.format(new Date());
                database.Insert("vip_card",new String[]{field1.getText(),field2.getText(),field3.getText(),field4.getText(),field5.getText(),date});
                javax.swing.JOptionPane.showMessageDialog(null,"提交成功!");
                /*field1.setText("");
                field2.setText("");
                field3.setText("");
                field4.setText("");
                field5.setText("");*/
                //setId(); //初始化vip卡号
                //更新显示界面
                new Tools().resetTabLayout(tabbedPane,getManaged(),1);
                new Tools().resetTabLayout(tabbedPane,new VipLayout().getManage(),0);
            }
        });
        panel_main.add(panel1);
        panel_main.add(commit);
        return panel_main;
    }

    //已办理vip
    private Component getManaged(){
        JPanel panel_main=new JPanel(new BorderLayout());
        String[] tableTitle={"卡号","身份证号","姓名","性别","电话号码","办理时间","删除"};
        //String[][] rowdata={{"","","","","","","X"}};
        String[][] rowdata=new String[1][];
        try {
            ResultSet rowCount=database.QueryInfo("select count(*) from vip_card");
            ResultSet resultSet=database.QueryInfo("select * from vip_card");
            int row=0;
            if(rowCount.next()){
                String data[][]=new String[rowCount.getInt("count(*)")][];
                rowdata=data;
            }
            while(resultSet.next()){
                rowdata[row]=new String[7];
                rowdata[row][0]=resultSet.getString("vid");
                rowdata[row][1]=resultSet.getString("idnum");
                rowdata[row][2]=resultSet.getString("cname");
                rowdata[row][3]=resultSet.getString("sex");
                rowdata[row][4]=resultSet.getString("telphone");
                rowdata[row][5]=resultSet.getString("mtime");
                rowdata[row][6]="X";
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
        table.getColumnModel().getColumn(6).setMaxWidth(50);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(table.getSelectedColumn()==6){
                    String idcard=(String)table.getValueAt(table.getSelectedRow(),0);
                    model.removeRow(table.getSelectedRow());
                    String sql="delete from vip_card where vid='"+idcard+"'";
                    database.UpdataInfo(sql);
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
                if(column==6){
                    background=Color.red;
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
    private void setId(){
        field1.setEditable(false);
        try{
            ResultSet resultSet=database.QueryInfo("select max(vid) from vip_card");
            if(resultSet.next()){
                long n;
                if(resultSet.getLong("max(vid)")>0){
                    n=resultSet.getLong("max(vid)")+1;
                }else{
                    n=2703000001L;
                }
                field1.setText(Long.toString(n));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
