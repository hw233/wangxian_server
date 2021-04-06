
package com.xuanzhi.tools.mail;

import javax.mail .internet .*;
import javax.mail.*;
import javax.activation .*;
import com.xuanzhi.tools.text.*;

/**
 * JavaMail send utils.
 * 
 */
public final class JavaMailUtils
{
	/**
	 * java -cp airinbox-tools.jar;d:\lib\mail.jar;d:\lib\smtp.jar;d:\lib\pop3.jar;d:\lib\activation.jar com.airinbox.util.JavaMailUtils
	 * 
	 */
	public static void main(String args[]) throws Exception 
	{
		JavaMailUtils.sendMail(args);
	}
	
	/**
	 *  args list here:<br>
	 *  <-smtp smtpHost> <-from fromAddress> <-to toAddress[;toAddess]> 
	 *  [-cc ccAddress[;ccAddress]] [-bcc ccAddress[;ccAddress]] [-subject subject] [-message message] 
	 *  [-header key=value[;key=value]] [-contenttype type]
	 *  [-attachment filename[;filename]] [-username username] [-password password] [-debug]
	 */
	public static void sendMail(String args[]) throws Exception
	{
		String smtpHost = null;
		InternetAddress to[] = null;
		InternetAddress cc[] = new InternetAddress[0];
		InternetAddress bcc[] = new InternetAddress[0];
		String subject = "no subject";
		String message = "no message";
		java.util.Properties headers = new java.util.Properties();
		String contentType = "text/plain";
		String attachments[] = new String[0];
		String from  = null;
		String username = "username";
		String password = "password";
		
		boolean debug = false;
		boolean help = false;
		
		try
		{
			for(int i = 0 ; !help && i < args.length ; i++)
			{
				if(args[i].equals("-smtp"))
				{
					smtpHost = args[++i];
				}else if(args[i].equals("-to"))
				{
					to = InternetAddress.parse(args[++i]);
				}else if(args[i].equals("-cc"))
				{
					cc = InternetAddress.parse(args[++i]);
				}else if(args[i].equals("-bcc"))
				{
					bcc = InternetAddress.parse(args[++i]);
				}else if(args[i].equals("-subject"))
				{
					subject = args[++i];
				}
				else if(args[i].equals("-messagefromfile"))
				{
					String filename = args[++i];
						java.io.InputStream input = new java.io.FileInputStream(filename);
						int length = (int)input.available();
						byte bytes[] = new byte[length];
						int n = input.read(bytes);
						message = new String(bytes,0,n);
						input.close();
				}
				else if(args[i].equals("-message"))
				{
					message = args[++i];
				}
				else if(args[i].equals("-header"))
				{
					String parts[] = StringUtil.split(args[++i],';');
					for(int j = 0 ; j < parts.length ; j ++)
					{
						String values[] = StringUtil.split(parts[j],'=');
						headers.setProperty(values[0],values[1]);
					}
				}
				else if(args[i].equals("-contenttype"))
				{
					contentType = args[++i];				
				}else if(args[i].equals("-attachment"))
				{
					attachments = StringUtil.split(args[++i],';');
				}else if(args[i].equals("-from"))
				{
					from = args[++i];
				}else if(args[i].equals("-username"))
				{
					username = args[++i];
				}else if(args[i].equals("-password"))
				{
					password = args[++i];
				}else if(args[i].equals("-debug"))
				{
					debug = true;
				}
				else 
				{
					help = true;
				}
		}
		}catch(Exception e)
		{
			System.out.println("parse parameters error: " + e.toString());
			help = true;
		}
		
		if(smtpHost == null || from == null || to == null)
		{
			help = true;
		}
		
		if(help == true)
		{
			if(System.getProperty("os.name").toLowerCase().indexOf("window") > -1)
				System.out.println("Usage: java -cp .\\airinbox-tools.jar;d:\\lib\\mail.jar;d:\\lib\\smtp.jar;d:\\lib\\pop3.jar;d:\\lib\\activation.jar com.airinbox.util.JavaMailUtils" 
								   +" <-smtp smtpHost> <-from fromAddress> <-to toAddress[;toAddess]> [-cc ccAddress[;ccAddress]] [-bcc ccAddress[;ccAddress]] [-subject subject] [-message message] [-header key=value[;key=value]] [-contenttype type]"
								   +" [-attachment filename[;filename]] [-username username] [-password password] [-debug].");
			else
				System.out.println("Usage: java -cp ./airinbox-tools.jar:/home/httpd/lib/mail.jar:/home/httpd/lib/smtp.jar:/home/httpd/lib/pop3.jar:/home/httpd/lib/activation.jar com.airinbox.util.JavaMailUtils" 
								   +" <-smtp smtpHost> <-from fromAddress> <-to toAddress[;toAddess]> [-cc ccAddress[;ccAddress]] [-bcc ccAddress[;ccAddress]] [-subject subject] [-message message] [-header key=value[;key=value]] [-contenttype type]"
								   +" [-attachment filename[;filename]] [-username username] [-password password] [-debug].");
			
			return;
		}
		
		JavaMailUtils.sendMail(smtpHost,to,cc,bcc,subject,message,headers,contentType,attachments,from,username,password,debug);

	}

	
   /**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param attachments, the filename array
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    * @param debug , if you want to see detail sending mail, set this value is true and see the System.out
    */
	public static void sendMail(String smtpHost,
							   InternetAddress to[],
							   InternetAddress cc[],
							   InternetAddress bcc[],
							   String subject,
							   String message,
							   java.util.Properties headers,
							   String contentType,
							   String []attachments,
							   String from,
							   String username,
							   String password,
							   boolean debug) throws MessagingException
    {
   	    //Set the host smtp address
	    java.util.Properties props = new java.util.Properties();
	    props.setProperty("mail.transport.protocol", "smtp");
	    props.setProperty("mail.host", smtpHost);

		if(username != null){
			props.put("mail.smtp.auth", "true"); 
			props.setProperty("mail.user", username);
		}
		if(password != null)
			props.setProperty("mail.password", password);

	    // create some properties and get the default Session
	    Session session = Session.getInstance(props, null);
	    session.setDebug(debug);
	    
	    // create a message
	    Message msg = new MimeMessage(session);
		if( headers == null)
			headers = new java.util.Properties();
		
		java.util.Iterator it = headers.entrySet().iterator();
		while(it.hasNext())
		{
			java.util.Map.Entry me = (java.util.Map.Entry)it.next();
			msg.setHeader((String)me.getKey(),(String)me.getValue());
		}
	
	    // set the from and to address
	    InternetAddress addressFrom = new InternetAddress(from);
	    msg.setFrom(addressFrom);
	
	    msg.setRecipients(Message.RecipientType.TO, to);
	    msg.setRecipients(Message.RecipientType.CC, cc);
	    msg.setRecipients(Message.RecipientType.BCC, bcc);
	
	    // Optional : You can also set your custom headers in the Email if you Want
	    //msg.addHeader("MyHeaderName", "myHeaderValue");
	
	    // Setting the Subject and Content Type
	    msg.setSubject(subject);
	    
		Multipart mp = new MimeMultipart("related");
		
		
	    MimeBodyPart textPart = new MimeBodyPart();
		if(message != null)
		{
			textPart.setContent(message, contentType);
			String id = headers.getProperty("Content-ID","");
			if( "" == id)
			{
				id = headers.getProperty("start","");
				if( "" != id)
					textPart.addHeader("Content-ID",id);
			}
			mp.addBodyPart(textPart);
		}
	    
	    for(int i = 0 ; attachments != null && i < attachments.length ; i ++)
	    {
	        MimeBodyPart attachFilePart = new MimeBodyPart();
	        FileDataSource fds = new FileDataSource(attachments[i]);
	        attachFilePart.setDataHandler(new DataHandler(fds));
	        attachFilePart.setFileName(fds.getName());
			attachFilePart.addHeader("Content-Location",fds.getName());
	        mp.addBodyPart(attachFilePart);
	    }	
		
		
	    
	    msg.setContent(mp);
 	    
		if(debug)
		{
			try
			{
				msg.writeTo(System.err);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if(username != null){
			msg.saveChanges(); 
			Transport transport = session.getTransport("smtp"); 
			transport.connect(smtpHost, username, password); 
			transport.sendMessage(msg, msg.getAllRecipients()); 
			transport.close(); 
		}else{
		
			Transport.send(msg);
		}
    }
	
	   /**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param attachments, the filename array
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    * @param debug , if you want to see detail sending mail, set this value is true and see the System.out
    */
	public static void sendMail(String smtpHost,
							   InternetAddress to[],
							   InternetAddress cc[],
							   InternetAddress bcc[],
							   String subject,
							   String message,
							   java.util.Properties headers,
							   String contentType,
							   javax.activation.DataSource  []attachments,
							   String from,
							   String username,
							   String password,
							   boolean debug) throws MessagingException
    {
   	    //Set the host smtp address
	    java.util.Properties props = new java.util.Properties();
	    props.setProperty("mail.transport.protocol", "smtp");
	    props.setProperty("mail.host", smtpHost);
		if(username != null){
			props.put("mail.smtp.auth", "true"); 
			props.setProperty("mail.user", username);
		}
		if(password != null)
			props.setProperty("mail.password", password);

	    // create some properties and get the default Session
	    Session session = Session.getInstance(props, null);
	    session.setDebug(debug);
	    
	    // create a message
	    Message msg = new MimeMessage(session);
		if( headers == null)
			headers = new java.util.Properties();
		
		java.util.Iterator it = headers.entrySet().iterator();
		while(it.hasNext())
		{
			java.util.Map.Entry me = (java.util.Map.Entry)it.next();
			msg.setHeader((String)me.getKey(),(String)me.getValue());
		}
	
	    // set the from and to address
	    InternetAddress addressFrom = new InternetAddress(from);
	    msg.setFrom(addressFrom);
	
	    msg.setRecipients(Message.RecipientType.TO, to);
	    msg.setRecipients(Message.RecipientType.CC, cc);
	    msg.setRecipients(Message.RecipientType.BCC, bcc);
	
	    // Optional : You can also set your custom headers in the Email if you Want
	    //msg.addHeader("MyHeaderName", "myHeaderValue");
	
	    // Setting the Subject and Content Type
	    msg.setSubject(subject);
	    
		Multipart mp = new MimeMultipart();
		
		
	    MimeBodyPart textPart = new MimeBodyPart();
		if(message != null)
		{
			textPart.setContent(message, contentType);
			String id = headers.getProperty("Content-ID","");
			if( "" == id)
			{
				id = headers.getProperty("start","");
				if( "" != id)
					textPart.addHeader("Content-ID",id);
			}
			mp.addBodyPart(textPart);
		}
	    
	    for(int i = 0 ; attachments != null && i < attachments.length ; i ++)
	    {
	        MimeBodyPart attachFilePart = new MimeBodyPart();
	        
	        attachFilePart.setDataHandler(new DataHandler(attachments[i]));
	        attachFilePart.setFileName(attachments[i].getName());
			attachFilePart.addHeader("Content-Location",attachments[i].getName());
	        mp.addBodyPart(attachFilePart);
	    }	
	    
	    msg.setContent(mp);
 	    
		if(debug)
		{
			try
			{
				msg.writeTo(System.err);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(username != null){
			msg.saveChanges(); 
			Transport transport = session.getTransport("smtp"); 
			transport.connect(smtpHost, username, password); 
			transport.sendMessage(msg, msg.getAllRecipients()); 
			transport.close(); 
		}else{
		
			Transport.send(msg);
		}
    }


	   /**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param attachments, the filename array
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    * @param debug , if you want to see detail sending mail, set this value is true and see the System.out
    */
	public static void sendMail(String smtpHost,
							   InternetAddress to[],
							   InternetAddress cc[],
							   InternetAddress bcc[],
							   String subject,
							   String message,
							   String contentType,
							   String []attachments,
							   String from,
							   String username,
							   String password,
							   boolean debug) throws MessagingException
    {
		sendMail(smtpHost,to,cc,bcc,subject,message,new java.util.Properties(),contentType,attachments,from,username,password,debug);
	}
	
	/**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    */
	public static void sendMail(String smtpHost,
							   InternetAddress to[],
							   InternetAddress cc[],
							   InternetAddress bcc[],
							   String subject,
							   String message,
							   String contentType,
							   String from,
							   String username,
							   String password
							   ) throws MessagingException
    {
		sendMail(smtpHost,to,cc,bcc,subject,message,contentType,null,from,username,password,false);
	}
	
   /**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    */
	public static void sendMail(String smtpHost,
							   InternetAddress to[],
							   InternetAddress cc[],
							   InternetAddress bcc[],
							   String subject,
							   String message,
							   String contentType,
							   String []attachments,
							   String from,
							   String username,
							   String password
							   ) throws MessagingException
    {
		sendMail(smtpHost,to,cc,bcc,subject,message,contentType,attachments,from,username,password,false);
	}
	
	   /**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    */
	public static void sendMail(String smtpHost,
							   InternetAddress to[],
							   InternetAddress cc[],
							   InternetAddress bcc[],
							   String subject,
							   String message,
							   String []attachments,
							   String from,
							   String username,
							   String password
							   ) throws MessagingException
    {
		sendMail(smtpHost,to,cc,bcc,subject,message,"text/plain",null,from,username,password,false);
	}

	
	/**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    */
	public static void sendMail(String smtpHost,
							   InternetAddress to[],
							   String subject,
							   String message,
							   String contentType,
							   String from,
							   String username,
							   String password
							   ) throws MessagingException
    {
		sendMail(smtpHost,to,new InternetAddress[0],new InternetAddress[0],subject,message,contentType,null,from,username,password,false);
	}
	
	
	/**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    */
	public static void sendMail(String smtpHost,
							   InternetAddress to[],
							   String subject,
							   String message,
							   String from,
							   String username,
							   String password
							   ) throws MessagingException
    {
		sendMail(smtpHost,to,new InternetAddress[0],new InternetAddress[0],subject,message,"text/plain",null,from,username,password,false);
	}
	
	
	/**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    */
	public static void sendMail(String smtpHost,
							   InternetAddress to[],
							   String subject,
							   String message,
							   String from
							   ) throws MessagingException
    {
		sendMail(smtpHost,to,new InternetAddress[0],new InternetAddress[0],subject,message,"text/plain",null,from,"username","password",false);
	}
	
  /**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    */
	public static void sendMail(String smtpHost,
							   String to,
							   String subject,
							   String message,
							   String from
							   ) throws MessagingException
    {
		sendMail(smtpHost,InternetAddress.parse(to),new InternetAddress[0],new InternetAddress[0],subject,message,"text/plain",null,from,"username","password",false);
	}

	
  /**
    * semd a mail with the username and password
    * 
    * @param stmpHost ,the mail server 
    * @param to, send to these email
    * @param cc, cc to these email
    * @param subject, the subject of this mail
    * @param message, the messge of this mail ]
    * @param contentType, the content type for this message content
    * @param from ,the semder
    * @param username , the user name for sender
    * @param password,  the user password for sender
    */
	public static void sendMail(String smtpHost,
							   String to,
							   String cc,
							   String bcc,
							   String subject,
							   String message,
							   String from
							   ) throws MessagingException
    {
		sendMail(smtpHost,InternetAddress.parse(to) ,InternetAddress.parse(cc),InternetAddress.parse(bcc),subject,message,"text/plain",null,from,"username","password",false);
	}
   
   
}
