/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.LibraryApp;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Library {
    private JFrame frame;
    private JTable bookTable;
    private JTextField bookIDField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField yearField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Library window = new Library();
                window.frame.setVisible(true);
            } catch (Exception e) {
            }
        });
    }

    public Library() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Library App");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        // Add the book table
        bookTable = new JTable();
        container.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // Add the book form
        JPanel bookForm = new JPanel();
        bookForm.add(new JLabel("Book ID:"));
        bookForm.add(bookIDField = new JTextField(20));
        bookForm.add(new JLabel("Title:"));
        bookForm.add(titleField = new JTextField(20));
        bookForm.add(new JLabel("Author:"));
        bookForm.add(authorField = new JTextField(20));
        bookForm.add(new JLabel("Year:"));
        bookForm.add(yearField = new JTextField(5));
        container.add(bookForm, BorderLayout.NORTH);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(new JButton(new AddBookAction()));
        buttonPanel.add(new JButton(new DeleteBookAction()));
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Inner classes for buttons
    private class AddBookAction extends AbstractAction {
        public AddBookAction() {
            super("Add");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Add book functionality
        String bookID = bookIDField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String year = yearField.getText();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO LibDatabase (BookID, Title, Author, Year) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, bookID);
            statement.setString(2, title);
            statement.setString(3, author);
            statement.setString(4, year);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(bookTable, "Book added successfully.");
                // Update the table model
                DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
                model.addRow(new Object[]{bookID, title, author, year});
            } else {
                JOptionPane.showMessageDialog(bookTable, "Failed to add book.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(bookTable, "Error adding book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        }
    }

    private class DeleteBookAction extends AbstractAction {
        public DeleteBookAction() {
            super("Delete");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Delete book functionality
            int selectedRow = bookTable.getSelectedRow();

        if (selectedRow >= 0) {
            String bookID = (String) bookTable.getValueAt(selectedRow, 0);

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM LibDatabase WHERE BookID = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, bookID);
                int rowsAffected = statement.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(bookTable, "Book deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(bookTable, "Book not found.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(bookTable, "Error deleting book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(bookTable, "No book selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
   
        }
    }
}


