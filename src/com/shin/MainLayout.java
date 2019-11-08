package com.shin;

import javax.swing.*;


//主界面
public class MainLayout {

    private JFrame frame_main;
    private JTabbedPane tabbedPan;

    public MainLayout(){
        frame_main=new JFrame("宾馆管理系统");
        frame_main.setSize(1200,700);
        tabbedPan=new JTabbedPane();
    }
    private void CreateWindow(){
        ChekInLayout chekInLayout=new ChekInLayout();
        VipLayout vipLayout=new VipLayout();
        ChangeRoomLayout changeRoomLayout=new ChangeRoomLayout();
        CheckoutLayout checkoutLayout=new CheckoutLayout();
        CustomerLayout customerLayout=new CustomerLayout();
        tabbedPan.addTab("入住",chekInLayout.getMainPanel());
        tabbedPan.addTab("换房",changeRoomLayout.getMainPanel());
        tabbedPan.addTab("退房",checkoutLayout.getMainPanel());
        tabbedPan.addTab("贵宾卡",vipLayout.getMainPanel());
        tabbedPan.addTab("房间信息",new JPanel());
        tabbedPan.addTab("顾客信息",customerLayout.getMainPanel());
        frame_main.setContentPane(tabbedPan);
        frame_main.setVisible(true);
    }
    public static void main(String[] args) {
        MainLayout main=new MainLayout();
        main.CreateWindow();
    }
}
