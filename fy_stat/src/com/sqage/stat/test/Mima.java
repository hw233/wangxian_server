package com.sqage.stat.test;
//用Java实现的字符串简单加密：
//用一个字符串 Key 加密原串
//请高手提提意见哦 
//JDK 5.0编译通过
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;

public class Mima {

	  char key;
	  public static void main(String[] args) throws Exception{
	    new Mima();
	  }
	  public Mima(){
	    MyGui tw = new MyGui("文件加密对话框");
	    tw.setVisible(true);
	  }
	  //加密
	  public String jiaMi(String s,String key){
	    String str = "";
	    int ch;
	    if(key.length() == 0){
	        return s;
	    }
	    else if(!s.equals(null)){
	        for(int i = 0,j = 0;i < s.length();i++,j++){
	          if(j > key.length() - 1){
	            j = j % key.length();
	          }
	          ch = s.codePointAt(i) + key.codePointAt(j);
	          if(ch > 65535){
	            ch = ch % 65535;//ch - 33 = (ch - 33) % 95 ;
	          }
	          str += (char)ch;
	        }
	    }
	    return str;

	  } 
	  //解密
	  public String jieMi(String s,String key){
	    String str = "";
	    int ch;
	    if(key.length() == 0){
	        return s;
	    }
	    else if(!s.equals(key)){
	        for(int i = 0,j = 0;i < s.length();i++,j++){
	          if(j > key.length() - 1){
	            j = j % key.length();
	          }
	          ch = (s.codePointAt(i) + 65535 - key.codePointAt(j));
	          if(ch > 65535){
	            ch = ch % 65535;//ch - 33 = (ch - 33) % 95 ;
	          }
	          str += (char)ch;
	        }
	    }
	    return str;
	  }
	  class MyGui extends JFrame
	  {
	    JButton b1 = new JButton("Encrypt");
	    JButton b2 = new JButton("Decrypt");
	    Panel pan1 = new Panel();
	    Panel pan2 = new Panel();
	    Panel pan3 = new Panel();
	    Panel pan4 = new Panel();
	    Panel pan5 = new Panel();
	    //
	    Label label1 = new Label("Sourse File");
	    Label label2 = new Label("Obj File");
	    Label label3 = new Label("Key:");
	    
	    
	    TextArea tf1 = new TextArea("Text Area 1",4,30,TextArea.SCROLLBARS_VERTICAL_ONLY);
	    TextArea tf2 = new TextArea("Text Area 2",4,30,TextArea.SCROLLBARS_VERTICAL_ONLY);
	    TextField tf3 = new TextField("key     ");
	    Toolkit theKit = getToolkit();
	    public MyGui()
	    {
	        init();
	    }
	    public MyGui(String title)
	    {
	        super(title);
	        init();
	    }
	    private void init()
	    {
	        setLayout(new BorderLayout());
	        pan1.setLayout(new GridLayout(1,2));
	        //
	        label1.setBackground(new Color(220,220,220));
	        label2.setBackground(new Color(220,220,220));
	        label3.setBackground(new Color(220,220,220));
	        //
	        tf3.setEchoChar('*');
	        //   
	            
	        Dimension wndSize = theKit.getScreenSize();
	        setBounds(wndSize.width/4,wndSize.height/4,
	                wndSize.width/2,wndSize.height/4);
	        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	        //LookAndFeel
	        setUndecorated(true);
	        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
	        //加密
	        b1.setToolTipText("加密");
	        b1.setMnemonic('E');
	        b1.addActionListener(new ActionListener(){
	          public void actionPerformed(ActionEvent e){
	            tf2.setText(jiaMi(tf1.getText(),tf3.getText()));
	            //tf3.setText("");
	          }
	        });
	        b1.addMouseListener(new MouseAdapter(){
	          public void mouseClicked(MouseEvent e){
	            tf2.setText(jiaMi(tf1.getText(),tf3.getText()));
	            //tf3.setText("");
	          }
	        });
	        //解密
	        b2.setToolTipText("解密");
	        b2.setMnemonic('D');
	        b2.addActionListener(new ActionListener(){
	          public void actionPerformed(ActionEvent e){
	            tf2.setText(jieMi(tf1.getText(),tf3.getText()));
	            //tf3.setText("");
	          }
	        });
	        b2.addMouseListener(new MouseAdapter(){
	          public void mouseClicked(MouseEvent e){
	            tf2.setText(jieMi(tf1.getText(),tf3.getText()));
	            //tf3.setText("");
	          }
	        });
	        //
	        pan1.add(pan2);
	        pan1.add(pan3);
	        pan2.add(label1);
	        pan3.add(label2);
	        pan4.add(tf1);
	        pan4.add(tf2);
	        pan5.add(b1);
	        pan5.add(b2);
	        pan5.add(label3);
	        pan5.add(tf3);       
	        //.
	        add("North",pan1);
	        add("South",pan5);
	        add("Center",pan4);
	    }
	  } 
}
