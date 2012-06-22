package com.fina;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class MainPage extends JFrame {

	private Container container;

	private JTabbedPane tabs;

	private JPanel pop3Panel;
	private JTextArea pop3TextArea;
	private JButton testPOP3;

	private JPanel imapPanel;
	//some comment
	private JTextArea imapTextArea; 
	private JButton testImap;

	private JPanel smtpPanel;
	private JTextField sendTo;
	private JButton sendButton;
	private JTextArea sendTextArea;

	private String fileName;

	final Vector<String> col = new Vector<String>();

	public MainPage(String fileName) {
		this.fileName = fileName;
		initComponents();
	}

	private void initComponents() {
		this.setTitle("Test Email Configuration");
		container = this.getContentPane();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.setLayout(new BorderLayout());
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		size.height = size.height - 100;
		size.width = size.width - 100;
		this.setSize(size);
		this.setLocation(50, 50);

		tabs = new JTabbedPane();

		col.add("key");
		col.add("Value");

		// POP3 Tab
		pop3Panel = new JPanel(new BorderLayout());
		JPanel tempNorthPanel = new JPanel();
		testPOP3 = new JButton("TEST | POP3");
		testPOP3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					pop3TextArea.setText(pop3TextArea.getText() + "\n"
							+ "Pleace wait...");
					int messageCount = TestConnectionPOP3.readMails(fileName);
					pop3TextArea.setText(pop3TextArea.getText() + "\n"
							+ "Connected successfully.\n");
					pop3TextArea.setText(pop3TextArea.getText() + "\n"
							+ "Start mail Read.\n");
					pop3TextArea.setText(pop3TextArea.getText() + "\n"
							+ "Message(s) Count = " + messageCount + "\n");
					pop3TextArea.setText(pop3TextArea.getText()
							+ "\nPOP3 Read Test is OK");
					pop3TextArea.setText(pop3TextArea.getText()
							+ "\n-----------------------------------------------------------------------------------");
				} catch (Exception e1) {
					e1.printStackTrace();
					pop3TextArea.setText(pop3TextArea.getText() + "\n"
							+ e1.getMessage());
				}
			}
		});
		tempNorthPanel.add(testPOP3);
		pop3Panel.add(tempNorthPanel, BorderLayout.NORTH);

		JSplitPane popSplit = new JSplitPane();

		pop3TextArea = new JTextArea();
		pop3TextArea.setEditable(false);
		pop3TextArea.setToolTipText("Console");
		popSplit.setRightComponent(pop3TextArea);

		final JTable pop3Table = new JTable(getCurrentRows("pop3"), col);
		pop3Table.setModel(new PropertiesTableModel());
		DefaultTableModel pop3DefaultTableModel = (DefaultTableModel) pop3Table
				.getModel();
		for (Vector<String> v : getCurrentRows("pop3")) {
			pop3DefaultTableModel.addRow(v);
		}
		JPanel popRPanel = new JPanel(new BorderLayout());
		popRPanel.add(new JScrollPane(pop3Table), BorderLayout.CENTER);
		popSplit.setLeftComponent(popRPanel);
		JButton popSaveButton = new JButton("Save POP3 Properties");
		popSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveProperties(pop3Table);
			}
		});
		JPanel popSaveTmpPanel = new JPanel();
		popSaveTmpPanel.add(popSaveButton);
		popRPanel.add(popSaveTmpPanel, BorderLayout.SOUTH);

		pop3Panel.add(popSplit, BorderLayout.CENTER);

		tabs.addTab("POP", pop3Panel);

		// IAMP Tab
		imapPanel = new JPanel(new BorderLayout());

		JPanel tempNorthPanelIMAP = new JPanel();
		testImap = new JButton("TEST | IMAP");
		testImap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					imapTextArea.setText(imapTextArea.getText()
							+ "\nPleace Wait...\n");
					int messagesCount = TestConnectionIMAP.readMails(fileName);
					imapTextArea.setText(imapTextArea.getText()
							+ "\nConnected successfully.\n");
					imapTextArea.setText(imapTextArea.getText() + "\n"
							+ "Start mail Read.\n");
					imapTextArea.setText(imapTextArea.getText() + "\n"
							+ "Message(s) Count = " + messagesCount + "\n");
					imapTextArea.setText(imapTextArea.getText()
							+ "\nIMAP Read Test is OK!");
					imapTextArea.setText(imapTextArea.getText()
							+ "\n-----------------------------------------------------------------------------------");
				} catch (Exception e1) {
					e1.printStackTrace();
					imapTextArea.setText(imapTextArea.getText() + "\n"
							+ e1.getMessage());
				}
			}
		});
		tempNorthPanelIMAP.add(testImap);
		imapPanel.add(tempNorthPanelIMAP, BorderLayout.NORTH);

		JSplitPane imapSplit = new JSplitPane();

		imapTextArea = new JTextArea();
		imapTextArea.setEditable(false);
		imapTextArea.setToolTipText("Console");
		imapSplit.setRightComponent(new JScrollPane(imapTextArea));

		final JTable imapTable = new JTable(getCurrentRows("imap"), col);
		imapTable.setModel(new PropertiesTableModel());
		JPanel imapRPanel = new JPanel(new BorderLayout());
		imapRPanel.add(new JScrollPane(imapTable), BorderLayout.CENTER);
		imapSplit.setLeftComponent(imapRPanel);
		JButton imapSaveButton = new JButton("Save IMAP Properties");
		
		imapSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveProperties(imapTable);
			}
		});
		JPanel imapSaveTmpPanel = new JPanel();
		imapSaveTmpPanel.add(imapSaveButton);
		imapRPanel.add(imapSaveTmpPanel, BorderLayout.SOUTH);

		imapPanel.add(imapSplit, BorderLayout.CENTER);
		tabs.addTab("IMAP", imapPanel);

		smtpPanel = new JPanel(new BorderLayout());

		JPanel tempSendNorthPanel = new JPanel();
		JLabel label = new JLabel("TO ");
		tempSendNorthPanel.add(label);

		sendTo = new JTextField(20);
		tempSendNorthPanel.add(sendTo);

		sendButton = new JButton("TEST | SMTP (Send Email)");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sendTextArea.setText(sendTextArea.getText()
							+ "Pleace wait...");
					String to = sendTo.getText();
					TestConnectionSMPT.sendMail(to);
					sendTextArea.setText("Send successfully.\nSend To : " + to);
					sendTextArea.setText(sendTextArea.getText() + "\n"
							+ "Send test is OK!");
					sendTextArea.setText(sendTextArea.getText()
							+ "\n-----------------------------------------------------------------------------------");
				} catch (Exception e1) {
					sendTextArea.setText(sendTextArea.getText() + "\n"
							+ e1.getMessage());
					e1.printStackTrace();
				}
			}
		});

		tempSendNorthPanel.add(sendButton);
		smtpPanel.add(tempSendNorthPanel, BorderLayout.NORTH);

		JSplitPane smtpSplit = new JSplitPane();
		sendTextArea = new JTextArea();
		sendTextArea.setEditable(false);
		sendTextArea.setToolTipText("Console");
		smtpSplit.setRightComponent(new JScrollPane(sendTextArea));

		final JTable smtpTable = new JTable(getCurrentRows("smtp"), col);
		smtpTable.setModel(new PropertiesTableModel());
		JPanel smtpRPanel = new JPanel(new BorderLayout());
		smtpRPanel.add(new JScrollPane(smtpTable), BorderLayout.CENTER);
		smtpSplit.setLeftComponent(smtpRPanel);
		JButton smtpSaveButton = new JButton("Save SMTP Properties");
		smtpSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveProperties(smtpTable);
			}
		});
		JPanel smtpSaveTmpPanel = new JPanel();
		smtpSaveTmpPanel.add(smtpSaveButton);
		smtpRPanel.add(smtpSaveTmpPanel, BorderLayout.SOUTH);

		smtpPanel.add(smtpSplit, BorderLayout.CENTER);
		tabs.addTab("SMTP", smtpPanel);

		tabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tableUpdateRows(pop3Table, "pop3");
				tableUpdateRows(imapTable, "imap");
				tableUpdateRows(smtpTable, "smtp");
			}
		});

		container.add(tabs);
	}

	private void tableUpdateRows(JTable table, String co) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			model.removeRow(i);
		}
		model.setDataVector(getCurrentRows(co), col);
		table.repaint();
	}

	private void saveProperties(JTable table) {
		int rowCount = table.getModel().getRowCount();

		Properties props = new Properties();
		try {
			FileReader fr = new FileReader(fileName);
			props.load(fr);
			fr.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		for (int i = 0; i < rowCount; i++) {
			String key = (String) table.getModel().getValueAt(i, 0);
			String val = (String) table.getModel().getValueAt(i, 1);
			props.put(key, val);
		}
		try {
			FileWriter fw = new FileWriter(fileName);
			props.store(fw, "Mail Properties");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Properties Saved!", "Info",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private Vector<Vector<String>> getCurrentRows(String co) {
		Properties prop = TestConnectionPOP3.createProperties(fileName);
		Set<Object> s = prop.keySet();
		Object[] arr = s.toArray();
		Vector<Vector<String>> row = new Vector<Vector<String>>();
		for (int i = 0; i < arr.length; i++) {
			String p = (String) arr[i];
			if (p.contains(co) || p.equals("mail.user")
					|| p.equals("mail.password")) {
				Vector<String> v = new Vector<String>();
				v.add(p);
				v.add(prop.get(arr[i]) + "");
				row.add(v);
			}
		}
		return row;
	}

	class PropertiesTableModel extends DefaultTableModel {
		public PropertiesTableModel() {
			super(new Object[][] {}, new String[] { "Key", "Value" });
		}

		Class<?>[] columnTypes = new Class<?>[] { String.class, String.class };

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return columnTypes[columnIndex];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex != 0);
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length != 1) {
			throw new RuntimeException(
					"Please Set Parameter Properties Folde Name");
		}

		final String folderName = args[0];

		try {
			javax.swing.UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Runnable run = new Runnable() {
			public void run() {
				new MainPage(folderName).setVisible(true);
			}
		};
		SwingUtilities.invokeLater(run);
	}
}