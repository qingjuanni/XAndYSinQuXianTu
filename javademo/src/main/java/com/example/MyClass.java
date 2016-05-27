package com.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MyClass {

//    public static void main(String[] args){
//
//
//
//    }


    private JFrame frame;
    private JTextField textField;
    private JButton btnNewButton;
    private JButton button;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        MyClass window = new MyClass();
        window.frame.setVisible(true);
    }

    /**
     * Create the application.
     */
    public MyClass() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("\u7528\u6237\u767B\u5F55");
        frame.setResizable(false);
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel label = new JLabel("\u7535\u5B50\u90AE\u7BB1\uFF1A");
        label.setBounds(68, 47, 71, 15);
        frame.getContentPane().add(label);

        JLabel label_1 = new JLabel("\u767B\u5F55\u5BC6\u7801\uFF1A");
        label_1.setBounds(68, 85, 71, 15);
        frame.getContentPane().add(label_1);

        JLabel label_2 = new JLabel("\u4E8C\u6B21\u786E\u8BA4\uFF1A");
        label_2.setBounds(68, 121, 71, 15);
        frame.getContentPane().add(label_2);

        textField = new JTextField("请输入登录邮箱");
        textField.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String s=textField.getText();
                if(s.equals("请输入登录邮箱")){
                    textField.setText("");
                }
            }
            public void mouseExited(MouseEvent e) {
                String s=textField.getText();
                if(s.equals("")||s.equals("请输入登录邮箱")){
                    textField.setText("请输入登录邮箱");
                }
            }
        });
        textField.setBounds(171, 44, 103, 21);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        button = new JButton("\u91CD\u586B");
        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String s=textField.getText();
                String m=passwordField.getText();
                String m1=passwordField_1.getText();
                if(s != "" && m != "" && m1 != ""){
                    textField.setText("");
                    passwordField.setText("");
                    passwordField_1.setText("");
                }else{
                    JOptionPane.showMessageDialog(frame, "请重填信息");
                }

            }
        });
        button.setBounds(92, 189, 71, 25);
        frame.getContentPane().add(button);

        btnNewButton = new JButton("\u6CE8\u518C");
        btnNewButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String s=textField.getText();
                String ss="\\d{5}@\\w{2}\\.[a-z]+";//邮箱格式
                String mm="\\.{6,}";//密码不少于6位
                @SuppressWarnings("deprecation")
                String m=passwordField.getText();
                @SuppressWarnings("deprecation")
                String m1=passwordField_1.getText();

                if(!s.matches(ss)){
                    JOptionPane.showMessageDialog(frame, "请输入正确的邮箱格式");
                }
                else if(s.matches(ss) && m.matches(mm) ){

                }
//				else if(!m1.equals(m)){
//					JOptionPane.showMessageDialog(frame, "请确认二次密码");
//				}
                else if(m1.equals(m)){
                    JOptionPane.showMessageDialog(frame, s+"注册成功");
                }
                else{
                    JOptionPane.showMessageDialog(frame, "注册失败");
                }


            }
        });
        btnNewButton.setBounds(239, 189, 80, 25);
        frame.getContentPane().add(btnNewButton);

        passwordField = new JPasswordField();
        passwordField.setBounds(171, 82, 103, 21);
        frame.getContentPane().add(passwordField);

        passwordField_1 = new JPasswordField();
        passwordField_1.setBounds(171, 118, 103, 21);
        frame.getContentPane().add(passwordField_1);
    }




}
