package com.main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MyFrame();
        });
//        SwingUtilities.invokeLater(MyFrame::new);
    }
}
