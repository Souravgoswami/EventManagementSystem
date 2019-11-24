import java.awt.GridLayout ;
import java.awt.Color ;
import java.awt.BorderLayout ;
import java.awt.Dimension ;
import java.awt.Container ;

import java.awt.event.ActionListener ;
import java.awt.event.WindowEvent ;
import java.awt.event.MouseAdapter ;
import java.awt.event.MouseEvent ;
import java.awt.event.ActionEvent ;

import javax.swing.JButton ;
import javax.swing.JRadioButton ;
import javax.swing.JFrame ;
import javax.swing.JLabel ;
import javax.swing.JPasswordField ;
import javax.swing.JTextField ;
import javax.swing.JComboBox ;
import javax.swing.AbstractAction ;
import javax.swing.ButtonGroup ;
import javax.swing.UIManager ;
import javax.swing.ImageIcon ;

import javax.swing.border.EmptyBorder ;

import javax.imageio.ImageIO ;

import java.sql.Connection ;
import java.sql.Statement ;
import java.sql.DriverManager ;
import java.sql.ResultSet ;

import java.io.FileReader ;
import java.io.BufferedReader ;
import java.io.File ;
import java.io.IOException ;

import java.util.regex.Matcher ;
import java.util.regex.Pattern ;
import org.apache.commons.codec.digest.Crypt ;

public class Register extends JFrame implements ActionListener {
	private static final long serialVersionUID = 4L ;
	Container c ;

	JLabel usernameLabel, passwordLabel, cpasswordLabel, uuidLabel, classLabel, sectionLabel,
			rollLabel, genderLabel, eventLabel, contactLabel, emailLabel, warnings, background ;

	JTextField username, password, cpassword, uuid, section, roll, gender, contact, email ;
	JComboBox event, classN ;
	JRadioButton male, female ;

	JPasswordField passwordField, cpasswordField ;
	JButton cancel, submit ;
	boolean submittable = true ;

	Register() {
		try {
			setIconImage(ImageIO.read(new File("img/address-book.png"))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot set icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		int totalWidth = 410, totalHeight = 360 ;

		setTitle("Register") ;
		setResizable(false) ;
		setBounds(640, 100, 900, 600) ;
		setSize(totalWidth, totalHeight) ;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;

		String cwd = System.getProperty("user.dir") ;
		String eventFile = cwd + "/../events.reg", classFile = cwd + "/../standards.reg" ;
		File evF = new File(eventFile),  clF = new File(classFile) ;
		String[] events = {"Unreadable events.reg"}, classes = {"Unreadable standards.reg"} ;

		if(evF.exists()) {
			BufferedReader reader ;
			String line, data = "" ;

			try {
				reader = new BufferedReader(new FileReader(evF)) ;
				while((line = reader.readLine()) != null) data += line + "\n" ;
				events= data.split("\n") ;
			}

			catch(IOException e) {
				System.out.println("\033[1;31mError while reading events.reg!\033[0m") ;
				submittable = false ;
			}
		}

		if(clF.exists()) {
			BufferedReader reader ;
			String line, data = "" ;

			try {
				reader = new BufferedReader(new FileReader(clF)) ;
				while((line = reader.readLine()) != null) data += line + "\n" ;
				classes= data.split("\n") ;
			}

			catch(IOException e) {
				System.out.println("\033[1;31mError while reading standards.reg!\033[0m") ;
				submittable = false ;
			}
		}

		// Container
		c = getContentPane() ;
		c.setLayout(null) ;
		c.setBackground(Color.decode("#222222")) ;

		// Labels
		int height = 5, inc = 25 ;

		background = new JLabel() ;

		try {
			background.setIcon(new ImageIcon(ImageIO.read(new File("img/material2.jpg")))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot load warning icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		background.setLocation(0, 0) ;
		background.setSize(totalWidth, totalHeight) ;

		//
		uuidLabel = new JLabel(":: Your Unique ID:") ;
		uuidLabel.setForeground(new Color(0xffffff)) ;
		uuidLabel.setSize(200, 20) ;
		uuidLabel.setLocation(5, height) ;

		uuid = new JTextField("Submit to Create UUID") ;
		uuid.setEditable(false) ;
		uuid.setSize(200, 20) ;
		uuid.setLocation(205, height) ;

		try {
			uuidLabel.setIcon(new ImageIcon(ImageIO.read(new File("img/id-card.png")))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot load warning icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		c.add(uuidLabel) ;
		c.add(uuid) ;

		height += inc ;

		//
		usernameLabel = new JLabel("Student Name:") ;
		usernameLabel.setForeground(new Color(0)) ;
		usernameLabel.setForeground(new Color(0xffffff)) ;
		usernameLabel.setSize(200, 20) ;
		usernameLabel.setLocation(5, height) ;

		username = new JTextField() ;
		username.setForeground(new Color(0)) ;
		username.setSize(200, 20) ;
		username.setLocation(205, height) ;

		c.add(usernameLabel) ;
		c.add(username) ;

		height += inc ;

		//
		passwordLabel = new JLabel("Choose a Password:") ;
		passwordLabel.setForeground(new Color(0xffffff)) ;
		passwordLabel.setSize(200, 20) ;
		passwordLabel.setLocation(5, height) ;

		password = new JPasswordField(1000) ;
		password.setForeground(new Color(0)) ;
		password.setSize(200, 20) ;
		password.setLocation(205, height) ;

		c.add(passwordLabel) ;
		c.add(password) ;

		height += inc ;

		//
		cpasswordLabel = new JLabel("Confirm Password:") ;
		cpasswordLabel.setForeground(new Color(0xffffff)) ;
		cpasswordLabel.setSize(200, 20) ;
		cpasswordLabel.setLocation(5, height) ;

		cpassword = new JPasswordField(1000) ;
		cpassword.setForeground(new Color(0)) ;
		cpassword.setSize(200, 20) ;
		cpassword.setLocation(205, height) ;

		c.add(cpasswordLabel) ;
		c.add(cpassword) ;

		height += inc ;

		//
		classLabel = new JLabel("Class:") ;
		classLabel.setForeground(new Color(0xffffff)) ;
		classLabel.setSize(200, 20) ;
		classLabel.setLocation(5, height) ;

		classN = new JComboBox(classes) ;
		classN.setForeground(new Color(0)) ;
		classN.setSize(200, 20) ;
		classN.setLocation(205, height) ;

		c.add(classLabel) ;
		c.add(classN) ;

		height += inc ;

		//
		sectionLabel = new JLabel("Section:") ;
		sectionLabel.setForeground(new Color(0xffffff)) ;
		sectionLabel.setSize(200, 20) ;
		sectionLabel.setLocation(5, height) ;

		section = new JTextField() ;
		section.setForeground(new Color(0)) ;
		section.setSize(200, 20) ;
		section.setLocation(205, height) ;

		c.add(sectionLabel) ;
		c.add(section) ;

		height += inc ;

		//
		rollLabel = new JLabel("Roll:") ;
		rollLabel.setForeground(new Color(0xffffff)) ;
		rollLabel.setSize(200, 20) ;
		rollLabel.setLocation(5, height) ;

		roll = new JTextField() ;
		roll.setForeground(new Color(0)) ;
		roll.setSize(200, 20) ;
		roll.setLocation(205, height) ;

		c.add(rollLabel) ;
		c.add(roll) ;

		height += inc ;

		//
		genderLabel = new JLabel("Gender:") ;
		genderLabel.setForeground(new Color(0xffffff)) ;
		genderLabel.setSize(200, 20) ;
		genderLabel.setLocation(5, height) ;

		male = new JRadioButton("Male") ;
		male.setForeground(new Color(0xffffff)) ;
		male.setBackground(new Color(0x222222)) ;
		male.setSelected(true) ;
		male.setSize(100, 20) ;
		male.setLocation(205, height) ;

		female = new JRadioButton("Female") ;
		female.setForeground(new Color(0xffffff)) ;
		female.setBackground(new Color(0x222222)) ;
		female.setSelected(false) ;
		female.setSize(100, 20) ;
		female.setLocation(305, height) ;

		ButtonGroup genderGroup = new ButtonGroup() ;
		genderGroup.add(male) ;
		genderGroup.add(female) ;

		c.add(genderLabel) ;
		c.add(male) ;
		c.add(female) ;

		height += inc ;

		//
		eventLabel = new JLabel("Event:") ;
		eventLabel.setForeground(new Color(0xffffff)) ;
		eventLabel.setSize(200, 20) ;
		eventLabel.setLocation(5, height) ;

		event = new JComboBox(events) ;
		event.setForeground(new Color(0)) ;
		event.setSize(200, 20) ;
		event.setLocation(205, height) ;

		c.add(eventLabel) ;
		c.add(event) ;

		height += inc ;

		//
		contactLabel = new JLabel("Contact Number:") ;
		contactLabel.setForeground(new Color(0xffffff)) ;
		contactLabel.setSize(200, 20) ;
		contactLabel.setLocation(5, height) ;

		contact = new JTextField() ;
		contact.setForeground(new Color(0)) ;
		contact.setSize(200, 20) ;
		contact.setLocation(205, height) ;

		c.add(contactLabel) ;
		c.add(contact) ;

		height += inc ;

		//
		emailLabel = new JLabel("Email:") ;
		emailLabel.setForeground(new Color(0xffffff)) ;
		emailLabel.setSize(200, 20) ;
		emailLabel.setLocation(5, height) ;

		email = new JTextField() ;
		email.setForeground(new Color(0)) ;
		email.setSize(200, 20) ;
		email.setLocation(205, height) ;

		c.add(emailLabel) ;
		c.add(email) ;

		height += inc ;

		// Buttons
		submit = new JButton(new AbstractAction("Submit") {
			int submitCount = 0, submitErrorPressed = 0 ;

			@Override
			public void actionPerformed(ActionEvent ae) {
				String u = username.getText() ;
				String pass = password.getText() ;
				String cpass = cpassword.getText() ;
				String s = section.getText() ;
				String cl = classN.getSelectedItem().toString() ;
				String r = roll.getText() ;
				String g = female.isSelected() ? "female" : "male" ;
				String c = contact.getText() ;
				String e = email.getText() ;
				String ev = event.getSelectedItem().toString() ;

				Pattern pswd = Pattern.compile("((?=.*[0-9])(?=.*[a-zA-Z]).{6,})") ;
				Pattern eml = Pattern.compile(".*@.+\\..*") ;
				Pattern cnt = Pattern.compile("[^0-9\\+]") ;

				if (!submittable) {
					submit.setText("Close") ;
					warnings.setText("Submit Disabled for Some Errors :(") ;
					submitErrorPressed += 1 ;

					if (submitErrorPressed > 1) {
						dispose() ;
						System.exit(0) ;
					}
				}

				else if (u.isEmpty())
					warnings.setText("Please Enter Your Name Above...") ;

				else if (pass.isEmpty())
					warnings.setText("Type a password 6 chars long with digits and numbers") ;

				else if (!pswd.matcher(pass).find())
					warnings.setText("Password should contain a digit, number, 6 chars") ;

				else if(!pass.equals(cpass))
					warnings.setText("Passwords didn't match") ;

				else if (s.isEmpty())
					warnings.setText("What is your Section in Class " + cl + "?") ;

				else if (r.isEmpty())
					warnings.setText("What is your roll?") ;

				else if (c.isEmpty() && e.isEmpty())
					warnings.setText("Provide Either Contact or Email") ;

				else if (!eml.matcher(e).find() && !e.isEmpty())
					warnings.setText("Please Enter a Valid Email") ;

				else if (cnt.matcher(c).find() && !c.isEmpty())
					warnings.setText("Phone Number should Contain Digits Only...") ;

				else if(c.length() < 10 && !c.isEmpty())
					warnings.setText("Phone Number too Short! Please Review!") ;

				else {
					submitCount += 1 ;

					if (submitCount == 2)
						recordStudent(u, Crypt.crypt(pass, "$6$salt"), s, cl, r, g, ev, c, e) ;

					else if (submitCount > 2) {
						try {
							Runtime.getRuntime().exec("java -cp commons-codec-1.13.jar:sqlite-jdbc-3.27.2.1.jar:. Login") ;
							dispose() ;
						}

						catch(Exception exc) {
							System.out.println( "\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m" ) ;
							System.exit(0) ;
						}
					}

					else
						warnings.setText("Please Review and Press the Submit Button Again") ;
				}
			}
		}) ;

		submit.setBackground(new Color( submittable ? 0x74BB25 : 0xdddddd )) ;
		submit.setForeground(new Color(0xffffff)) ;
		submit.setPreferredSize(new Dimension(100, 40)) ;

		cancel = new JButton(new AbstractAction("Close") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose() ;
				System.exit(0) ;
			}
		}) ;

		cancel.addActionListener(this) ;
		cancel.setBackground(new Color(0xFFBA00)) ;
		cancel.setForeground(new Color(0xffffff)) ;
		cancel.setPreferredSize(new Dimension(100, 40)) ;

		JButton ary[] = {submit, cancel} ;
		int colours[] = { submittable ? 0x74BB25 : 0xdddddd , 0xFFBA00 } ;

		for(int i = 0 ; i < ary.length ; ++i) {
			JButton obj = ary[i] ;
			int colour = colours[i] ;

			obj.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent evt) {
					obj.setBackground(UIManager.getColor("control")) ;
					obj.setForeground(new Color(0)) ;
				}

				public void mouseExited(java.awt.event.MouseEvent evt) {
					obj.setBackground(new Color(colour)) ;
					obj.setForeground(new Color(0xffffff)) ;
				}
			}) ;
		}

		cancel.setSize(100, 25) ;
		cancel.setLocation(totalWidth - 105, height + inc) ;
		c.add(cancel) ;

		submit.setSize(100, 25) ;
		submit.setLocation(totalWidth - 210, height + inc) ;
		c.add(submit) ;

		//
		warnings = new JLabel("Welcome! You can Register for an Event Here...") ;
		try {
			warnings.setIcon(new ImageIcon(ImageIO.read(new File("img/alarm.png")))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot load warning icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		warnings.setSize(400, 200) ;
		warnings.setForeground(new Color(0xffffff)) ;
		warnings.setLocation(5, height - 90) ;
		c.add(warnings) ;
		c.add(background) ;

		setVisible(true) ;
	}

	public void recordStudent(String... args) {
		String u = args[0], p = args[1], s = args[2], cl = args[3], r = args[4], g = args[5] ;
		String ev = args[6], c = args[7], e = args[8] ;

		Connection con = null ;
		Statement stmt = null ;

		try {
			Class.forName("org.sqlite.JDBC") ;
			con = DriverManager.getConnection("jdbc:sqlite:info.db") ;
			con.setAutoCommit(false) ;
			System.out.println("\033[38;5;10mOpened database successfully.\033[0m") ;

			stmt = con.createStatement() ;
			stmt.executeUpdate(
				"INSERT INTO Participants(name,password,class,section,roll,gender,event,phone,email) " +
				"VALUES " + String.format("('%s','%s','%s','%s','%s','%s','%s','%s','%s');", u, p, cl, s, r, g, ev, c, e)
			) ;

			ResultSet rs = stmt.executeQuery("SELECT * FROM Participants WHERE id = (SELECT MAX(id) FROM Participants)") ;

			String id = rs.getString("id") ;
			uuid.setText(id) ;

			stmt.close() ;
			con.commit() ;
			con.close() ;

			warnings.setText("Congrats! Must Note that Your ID is " + id) ;

			try {
				warnings.setIcon(new ImageIcon(ImageIO.read(new File("img/checked.png")))) ;
			}

			catch(Exception exc) {
				System.out.println(
					"\033[1;33m::Cannot load warning icon: \033[0m" +
					"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
				) ;
			}

			submit.setText("Login") ;
		}

		catch(Exception exc) {
			System.out.println( "\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m" ) ;
			System.exit(0) ;
		}
	}

	public static void main(String[] args) {
		new Register() ;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String user = username.getText() ;
		String pass = password.getText() ;
	}
}
