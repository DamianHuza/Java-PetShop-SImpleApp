package forms;

import entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JDialog {
    private JPanel loginPanel;
    private JLabel title;
    private JLabel loginIcon;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JButton btnLogin;
    private JLabel lbRegister;
    private JButton btnRegister;
    public User user;

    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(490, 580));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tfPassword.setEchoChar((char) 0);


        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tfUsername.getText();
                String password = String.valueOf(tfPassword.getPassword());

                user = getValidUser(username, password);

                if (user != null) {
                    dispose();
                    //new LoginForm(null);
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Email or password invalid", "Try again", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterForm(null);
            }
        });

        tfUsername.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tfUsername.getText().equals("Username")) {
                    tfUsername.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tfUsername.getText().isEmpty()) {
                    tfUsername.setText("Username");
                }
            }
        });

        tfPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(tfPassword.getPassword()).equals("Password")) {
                    tfPassword.setText("");
                    tfPassword.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(tfPassword.getPassword()).isEmpty()) {
                    tfPassword.setText("Password");
                    tfPassword.setEchoChar((char) 0);
                }
            }
        });


        setVisible(true);

    }


    private User getValidUser(String username, String password) {
        User user = null;

        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM account WHERE name=? AND password=?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

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


    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
    }

}
