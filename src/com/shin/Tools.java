package com.shin;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class Tools {

    public void resetTabLayout(JTabbedPane tabbedPane,Component component, int index){
        String title=tabbedPane.getTitleAt(index);
        tabbedPane.remove(index);
        tabbedPane.insertTab(title,null,component,null,index);
        tabbedPane.setSelectedIndex(index);
    }
    public void addComponent(JPanel panel,JComponent [] components){
        for(int i=0;i<components.length;i++){
            panel.add(components[i]);
        }
    }
}
