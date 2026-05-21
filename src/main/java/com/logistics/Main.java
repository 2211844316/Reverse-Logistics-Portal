package com.logistics;

import com.logistics.gui.MainFrame;
import javax.swing.*;

/**
 * Main entry point for the Reverse Logistics Portal application.
 * 
 * This application demonstrates three design patterns:
 * 1. Strategy Pattern - Different refund processing methods
 * 2. Observer Pattern - Status change notifications
 * 3. Singleton Pattern - Centralized configuration management
 */
public class Main {
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Customize Nimbus defaults
        UIManager.put("control", new java.awt.Color(241, 245, 249));
        UIManager.put("nimbusBase", new java.awt.Color(99, 102, 241));
        UIManager.put("nimbusBlueGrey", new java.awt.Color(148, 163, 184));

        // Launch application on EDT
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            System.out.println("===========================================");
            System.out.println("  Reverse Logistics Portal - Started");
            System.out.println("  Design Patterns: Strategy, Observer, Singleton");
            System.out.println("===========================================");
        });
    }
}
