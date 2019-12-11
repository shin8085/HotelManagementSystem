package com.shin;

import javax.swing.*;


//主界面
public class MainLayout {

    private JFrame frame_main;
    public static JTabbedPane tabbedPane;

    public MainLayout(){
        frame_main=new JFrame("宾馆管理系统");
        frame_main.setSize(1200,700);
        frame_main.setResizable(false);
        tabbedPane =new JTabbedPane();
    }
    private void CreateWindow(){
        CheckInOrBookLayout checkInOrBookLayout=new CheckInOrBookLayout();
        VipLayout vipLayout=new VipLayout();
        ChangeRoomLayout changeRoomLayout=new ChangeRoomLayout();
        CheckoutLayout checkoutLayout=new CheckoutLayout();
        CustomerLayout customerLayout=new CustomerLayout();
        RoomInfoLayout roomInfoLayout=new RoomInfoLayout();
        BookInfoLayout bookInfoLayout=new BookInfoLayout();
        tabbedPane.addTab("入住/预定",checkInOrBookLayout.getMainPanel());
        tabbedPane.addTab("换房",changeRoomLayout.getMainPanel());
        tabbedPane.addTab("退房",checkoutLayout.getMainPanel());
        tabbedPane.addTab("贵宾卡",vipLayout.getMainPanel());
        tabbedPane.addTab("预定信息",bookInfoLayout.getMainPanel());
        tabbedPane.addTab("房间信息",roomInfoLayout.getMainPanel());
        tabbedPane.addTab("顾客信息",customerLayout.getMainPanel());
        frame_main.setContentPane(tabbedPane);
        frame_main.setVisible(true);
    }

    public static void main(String[] args) {
        MainLayout main=new MainLayout();
        main.CreateWindow();
    }

}
