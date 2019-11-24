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
import javax.swing.border.EmptyBorder ;
import javax.swing.ImageIcon ;

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

public class Edit extends JFrame implements ActionListener {
	private static final long serialVersionUID = 4L ;
	Container c ;

	JLabel usernameLabel, passwordLabel, cpasswordLabel, uuidLabel, classLabel, sectionLabel,
			rollLabel, genderLabel, eventLabel, contactLabel, emailLabel, warnings, background ;

	JTextField username, password, cpassword, uuid, section, roll, gender, contact, email ;
	JComboBox event, classN ;
	JRadioButton male, female ;

	JButton cancel, submit ;
	boolean submittable = true ;

	Edit(String... args) {
		try {
			setIconImage(ImageIO.read(new File("img/pencil.png"))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot set icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		String uid = args[0], nm = args[1], pss = args[2], cln = args[3], sect = args[4], rol = args[5],  gend = args[6],
						eve = args[7], cnum = args[8], em = args[9] ;

		int totalWidth = 410, totalHeight = 360 ;

		setTitle("Update") ;
		setResizable(false) ;
		setBounds(640, 100, 900, 600) ;
		setSize(totalWidth, totalHeight) ;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;

		String cwd = System.getProperty("user.dir") ;
		String eventFile = cwd + "/../events.reg", classFile = cwd + "/../standards.reg" ;
		File evF = new File(eventFile) ;
		File clF = new File(classFile) ;
		String[] events = {"Unreadable events.reg"}, classes = {"Unreadable standards.reg"} ;

		if(evF.exists()) {
			BufferedReader reader ;
			String line, data = "" ;

			try {
				reader = new BufferedReader(new FileReader(evF)) ;
				while((line = reader.readLine()) != null) data += line + "\n" ;
				events = data.split("\n") ;
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
				classes = data.split("\n") ;
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

		//
		background = new JLabel() ;

		try {
			background.setIcon(new ImageIcon(ImageIO.read(new File("img/material.jpg")))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot load warning icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		background.setLocation(0, 0) ;
		background.setSize(totalWidth, totalHeight) ;

		// Labels
		int height = 5, inc = 25 ;

		//
		uuidLabel = new JLabel("::Your Unique ID:") ;
		uuidLabel.setForeground(Color.decode("#ffffff")) ;
		uuidLabel.setSize(200, 20) ;
		uuidLabel.setLocation(5, height) ;

		try {
			uuidLabel.setIcon(new ImageIcon(ImageIO.read(new File("img/id-card.png")))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot load warning icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		uuid = new JTextField(uid) ;
		uuid.setEditable(false) ;
		uuid.setSize(200, 20) ;
		uuid.setLocation(205, height) ;

		c.add(uuidLabel) ;
		c.add(uuid) ;

		height += inc ;

		//
		usernameLabel = new JLabel("Student Name:") ;
		usernameLabel.setForeground(Color.decode("#ffffff")) ;
		usernameLabel.setSize(200, 20) ;
		usernameLabel.setLocation(5, height) ;

		username = new JTextField(nm) ;
		username.setSize(200, 20) ;
		username.setLocation(205, height) ;

		c.add(usernameLabel) ;
		c.add(username) ;

		height += inc ;

		//
		passwordLabel = new JLabel("New Password:") ;
		passwordLabel.setForeground(Color.decode("#ffffff")) ;
		passwordLabel.setSize(200, 20) ;
		passwordLabel.setLocation(5, height) ;

		password = new JPasswordField(1000) ;
		password.setText(pss) ;
		password.setSize(200, 20) ;
		password.setLocation(205, height) ;

		c.add(passwordLabel) ;
		c.add(password) ;

		height += inc ;

		//
		cpasswordLabel = new JLabel("Confirm Password:") ;
		cpasswordLabel.setForeground(Color.decode("#ffffff")) ;
		cpasswordLabel.setSize(200, 20) ;
		cpasswordLabel.setLocation(5, height) ;

		cpassword = new JPasswordField(1000) ;
		cpassword.setSize(200, 20) ;
		cpassword.setLocation(205, height) ;

		c.add(cpasswordLabel) ;
		c.add(cpassword) ;

		height += inc ;

		//
		classLabel = new JLabel("Class:") ;
		classLabel.setForeground(Color.decode("#ffffff")) ;
		classLabel.setSize(200, 20) ;
		classLabel.setLocation(5, height) ;

		classN = new JComboBox(classes) ;
		classN.setSelectedItem(cln) ;
		classN.setSize(200, 20) ;
		classN.setLocation(205, height) ;

		c.add(classLabel) ;
		c.add(classN) ;

		height += inc ;

		//
		sectionLabel = new JLabel("Section:") ;
		sectionLabel.setForeground(Color.decode("#ffffff")) ;
		sectionLabel.setSize(200, 20) ;
		sectionLabel.setLocation(5, height) ;

		section = new JTextField(sect) ;
		section.setSize(200, 20) ;
		section.setLocation(205, height) ;

		c.add(sectionLabel) ;
		c.add(section) ;

		height += inc ;

		//
		rollLabel = new JLabel("Roll:") ;
		rollLabel.setForeground(Color.decode("#ffffff")) ;
		rollLabel.setSize(200, 20) ;
		rollLabel.setLocation(5, height) ;

		roll = new JTextField(rol) ;
		roll.setSize(200, 20) ;
		roll.setLocation(205, height) ;

		c.add(rollLabel) ;
		c.add(roll) ;

		height += inc ;

		//
		genderLabel = new JLabel("Gender: ") ;
		genderLabel.setForeground(Color.decode("#ffffff")) ;
		genderLabel.setSize(200, 20) ;
		genderLabel.setLocation(5, height) ;

		male = new JRadioButton("Male") ;
		male.setForeground(Color.decode("#ffffff")) ;
		male.setBackground(new Color(0x222222)) ;
		male.setSize(100, 20) ;
		male.setLocation(205, height) ;

		female = new JRadioButton("Female") ;
		female.setForeground(Color.decode("#ffffff")) ;
		female.setBackground(new Color(0x222222)) ;
		female.setSelected(false) ;
		female.setSize(100, 20) ;
		female.setLocation(305, height) ;

		if (gend.equals("male"))
			male.setSelected(true) ;
		else
			female.setSelected(true) ;

		ButtonGroup genderGroup = new ButtonGroup() ;
		genderGroup.add(male) ;
		genderGroup.add(female) ;

		c.add(genderLabel) ;
		c.add(male) ;
		c.add(female) ;

		height += inc ;

		//
		eventLabel = new JLabel("Event:") ;
		eventLabel.setForeground(Color.decode("#ffffff")) ;
		eventLabel.setSize(200, 20) ;
		eventLabel.setLocation(5, height) ;

		event = new JComboBox(events) ;
		event.setSelectedItem(eve) ;
		event.setSize(200, 20) ;
		event.setLocation(205, height) ;

		c.add(eventLabel) ;
		c.add(event) ;

		height += inc ;

		//
		contactLabel = new JLabel("Contact Number:") ;
		contactLabel.setForeground(Color.decode("#ffffff")) ;
		contactLabel.setSize(200, 20) ;
		contactLabel.setLocation(5, height) ;

		contact = new JTextField(cnum) ;
		contact.setSize(200, 20) ;
		contact.setLocation(205, height) ;

		c.add(contactLabel) ;
		c.add(contact) ;

		height += inc ;

		//
		emailLabel = new JLabel("Email:") ;
		emailLabel.setForeground(Color.decode("#ffffff")) ;
		emailLabel.setSize(200, 20) ;
		emailLabel.setLocation(5, height) ;

		email = new JTextField(em) ;
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

			else if (!pass.equals(pss) && (!pswd.matcher(pass).find() || !pswd.matcher(cpass).find()))
				warnings.setText("Password should contain a digit, number, 6 chars") ;

			else if((!pass.equals(pss) || !cpass.isEmpty()) &&  !pass.equals(cpass))
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
					updateStudent(uid, u, Crypt.crypt(pass, "$6$salt"), cl, s, r, g, ev, c, e) ;

				else if (submitCount >= 3) {
					dispose() ;
					System.exit(0) ;
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
		warnings = new JLabel("Welcome, here you can Update your Info...") ;

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
		warnings.setForeground(Color.decode("#ffffff")) ;
		warnings.setLocation(5, height - 90) ;
		c.add(warnings) ;

		c.add(background) ;
		setVisible(true) ;
	}

	public void updateStudent(String... args) {
		String uid = args[0], u = args[1], p = args[2], cl = args[3], s = args[4], r = args[5], g = args[6] ;
		String ev = args[7], c = args[8], e = args[9] ;

		Connection con = null ;
		Statement stmt = null ;

		try {
			con = DriverManager.getConnection("jdbc:sqlite:info.db") ;
			con.setAutoCommit(false) ;
			System.out.println("\033[38;5;151mOpened database successfully.\033[0m") ;

			stmt = con.createStatement() ;

			stmt.executeUpdate(
				String.format(
					"UPDATE Participants SET name = '%s', password = '%s', class = '%s', section = '%s', roll = '%s', gender = '%s', event = '%s', phone = '%s', email = '%s' WHERE id = %s",
					u, p, cl, s, r, g, ev, c, e, uid
				)
			) ;

			stmt.close() ;
			con.commit() ;
			con.close() ;

			warnings.setText("Updated Entries Successfully!") ;

			try {
				warnings.setIcon(new ImageIcon(ImageIO.read(new File("img/checked.png")))) ;
			}

			catch(Exception exc) {
				System.out.println(
					"\033[1;33m::Cannot load warning icon: \033[0m" +
					"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
				) ;
			}

			submit.setVisible(false) ;
		}

		catch(Exception exc) {
			System.out.println( "\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m" ) ;
			System.exit(0) ;
		}
	}

	public static void main(String... args) {
		if (args.length != 2) {
			System.out.println(
				"\033[1;31mSorry this app isn't intended for directly running by the user.\033[0m\n" +
				"\033[1;34mPlease Run the Login app.\033[0m"
			) ;

			System.exit(1) ;
		}

		String uid = args[0].trim(), pss = args[1].trim() ;
		Connection con = null ;
		Statement stmt = null ;

		try {
			Class.forName("org.sqlite.JDBC") ;
			con = DriverManager.getConnection("jdbc:sqlite:info.db") ;
			con.setAutoCommit(false) ;
			System.out.println("\033[38;5;151mOpened database successfully.\033[0m") ;

			stmt = con.createStatement() ;
			ResultSet rs = stmt.executeQuery("SELECT * FROM Participants WHERE id=" + uid + ";") ;

			if (rs.getString("password").equals(Crypt.crypt(pss, "$6$salt"))) {
				new Edit(
					uid, rs.getString("name"), pss, rs.getString("class"), rs.getString("section"), rs.getString("roll"), rs.getString("gender"),
					rs.getString("event"), rs.getString("phone"), rs.getString("email")
				) ;
			}

			else {
				System.out.println(
					"\033[1;33mSorry this app isn't intended for directly running by the user.\033[0m.\n" +
					"\033[1;36mPlease Run the Login app.\033[0m"
				) ;

				System.exit(2) ;
			}

			stmt.close() ;
			con.close() ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;31mSorry, the id \"" + uid + "\" is invalid.\033[0m.\n" +
				"\033[1;32m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m\n" +
				"\033[1;35mPlease Run the Login app first.\033[0m"
			) ;

			System.exit(127) ;
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String user = username.getText() ;
		String pass = password.getText() ;
	}
}
