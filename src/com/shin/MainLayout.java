package com.shin;

import com.sun.source.tree.LambdaExpressionTree;

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
        CheckInOrBookLayout checkInOrBookLayout=new CheckInOrBookLayout();
        VipLayout vipLayout=new VipLayout();
        ChangeRoomLayout changeRoomLayout=new ChangeRoomLayout();
        CheckoutLayout checkoutLayout=new CheckoutLayout();
        CustomerLayout customerLayout=new CustomerLayout();
        RoomInfoLayout roomInfoLayout=new RoomInfoLayout();
        BookInfoLayout bookInfoLayout=new BookInfoLayout();
        tabbedPan.addTab("入住/预定",checkInOrBookLayout.getMainPanel());
        tabbedPan.addTab("换房",changeRoomLayout.getMainPanel());
        tabbedPan.addTab("退房",checkoutLayout.getMainPanel());
        tabbedPan.addTab("贵宾卡",vipLayout.getMainPanel());
        tabbedPan.addTab("预定信息",bookInfoLayout.getPanelMain());
        tabbedPan.addTab("房间信息",roomInfoLayout.getMainPanel());
        tabbedPan.addTab("顾客信息",customerLayout.getMainPanel());
        frame_main.setContentPane(tabbedPan);
        frame_main.setVisible(true);
    }

    public static void main(String[] args) {
        MainLayout main=new MainLayout();
        main.CreateWindow();
    }

}
