package forms;

import entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminConsole extends JDialog {
    private JTextField tfUsername;
    private JButton btnBan;
    private JComboBox<String> cbBreed;
    private JTextField tfAmount;
    private JButton btnRemove;
    private JButton btnAdd;
    private JPanel adminPanel;

    public AdminConsole(JFrame parent) {
        super(parent);
        setTitle("Admin");
        setContentPane(adminPanel);
        setMinimumSize(new Dimension(550, 600));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cbBreed.addItem("Syrian Hamster");
        cbBreed.addItem("Campbell Hamster");
        cbBreed.addItem("Winter White Hamster");
        cbBreed.addItem("Roborovski Hamster");
        cbBreed.addItem("Chinese Hamster");

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addHamster();
            }
        });
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeHamster();
            }
        });
        btnBan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                banAccount();
            }
        });
        setVisible(true);
    }

    private void addHamster() {
        String breed = (String) cbBreed.getSelectedItem();
        String amount = tfAmount.getText();

        if (!amount.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (addHamstersToDatabase(breed, Integer.parseInt(amount))) {
            JOptionPane.showMessageDialog(this,
                    "Stock updated",
                    "Done",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
            JOptionPane.showMessageDialog(this,
                    "There was an error",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void removeHamster() {
        String breed = (String) cbBreed.getSelectedItem();
        String amount = tfAmount.getText();

        if (!amount.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Integer.parseInt(amount) > getStock(breed)) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (removeHamstersFormDatabase(breed, Integer.parseInt(amount))) {
            JOptionPane.showMessageDialog(this,
                    "Stock updated",
                    "Done",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
            JOptionPane.showMessageDialog(this,
                    "There was an error",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private boolean addHamstersToDatabase(String breed, int nr) {
        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";
        boolean ok = false;

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            // Connected to database successfully...

            Statement stmt = conn.createStatement();
            String sql = "UPDATE breed SET stock=stock+? WHERE name=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, nr);
            preparedStatement.setString(2, breed);

            preparedStatement.executeUpdate();
            ok = true;

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    private boolean removeHamstersFormDatabase(String breed, int nr) {
        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";
        boolean ok = false;

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            // Connected to database successfully...

            Statement stmt = conn.createStatement();
            String sql = "UPDATE breed SET stock=stock-? WHERE name=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, nr);
            preparedStatement.setString(2, breed);

            preparedStatement.executeUpdate();
            ok = true;

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    private void banAccount() {

        String name = tfUsername.getText();
        User user = getValidUser(name);
        if(user==null){
            JOptionPane.showMessageDialog(this,
                    "Account doesn't exists",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(user.admin)
        {
            JOptionPane.showMessageDialog(this,
                    "Can't ban admins",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        removeOrders(user);

        if(removeAccountFromDatabase(name)){
            JOptionPane.showMessageDialog(this,
                    "Account banned",
                    "Done",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Error",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    private void removeOrders(User user) {
        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            // Connected to database successfully...

            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM orders WHERE account_id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, user.id);

            preparedStatement.executeUpdate();

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean removeAccountFromDatabase(String name) {
        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";
        boolean ok = false;

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            // Connected to database successfully...

            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM account WHERE name=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();
            ok = true;

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    private User getValidUser(String username) {
        User user = null;

        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM account WHERE name=?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.id = resultSet.getInt("acc_id");
                user.name = resultSet.getString("name");
                user.password = resultSet.getString("password");
                user.admin = resultSet.getBoolean("admin");
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    private int getStock(String breed) {
        int nr = 0;

        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

            Statement stmt = conn.createStatement();
            String sql = "SELECT stock FROM breed WHERE name=?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, breed);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                nr = resultSet.getInt("stock");
            }


            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nr;
    }

    public static void main(String[] args) {
        AdminConsole cons = new AdminConsole(null);
    }
}
