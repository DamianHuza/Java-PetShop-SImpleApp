package forms;

import entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Pattern;

public class RegisterForm extends JDialog {
    private JPanel registerPanel;
    private JTextField tfUsername;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnCancel;
    private JButton btnRegister;
    private JTextField tfPhone;

    public User user;

    public RegisterForm(JFrame parent) {
        super(parent);
        setTitle("Register");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(720, 570));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });


        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfUsername.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String passwordConfirm = String.valueOf(pfConfirmPassword.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all required fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(passwordConfirm)) {
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!checkUsername(name)) {
            JOptionPane.showMessageDialog(this,
                    "Username taken",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!isEmailFormat(email)){
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        else{
            if(!checkEmail(email)){
                JOptionPane.showMessageDialog(this,
                        "Email already in use",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if(!phone.isEmpty()){
            if (!phone.matches("[0-9]+") || phone.length()!=10 || phone.charAt(0)!='0' || phone.charAt(1)!='7'){
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid phone number",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }



        if (phone.isEmpty()) {
            user = addUserToDatabase(name, email, password);
        } else {
            user = addUserToDatabase(name, email, password, phone);
        }

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "New user: " + user.name,
                    "Account created successfully",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private User addUserToDatabase(String name, String email, String password, String phone) {
        User user = null;
        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            // Connected to database successfully...

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO account (name, password, email, admin, phone) " +
                    "VALUES (?, ?, ?, false, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);

            //Insert row into the table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.password = password;
                user.admin = false;
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    private User addUserToDatabase(String name, String email, String password) {
        User user = null;
        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            // Connected to database successfully...

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO account (name, password, email, admin) " +
                    "VALUES (?, ?, ?, false)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);

            //Insert row into the table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.password = password;
                user.admin = false;
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static boolean isEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private boolean checkUsername(String name) {
        boolean valid = true;

        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM account WHERE name=?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                valid = false;
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return valid;
    }

    private boolean checkEmail(String email) {
        boolean valid = true;

        final String dbURL = "jdbc:postgresql://localhost:5432/Hamster_db";
        final String dbUsername = "postgres";
        final String dbPassword = "12345678";

        try {
            Connection conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM account WHERE email=?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                valid = false;
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return valid;
    }

    public static void main(String[] args) {
        RegisterForm myForm = new RegisterForm(null);
    }

}
