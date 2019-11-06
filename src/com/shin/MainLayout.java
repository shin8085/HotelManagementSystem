package com.shin;

import javax.swing.*;

public class MainLayout {

    private JFrame frame_main;
    private JTabbedPane tabbedPan;

    public MainLayout(){
        frame_main=new JFrame("宾馆管理系统");
        frame_main.setSize(1200,700);
        tabbedPan=new JTabbedPane();
    }
    private void CreateWindow(){
        CustomerLayout customerLayout=new CustomerLayout();
        tabbedPan.addTab("入住",customerLayout.getMainPanel());
        tabbedPan.addTab("房间信息",new JPanel());
        tabbedPan.addTab("贵宾卡",new JPanel());
        frame_main.setContentPane(tabbedPan);
        frame_main.setVisible(true);
    }
    public static void main(String[] args) {
        MainLayout main=new MainLayout();
        main.CreateWindow();
    }
}
