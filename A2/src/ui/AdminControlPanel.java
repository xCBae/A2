package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import component.Tree;
import component.UserComponentVisitor;
import component.UserUpdateVisitor;
import component.VisitorValidation;
import user.UserGroup;
import user.UserName;
import java.awt.Color;
import javax.swing.tree.DefaultTreeModel;

public class AdminControlPanel extends JFrame {

    private static AdminControlPanel acp = null;
	private DefaultMutableTreeNode jRoot, child;
	private JTree tree;
	private int groupTotal = 1;
	private int userTotal = 0;
	private JPanel panel;
	private UserComponentVisitor findUserC = new UserComponentVisitor();
	private VisitorValidation valVis = new VisitorValidation();
	private UserUpdateVisitor lastUpdate = new UserUpdateVisitor();
	private UserGroup rootGroup = new UserGroup("ROOT");
	private Tree root = new Tree("ROOT", rootGroup);
	
    private AdminControlPanel(){ 
        makeGui();
    }
    
    public static AdminControlPanel getInstance(){
        if (acp == null){
        	acp = new AdminControlPanel();
		}
        return acp;
    }


    public void makeGui() {
		//Frame
        JPanel contentPane;
        this.setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		//Split Panel
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane);
		
		//Left panel
        JPanel tree_Panel = new JPanel();
        tree_Panel.setBackground(new Color(0, 128, 192));
		splitPane.setLeftComponent(tree_Panel);

		//Right panel
		panel = new JPanel();
		panel.setBackground(new Color(0, 128, 192));
		splitPane.setRightComponent(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
        //Tree on left panel
		jRoot = new DefaultMutableTreeNode("ROOT");
		tree = new JTree(jRoot);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("ROOT") {
				{
				}
			}
		));
		DefaultMutableTreeNode firstLeaf = ((DefaultMutableTreeNode)tree.getModel().getRoot()).getFirstLeaf();
		tree.setSelectionPath(new TreePath(firstLeaf.getPath()));
		tree_Panel.add(tree);
		
		//Panel to add user
		addUserComponents();
		//Open user control panel
		userControlPanel();
		//Total users/group panel
		userComponentCount();
		//Message Panel
		messageCount();

        setVisible(true);
	}    

	//Add users and groups
	private void addUserComponents(){
		JTextField userTxtField;
        JTextField groupTxtField;
		JPanel user_Panel = new JPanel();
		user_Panel.setBackground(new Color(0, 128, 192));
		panel.add(user_Panel);
		user_Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

		//Text field to add user
		userTxtField = new JTextField();
		user_Panel.add(userTxtField);
		userTxtField.setColumns(10);
		
		//Button for adding user
		JButton addUser_button = new JButton("Add User");
		addUser_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Element that cursor selected in JTree
				DefaultMutableTreeNode selectedElement 
   					=(DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
				UserGroup tempGroup = new UserGroup(selectedElement.toString());
				UserName username = new UserName(userTxtField.getText());
				Tree selectedGroup = root.accept(findUserC,tempGroup);
				//Add user if selected directory is a group and the user does not exist already
				if(selectedGroup != null && selectedGroup.getUserComponent() instanceof UserGroup){
					if(root.accept(findUserC,username) != null){
						System.out.println("Error. User already exits.");
						return;
					}
					root.accept(findUserC,tempGroup).getChildren().add(new Tree(userTxtField.getText(),username));
					//Update UI
					child = new DefaultMutableTreeNode(userTxtField.getText());
					selectedElement.add(child);
					tree.updateUI();
					//Increment user total
					userTotal++;
				}else{
					System.out.println("Error can not add user to another user.");
					
				}
			}
		});

        addUser_button.setPreferredSize(new Dimension(200, 25));
		user_Panel.add(addUser_button);
		
		//Add group
		JPanel group_Panel = new JPanel();
		group_Panel.setBackground(new Color(0, 128, 192));
		panel.add(group_Panel);
		group_Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		
		groupTxtField = new JTextField();
		group_Panel.add(groupTxtField);
		groupTxtField.setColumns(10);
		
		JButton addGroup_button = new JButton("Add Group");
		addGroup_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectedElement 
   					=(DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
				UserGroup tempGroup = new UserGroup(selectedElement.toString());
				UserGroup UserGroup = new UserGroup(groupTxtField.getText());
				Tree selectedGroup = root.accept(findUserC,tempGroup);
				if(selectedGroup != null && selectedGroup.getUserComponent() instanceof UserGroup ){
					if(root.accept(findUserC,UserGroup) != null){
						System.out.println("Error. This group already exists.");
						return;
					}
					selectedGroup.getChildren().add(new Tree(groupTxtField.getText(),UserGroup));
					//Update UI
					child = new DefaultMutableTreeNode(groupTxtField.getText());
					selectedElement.add(child);
					tree.updateUI();
					//Increment Group total
					groupTotal++;
				}else{
					System.out.println("Error can not add group to a user.");
				}
			}
		});
		addGroup_button.setPreferredSize(new Dimension(200, 25));
		group_Panel.add(addGroup_button);
	}

	//User control panel
	private void userControlPanel(){
		JPanel userView_Panel = new JPanel();
		userView_Panel.setBackground(new Color(0, 128, 192));
		panel.add(userView_Panel);
		userView_Panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		
		JButton openUserView_button = new JButton("Open User View");
		openUserView_button.setPreferredSize(new Dimension(150, 25));
		openUserView_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectedElement 
   				=(DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
				
				Tree selectedNode = root.accept(findUserC, new UserName(selectedElement.toString()));
				UserPanel userPanel = new UserPanel((UserName)selectedNode.getUserComponent(), root);
			}
		});
		userView_Panel.add(openUserView_button);

		//Last updated user button
		JButton lastUpdate_button = new JButton("Last Update User");
		lastUpdate_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("The last updated user is: "+root.accept(lastUpdate));
			}
		});
		userView_Panel.add(lastUpdate_button);

		//Validate username button
		JButton validateButton = new JButton("Validate Names");
		validateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isValid = root.accept(valVis);
				if(isValid == true){
					System.out.println("Usernames are valid.");
				}else{
					System.out.println("Usernames are not valid.");
				}
			}
		});
		userView_Panel.add(validateButton);
		validateButton.setPreferredSize(new Dimension(150, 25));
	}

	//Users and groups total
	private void userComponentCount(){
		JPanel total_Panel = new JPanel();
		panel.add(total_Panel);
		total_Panel.setLayout(new GridLayout(2, 2, 0, 0));
		
		//Panels
		JPanel userTotalPanel = new JPanel();
		userTotalPanel.setBackground(new Color(0, 128, 192));
		total_Panel.add(userTotalPanel);
		JPanel groupTotalBtnPanel = new JPanel();
		groupTotalBtnPanel.setBackground(new Color(0, 128, 192));
		total_Panel.add(groupTotalBtnPanel);
		JPanel userLabelPanel = new JPanel();
		total_Panel.add(userLabelPanel);
		JPanel groupTotalLabelPanel = new JPanel();
		total_Panel.add(groupTotalLabelPanel);
		
		//Labels
		JLabel lblNewLabel = new JLabel("Total Users: "+ userTotal);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		JLabel lblNewLabel_1 = new JLabel("Total Groups: "+ groupTotal);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		userLabelPanel.add(lblNewLabel);
		groupTotalLabelPanel.add(lblNewLabel_1);

		//User total
		JButton userTotal_button = new JButton("Show User Total");
		userTotal_button.setPreferredSize(new Dimension(200, 25));
		userTotal_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNewLabel.setText("Total Users: "+ userTotal);
				lblNewLabel.updateUI();
			}
		});
		userTotalPanel.add(userTotal_button);

		//Group total
		JButton groupTotal_button = new JButton("Show Group Total");
		groupTotal_button.setPreferredSize(new Dimension(200, 25));
		groupTotal_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNewLabel_1.setText("Total Groups: "+ groupTotal);
				lblNewLabel_1.updateUI();
			}
		});
		groupTotalBtnPanel.add(groupTotal_button);
	}

	//Message count and positive message ratio
	private void messageCount(){

		JPanel message_Panel = new JPanel();
		JLabel msgTotal_label = new JLabel("Total Message: 0");
		msgTotal_label.setFont(new Font("Tahoma", Font.BOLD, 11));
		JLabel posPerc_label = new JLabel("Positive ratio: ");
		posPerc_label.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(message_Panel);
		message_Panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel msgTotalbtn_panel = new JPanel();
		msgTotalbtn_panel.setBackground(new Color(0, 128, 192));
		message_Panel.add(msgTotalbtn_panel);
		
		JButton messageTotal_button = new JButton("Show Messages Total");
		messageTotal_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int msgCount = root.countMsg(root)[0];
				msgTotal_label.setText("Total Messages: "+ msgCount);
				msgTotal_label.updateUI();
			}
		});
		msgTotalbtn_panel.add(messageTotal_button);
		messageTotal_button.setFont(new Font("Tahoma", Font.PLAIN, 11));
		messageTotal_button.setPreferredSize(new Dimension(200, 25));
		
		JPanel posPercBtn_panel = new JPanel();
		posPercBtn_panel.setBackground(new Color(0, 128, 192));
		message_Panel.add(posPercBtn_panel);
		
		JButton posPerc_button = new JButton("Show Positive Percentage");
		posPercBtn_panel.add(posPerc_button);
		posPerc_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float ratio = (float)(root.countMsg(root)[1])/root.countMsg(root)[0];
				ratio = (float)Math.round(ratio *10000)/100;
				posPerc_label.setText("Positive ratio: "+Float.toString(ratio)+"%");
				posPerc_label.updateUI();
			}
		});
		posPerc_button.setPreferredSize(new Dimension(200, 25));
		
		JPanel totalMsgLbl_panel = new JPanel();
		message_Panel.add(totalMsgLbl_panel);
		
		totalMsgLbl_panel.add(msgTotal_label);
		
		JPanel posPercLbl_panel = new JPanel();
		message_Panel.add(posPercLbl_panel);
	
		posPercLbl_panel.add(posPerc_label);
	}

}