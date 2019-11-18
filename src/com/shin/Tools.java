package com.shin;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;

public class Tools {

    public void resetTabLayout(JTabbedPane tabbedPane,Component component, int index){
        String title=tabbedPane.getTitleAt(index);
        tabbedPane.remove(index);
        tabbedPane.insertTab(title,null,component,null,index);
        tabbedPane.setSelectedIndex(index);
    }
}
