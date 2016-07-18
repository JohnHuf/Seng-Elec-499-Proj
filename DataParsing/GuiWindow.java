
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class GuiWindow {



	private JFrame frmBoxingMetricsDemo;
	private JTextField txtpnPunchCount;
	private JTextField txtpnPunchSpeed;
	private JTextField txtpnPunchAccel;
	private JTextField txtpnClassifier;

	public JFrame getFrmBoxingMetricsDemo() {
		return frmBoxingMetricsDemo;
	}

	public void setFrmBoxingMetricsDemo(JFrame frmBoxingMetricsDemo) {
		this.frmBoxingMetricsDemo = frmBoxingMetricsDemo;
	}
	/**
	 * Launch the application.
	 */


	/**
	 * Create the application.
	 */
	public GuiWindow() {
		initialize();
	}
	
	/**
	 * Some Setter Methods to handle GUI boxes
	 */
	public void setClass(String punchClass){
		txtpnClassifier.setText(punchClass);
	}
	
	public void setCount(int count){
		txtpnPunchCount.setText(""+count);
	}
	
	public void setSpeed(double speed){
		txtpnPunchSpeed.setText(new DecimalFormat("#0.00").format(speed) + " m/s");
	}
	
	public void setAccel(double accel){
		txtpnPunchAccel.setText(new DecimalFormat("#0.00").format(accel) + " G");
	}
	
	public void setBgRed(){
		frmBoxingMetricsDemo.getContentPane().setBackground(new Color(153, 0, 0));
	}
	
	public void setBgGreen(){
		frmBoxingMetricsDemo.getContentPane().setBackground(new Color(0, 153, 0));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBoxingMetricsDemo = new JFrame();
		frmBoxingMetricsDemo.setResizable(false);
		frmBoxingMetricsDemo.getContentPane().setBackground(new Color(153, 0, 0));
		frmBoxingMetricsDemo.setTitle("Boxing Metrics Demo Window");
		frmBoxingMetricsDemo.setBounds(0, 0, 1080, 920);
		frmBoxingMetricsDemo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBoxingMetricsDemo.getContentPane().setLayout(null);
		
		txtpnClassifier = new JTextField();
		txtpnClassifier.setEditable(false);
		txtpnClassifier.setHorizontalAlignment(SwingConstants.CENTER);
		txtpnClassifier.setFont(new Font("Arial Black", Font.PLAIN, 75));
		txtpnClassifier.setText("Uppercut");
		txtpnClassifier.setBounds(12, 370, 494, 113);
		frmBoxingMetricsDemo.getContentPane().add(txtpnClassifier);
		
		JLabel lblPunchClass = new JLabel("Punch Class");
		lblPunchClass.setHorizontalAlignment(SwingConstants.CENTER);
		lblPunchClass.setForeground(new Color(255, 255, 255));
		lblPunchClass.setFont(new Font("Arial Black", Font.PLAIN, 50));
		lblPunchClass.setBounds(12, 299, 494, 71);
		frmBoxingMetricsDemo.getContentPane().add(lblPunchClass);
		
		txtpnPunchCount = new JTextField();
		txtpnPunchCount.setText("No Punch");
		txtpnPunchCount.setHorizontalAlignment(SwingConstants.CENTER);
		txtpnPunchCount.setFont(new Font("Arial Black", Font.PLAIN, 75));
		txtpnPunchCount.setEditable(false);
		txtpnPunchCount.setBounds(12, 614, 494, 113);
		frmBoxingMetricsDemo.getContentPane().add(txtpnPunchCount);
		
		JLabel lblPunchCount = new JLabel("Punch Count");
		lblPunchCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblPunchCount.setForeground(Color.WHITE);
		lblPunchCount.setFont(new Font("Arial Black", Font.PLAIN, 50));
		lblPunchCount.setBounds(12, 543, 494, 71);
		frmBoxingMetricsDemo.getContentPane().add(lblPunchCount);
		
		txtpnPunchSpeed = new JTextField();
		txtpnPunchSpeed.setText("0 m/s");
		txtpnPunchSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		txtpnPunchSpeed.setFont(new Font("Arial Black", Font.PLAIN, 75));
		txtpnPunchSpeed.setEditable(false);
		txtpnPunchSpeed.setBounds(568, 370, 494, 113);
		frmBoxingMetricsDemo.getContentPane().add(txtpnPunchSpeed);
		
		JLabel lblPunchSpeed = new JLabel("Punch Speed");
		lblPunchSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		lblPunchSpeed.setForeground(Color.WHITE);
		lblPunchSpeed.setFont(new Font("Arial Black", Font.PLAIN, 50));
		lblPunchSpeed.setBounds(568, 299, 494, 71);
		frmBoxingMetricsDemo.getContentPane().add(lblPunchSpeed);
		
		txtpnPunchAccel = new JTextField();
		txtpnPunchAccel.setText("0 G");
		txtpnPunchAccel.setHorizontalAlignment(SwingConstants.CENTER);
		txtpnPunchAccel.setFont(new Font("Arial Black", Font.PLAIN, 75));
		txtpnPunchAccel.setEditable(false);
		txtpnPunchAccel.setBounds(568, 614, 494, 113);
		frmBoxingMetricsDemo.getContentPane().add(txtpnPunchAccel);
		
		JLabel lblPunchAccel = new JLabel("Punch Max Accel");
		lblPunchAccel.setHorizontalAlignment(SwingConstants.CENTER);
		lblPunchAccel.setForeground(Color.WHITE);
		lblPunchAccel.setFont(new Font("Arial Black", Font.PLAIN, 50));
		lblPunchAccel.setBounds(568, 543, 494, 71);
		frmBoxingMetricsDemo.getContentPane().add(lblPunchAccel);
		
		JLabel lblTitle = new JLabel("Metrics Center");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(new Color(255, 255, 255));
		lblTitle.setFont(new Font("Arial Black", Font.PLAIN, 75));
		lblTitle.setBounds(204, 90, 670, 148);
		frmBoxingMetricsDemo.getContentPane().add(lblTitle);
	}
}
