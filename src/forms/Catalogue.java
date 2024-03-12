package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Catalogue extends JFrame {
    private JButton btnLogout;
    private JButton btnRefresh;
    private JPanel cataloguePane;
    private JLabel lbStock1;
    private JLabel lbStock2;
    private JLabel lbStock3;
    private JLabel lbStock4;
    private JLabel lbStock5;
    private JLabel lbPrice1;
    private JLabel lbPrice2;
    private JLabel lbPrice3;
    private JLabel lbPrice4;
    private JLabel lbPrice5;
    private JButton btnOrder;
    private JScrollPane scrollPane;
    private JLabel lbAccount;
    private JPanel controlPanel1;
    private JPanel controlPanel2;
    private JPanel controlPanel3;
    private JPanel controlPanel4;
    private JPanel controlPanel5;
    private JTextField tfAmount1;
    private JTextField tfAmount2;
    private JTextField tfAmount3;
    private JTextField tfAmount4;
    private JTextField tfAmount5;
    private JButton btnAdmin;
    private LoginForm loginForm;

    public Catalogue() {
        setTitle("Catalogue");
        setContentPane(cataloguePane);
        //setSize(1000,400);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        setMinimumSize(new Dimension(900, 900));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        refresh();

        loginForm = new LoginForm(this);
        if (loginForm.user != null) {
            lbAccount.setText(loginForm.user.getName());
            setVisible(true);
            if(!loginForm.user.isAdmin()){
                btnAdmin.setVisible(false);
            }

        } else {
            dispose();
        }

        //setVisible(true);
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Catalogue();
            }
        });
        btnOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });
        btnAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminConsole(null);
            }
        });
    }

    private void refresh() {
        //region Stock
        if (getStock(1) != 0) {
            lbStock1.setForeground(new Color(46, 199, 0));
            lbStock1.setText("In Stock");
            controlPanel1.setVisible(true);
        } else {
            lbStock1.setForeground(new Color(201, 14, 14));
            lbStock1.setText("Stock empty");
            controlPanel1.setVisible(false);
        }

        if (getStock(2) != 0) {
            lbStock2.setForeground(new Color(46, 199, 0));
            lbStock2.setText("In Stock");
            controlPanel2.setVisible(true);
        } else {
            lbStock2.setForeground(new Color(201, 14, 14));
            lbStock2.setText("Stock empty");
            controlPanel2.setVisible(false);
        }

        if (getStock(3) != 0) {
            lbStock3.setForeground(new Color(46, 199, 0));
            lbStock3.setText("In Stock");
            controlPanel3.setVisible(true);
        } else {
            lbStock3.setForeground(new Color(201, 14, 14));
            lbStock3.setText("Stock empty");
            controlPanel3.setVisible(false);
        }

        if (getStock(4) != 0) {
            lbStock4.setForeground(new Color(46, 199, 0));
            lbStock4.setText("In Stock");
            controlPanel4.setVisible(true);
        } else {
            lbStock4.setForeground(new Color(201, 14, 14));
            lbStock4.setText("Stock empty");
            controlPanel4.setVisible(false);
        }

        if (getStock(5) != 0) {
            lbStock5.setForeground(new Color(46, 199, 0));
            lbStock5.setText("In Stock");
            controlPanel5.setVisible(true);
        } else {
            lbStock5.setForeground(new Color(201, 14, 14));
            lbStock5.setText("Stock empty");
            controlPanel5.setVisible(false);
        }
        //endregion

        //region Price & Amount
        lbPrice1.setText(String.valueOf(getPrice(1)) + " RON");
        lbPrice2.setText(String.valueOf(getPrice(2)) + " RON");
        lbPrice3.setText(String.valueOf(getPrice(3)) + " RON");
        lbPrice4.setText(String.valueOf(getPrice(4)) + " RON");
        lbPrice5.setText(String.valueOf(getPrice(5)) + " RON");

        tfAmount1.setText("");
        tfAmount2.setText("");
        tfAmount3.setText("");
        tfAmount4.setText("");
        tfAmount5.setText("");
        //endregion
    }

    private int getStock(int id) {
        int nr = 0;

        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

            Statement stmt = conn.createStatement();
            String sql = "SELECT stock FROM breed WHERE breed_id=?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

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

    private int getPrice(int id) {
        int price = 0;

        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM breed WHERE breed_id=?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                price = resultSet.getInt("price");
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    private void placeOrder() {
        ArrayList<String> amount = new ArrayList<>();
        ArrayList<Integer> nr = new ArrayList<>();
        int price = 0;
        String order = "";
        final List<String> NAMES = List.of("Syrian Hamster", "Campbell Hamster", "Winter White Hamster", "Roborovski Hamster", "Chinese Hamster");

        amount.add(tfAmount1.getText());
        amount.add(tfAmount2.getText());
        amount.add(tfAmount3.getText());
        amount.add(tfAmount4.getText());
        amount.add(tfAmount5.getText());

        //checking for invalid ammounts
        for (int i = 0; i < amount.size(); i++) {
            if (amount.get(i).isEmpty()) {
                nr.add(0);
            } else if (amount.get(i).matches("[0-9]+")) {
                nr.add(Integer.parseInt(amount.get(i)));
            } else {
                JOptionPane.showMessageDialog(this, amount.get(i) + " is not a valid quantity", "Try again", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        //checking stock
        for (int i = 0; i < nr.size(); i++) {
            if (nr.get(i) > getStock(i + 1)) {
                JOptionPane.showMessageDialog(this, "Looks like we don't have enough " + NAMES.get(i) + "s", "Try again", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        //calculating price
        for (int i = 0; i < nr.size(); i++) {
            if (nr.get(i) > 0) {
                price = price + nr.get(i) * getPrice(i + 1);
            }
        }
        if (price == 0) {
            JOptionPane.showMessageDialog(this, "Cannot place an empty order", "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //building order
        for (int i = 0; i < nr.size(); i++) {
            if ((nr.get(i) > 0)) {
                order = order + amount.get(i) + " " + NAMES.get(i) + "    \n";
            }
        }

        //confirming order
        OrderAdressForm ord = new OrderAdressForm(this, price);
        boolean ok = false;

        if (ord.confirm) {

            //adding order
            ok = addOrderToDatabase(loginForm.user.id, order, ord.adress, price);

            //updating stock
            for (int i = 0; i < nr.size(); i++) {
                if ((nr.get(i) > 0)) {
                    removeHamstersFormDatabase(NAMES.get(i), nr.get(i));
                }
            }
        }
        //database feedback
        if (ok) {
            JOptionPane.showMessageDialog(this, "Order placed successfully", "DONE", JOptionPane.INFORMATION_MESSAGE);
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Order canceled", "Try again", JOptionPane.INFORMATION_MESSAGE);
        }
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

    private boolean addOrderToDatabase(int id, String order, String adress, int price) {
        boolean ok = false;
        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            // Connected to database successfully...

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO orders (account_id, content, adress, price) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, order);
            preparedStatement.setString(3, adress);
            preparedStatement.setInt(4, price);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) ok = true;
            //Insert row into the table

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    public static void main(String[] args) {
        Catalogue cat = new Catalogue();
    }
}
