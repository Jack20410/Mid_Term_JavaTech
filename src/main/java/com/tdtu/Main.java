package com.tdtu;

import com.tdtu.ui.AppFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppFrame app = new AppFrame();
            app.setVisible(true);
        });
    }
}
