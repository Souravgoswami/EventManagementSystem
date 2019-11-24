import java.awt.BorderLayout ;
import java.awt.GridLayout ;
import java.awt.Color ;
import java.awt.Container ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.WindowEvent ;
import java.awt.event.MouseAdapter ;
import java.awt.event.MouseEvent ;

import javax.swing.JButton ;
import javax.swing.JFrame ;
import javax.swing.JLabel ;
import javax.swing.JPasswordField ;
import javax.swing.JTextField ;
import javax.swing.AbstractAction ;
import javax.swing.UIManager ;
import javax.imageio.ImageIO ;
import javax.swing.ImageIcon ;

import java.sql.Connection ;
import java.sql.Statement ;
import java.sql.DriverManager ;
import java.sql.ResultSet ;

import java.io.File ;
import java.io.IOException ;

import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import org.apache.commons.codec.digest.Crypt ;

public class Login extends JFrame implements ActionListener {
	private static final long serialVersionUID = 4L ;

	int totalWidth = 340, totalHeight = 135 ;
	int padding = 5, cw = 200, ch = 20, bcw = 100, bch = 25 ;
	Container c ;

	JLabel usernameLabel, passwordLabel, warnings, background ;
	JTextField username, password ;
	JButton login, cancel, signup ;

	Login() {
		setTitle("Log in | Sign up") ;
		setSize(totalWidth, totalHeight) ;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;

		try {
			setIconImage(ImageIO.read(new File("img/key.png"))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot set icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		c = getContentPane() ;
		c.setLayout(null) ;
		c.setBackground(Color.decode("#222222")) ;

		setResizable(false) ;
		warnings = new JLabel("Hi, would you like to Register for an Event?") ;

		int height = padding, inc = 25 ;

		try {
			warnings.setIcon(new ImageIcon(ImageIO.read(new File("img/alarm.png")))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot load warning icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		//
		background = new JLabel() ;

		try {
			background.setIcon(new ImageIcon(ImageIO.read(new File("img/material0.jpg")))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot load warning icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		background.setLocation(0, 0) ;
		background.setSize(totalWidth, totalHeight) ;

		// User Label
		usernameLabel = new JLabel("UUID:") ;
		usernameLabel.setForeground(new Color(0xffffff)) ;
		usernameLabel.setLocation(padding, height) ;
		usernameLabel.setSize(cw, ch) ;

		try {
			usernameLabel.setIcon(new ImageIcon(ImageIO.read(new File("img/id-card.png")))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot set icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}

		username = new JTextField() ;
		username.setForeground(new Color(0)) ;
		username.setLocation(padding * 2 + 120, height) ;
		username.setSize(cw, ch) ;

		c.add(usernameLabel) ;
		c.add(username) ;

		height += inc ;

		// Password
		passwordLabel = new JLabel("Password:") ;
		passwordLabel.setForeground(new Color(0xffffff)) ;
		passwordLabel.setSize(cw, ch) ;
		passwordLabel.setLocation(padding, height) ;

		try {
			passwordLabel.setIcon(new ImageIcon(ImageIO.read(new File("img/password.png")))) ;
		}

		catch(Exception exc) {
			System.out.println(
				"\033[1;33m::Cannot set icon: \033[0m" +
				"\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m"
			) ;
		}


		password = new JPasswordField(1000) ;
		password.setForeground(new Color(0)) ;
		password.setSize(cw, ch) ;
		password.setLocation(padding * 2 + 120, height) ;

		c.add(passwordLabel) ;
		c.add(password) ;

		height += inc ;

		warnings.setForeground(new Color(0xffffff)) ;
		warnings.setSize(totalWidth, ch) ;
		warnings.setLocation(padding, height) ;
		c.add(warnings) ;

		height += inc ;

		// Buttons
		login = new JButton(new AbstractAction("Login") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Pattern pswdMatch = Pattern.compile("((?=.*[0-9])(?=.*[a-zA-Z]).{6,})") ;
				Pattern uuidMatch = Pattern.compile("[^0-9]") ;

				String paswd = password.getText() ;
				String u = username.getText(), p = Crypt.crypt(paswd, "$6$salt") ;

				if(u.isEmpty() && paswd.isEmpty())
					warnings.setText("Please Provide a UUID and a Password") ;

				else if(u.isEmpty())
					warnings.setText("Please Type your UUID") ;

				else if(uuidMatch.matcher(u).find())
					warnings.setText("UUID Should Contain Digits Only") ;

				else if(paswd.isEmpty())
					warnings.setText("Please Provide your Password") ;

				else if(!pswdMatch.matcher(paswd).find())
					warnings.setText("Password doesn't Match") ;

				else
					loginDB(u, p) ;
			}
		}) ;

		login.setBackground(new Color(0xFAC536)) ;
		login.setForeground(new Color(0xffffff)) ;
		login.setSize(bcw, bch) ;
		login.setLocation(padding * 2, height) ;
		c.add(login) ;

		signup = new JButton(new AbstractAction("Sign Up") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					Runtime.getRuntime().exec("java -cp commons-codec-1.13.jar:sqlite-jdbc-3.27.2.1.jar:. Register") ;
					dispose() ;
				}

				catch(Exception exc) {
					System.out.println( "\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m" ) ;
					System.exit(0) ;
				}
			}
		}) ;

		signup.addActionListener(this) ;
		signup.setForeground(new Color(0xffffff)) ;
		signup.setSize(bcw, bch) ;
		signup.setLocation(padding * 4 + bcw, height) ;
		signup.setBackground(new Color(0x736FFF)) ;
		signup.setForeground(new Color(0xffffff)) ;
		c.add(signup) ;

		cancel = new JButton(new AbstractAction("Close") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose() ;
				System.exit(0) ;
			}
		}) ;

		cancel.addActionListener(this) ;
		cancel.setBackground(new Color(0xff5555)) ;
		cancel.setForeground(new Color(0xffffff)) ;
		cancel.setForeground(new Color(0xffffff)) ;
		cancel.setSize(bcw, bch) ;
		cancel.setLocation(padding * 6 + bcw + bcw, height) ;
		c.add(cancel) ;

		JButton ary[] = {login, signup, cancel} ;
		int colours[] = {0xFAC536, 0x736FFF, 0xff5555} ;

		for(int i = 0 ; i < 3 ; ++i) {
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

		c.add(background) ;
		setVisible(true) ;
	}

	public void loginDB(String... args) {
		Connection con = null ;
		Statement stmt = null ;
		String uid = args[0], pass = args[1] ;

		try {
			con = DriverManager.getConnection("jdbc:sqlite:info.db") ;
			con.setAutoCommit(false) ;
			System.out.println("\033[38;5;151mOpened database successfully.\033[0m") ;

			stmt = con.createStatement() ;

			ResultSet rs = stmt.executeQuery(
				"SELECT * FROM Participants WHERE id=" + uid + ";"
			) ;

			if (rs.next()) {
				if (rs.getString("password").equals(pass)) {
					Runtime.getRuntime().exec(String.format(
						"java -cp commons-codec-1.13.jar:sqlite-jdbc-3.27.2.1.jar:. Edit %s %s",
							username.getText(), password.getText())
					) ;
					dispose() ;
				}

				else
					warnings.setText("Invalid Password") ;
			}

			else
				warnings.setText("Invalid UUID") ;

			stmt.close() ;
			con.close() ;
		}

		catch(Exception exc) {
			System.out.println( "\033[1;31m" + exc.getClass().getName() + ": " + exc.getMessage() + "\033[0m" ) ;
			System.exit(0) ;
		}
	}

	public static void main(String[] args) {
		new Login() ;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String user = username.getText().trim() ;
		String pass = password.getText() ;
	}
}
