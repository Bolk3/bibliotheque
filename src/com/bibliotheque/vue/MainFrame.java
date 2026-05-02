package com.bibliotheque.vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame{

    JPanel          menu;
    JPanel          current;
    Vector<JPanel>  history = new Vector<>();
    Integer         tab = -1;

    public MainFrame() {
        setLayout(new BorderLayout());
        getContentPane().setPreferredSize(new Dimension(720, 480));
        pack();
    }

    private void deleteHistoryFrom(int index) {
        while (history.size() > index) {
            history.removeLast();
        }
    }

    public boolean canGoBack() {
        return tab > 0;
    }
 
    public boolean canGoNext() {
        return tab >= 0 && !isLast();
    }
 
    private boolean isLast() {
        return history.isEmpty() || (tab + 1) == history.size();
    }

    private void updateFrame() {
        if (tab < 0 || tab >= history.size()) return;
 
        current = history.get(tab);
 
        getContentPane().removeAll();
 
        if (menu != null) {
            getContentPane().add(menu, BorderLayout.WEST);
        }
        if (current != null) {
            getContentPane().add(current, BorderLayout.CENTER);
        }
 
        revalidate();
        repaint();
    }

    public void goForward(JPanel panel) {
        if (!isLast()) {
            deleteHistoryFrom(tab + 1);
        }
        history.add(panel);
        tab++;
        updateFrame();
    }

    public void goBack() {
        if (!canGoBack()) return;
        tab--;
        updateFrame();
    }

    public void goNext() {
        if (!canGoNext()) return;
        tab++;
        updateFrame();
    }

    public JPanel getCurrentPanel() {
        return current;
    }

    public int getHistorySize() {
        return history.size();
    }

    public int getCurrentIndex() {
        return tab;
    }

    public void setMenu(JPanel menu) {
        this.menu = menu;
        updateFrame();
    }

}
