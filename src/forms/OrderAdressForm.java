package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderAdressForm extends JDialog{
    private JTextField tfCity;
    private JTextField tfStreet;
    private JTextField tfNr;
    private JTextField tfAp;
    private JButton btnConfirm;
    private JButton btnCancel;
    private JPanel orderAdressPanel;
    private JLabel lbPrice;

    public boolean confirm = false;

    public String adress = null;

    public OrderAdressForm (JFrame parent, int price){
        super(parent);
        setTitle("Login");
        setContentPane(orderAdressPanel);
        setMinimumSize(new Dimension(500, 485));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        lbPrice.setText("Final Price: " + String.valueOf(price) + " RON");

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildAdress();
                if(confirm) dispose();
            }
        });
        setVisible(true);
    }

    public void buildAdress(){
        String city = tfCity.getText();
        String street = tfStreet.getText();
        String apartment = tfAp.getText();
        String number = tfNr.getText();

        if(!city.matches("[ a-zA-Z]+")){
            JOptionPane.showMessageDialog(this,
                    "Invalid city",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!street.matches("[ a-zA-Z]+")){
            JOptionPane.showMessageDialog(this,
                    "Invalid street",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!number.matches("[0-9]+")){
            JOptionPane.showMessageDialog(this,
                    "Invalid Nr",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!apartment.matches("[0-9]+") && !apartment.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Invalid Ap",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        confirm=true;
        adress = city + ", " + street + " " + number;
        if(!apartment.isEmpty()){
            adress = adress + ", ap " + apartment;
        }
    }

    public static void main(String[] args){
        OrderAdressForm o = new OrderAdressForm(null, 0);
    }
}
