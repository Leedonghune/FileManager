import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.io.*;
import javax.swing.event.*;

import java.util.*;

import java.text.*;



class FileViewer implements TreeWillExpandListener,TreeSelectionListener
{ 

	private JFrame frame = new JFrame("/home/");
	private Container con = null;
	private JPanel com= new JPanel(new BorderLayout());
	private JComboBox ComB = new JComboBox();
	private JLabel La = new JLabel("File Explorer");
	private JSplitPane pMain=new JSplitPane();
	private JScrollPane pLeft=null;
	private JPanel pRight=new JPanel(new BorderLayout());


	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("..");
	private JTree tree;

	private JPanel pNorth=new JPanel();

	

	private Dimension dim,dim1;
	
	FileViewer(){
		init();
		start();
		frame.setSize(800,600);
		frame.setLocationRelativeTo(null); //frame�� ��� ����
		frame.setVisible(true);
	}

	void init(){
		pMain.setResizeWeight(1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		con = frame.getContentPane();
//		con.setLayout(new BorderLayout());

		ComB.addItem("�ѱ���");
		ComB.addItem("ENGLISH");
		ComB.addItemListener(new AddListener(La));

		com.add(ComB,BorderLayout.EAST);//panel�� combobox�� ����
		com.add(La,BorderLayout.WEST);
		frame.add(com,BorderLayout.SOUTH);
		frame.add(pNorth,"North");
		File file=new File("");
		File list[]=file.listRoots();
		DefaultMutableTreeNode temp;

		for(int i=0;i<list.length;++i)
		{
			temp=new DefaultMutableTreeNode(list[i].getPath());
			temp.add(new DefaultMutableTreeNode("����"));
			root.add(temp);
		}
		tree=new JTree(root);
		pLeft=new JScrollPane(tree);

		pMain.setDividerLocation(150);
		pMain.setLeftComponent(pLeft);
		pMain.setRightComponent(pRight);
		frame.add(pMain);

	}

	void start()
	{
		tree.addTreeWillExpandListener(this);
		tree.addTreeSelectionListener(this);
	}

	public static void main(String args[]){
		JFrame.setDefaultLookAndFeelDecorated(true);
		new FileViewer();
	}

	String getPath(TreeExpansionEvent e)
	{
		StringBuffer path=new StringBuffer();//append,stringbuffer �޸� ��������
		TreePath temp=e.getPath(); 
		Object list[]=temp.getPath();
		for(int i=0;i<list.length;++i)//list�迭 ũ�⸸ŭ for���� ������
		{
			if(i>0)
			{
				path.append(((DefaultMutableTreeNode)list[i]).getUserObject()+"\\");
			}
		}
		return path.toString();
	}
	String getPath(TreeSelectionEvent e)
	{
		StringBuffer path=new StringBuffer();
		TreePath temp=e.getPath(); 
		Object list[]=temp.getPath();
		for(int i=0;i<list.length;++i)
		{
			if(i>0)
			{
				path.append(((DefaultMutableTreeNode)list[i]).getUserObject()+"\\");
			}
		}
		return path.toString();
	}

	public void treeWillCollapse(TreeExpansionEvent event){}

	public void treeWillExpand(TreeExpansionEvent e)
	{
		if(((String)((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getUserObject()).equals("����ǻ��")){}
		else
		{
			try{
				DefaultMutableTreeNode parent=(DefaultMutableTreeNode)e.getPath().getLastPathComponent();
				File tempFile=new File(getPath(e));
				File list[]=tempFile.listFiles();
				DefaultMutableTreeNode tempChild;//TreeNode���� ����
				for(File temp:list)
				{
					if(temp.isDirectory() && !temp.isHidden())
					{
						tempChild=new DefaultMutableTreeNode(temp.getName());
						if(true)
						{
							File inFile=new File(getPath(e)+temp.getName()+"\\");
							File inFileList[]=inFile.listFiles();
							for(File inTemp:inFileList)
							{
								if(inTemp.isDirectory() && !inTemp.isHidden())
								{
									tempChild.add(new DefaultMutableTreeNode("����"));
									break;
								}
							}
						}
						parent.add(tempChild);
					}
				}
				parent.remove(0);
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(frame, "��ũ Ȥ�� ������ ã���� �����ϴ�.");
			}
		}
	}
	public void valueChanged(TreeSelectionEvent e)
	{
		if(((String)((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getUserObject()).equals("����ǻ��")){
		}
		else
		{
			try
			{
				pRight=new FView(getPath(e)).getTablePanel();
				pMain.setRightComponent(pRight);
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(frame, "��ũ Ȥ�� ������ ã���� �����ϴ�.");
			}
		}
	}
}
class FView
{ 
	public static String title[]= {"�̸�", "ũ��","������ ��¥"};
	public  ATable at=new ATable(title);
	private JTable jt=new JTable(at);

	private JPanel pMain=new JPanel(new BorderLayout());
	private JScrollPane pCenter=new JScrollPane(jt);

	private File file;
	private File list[];
	private long size=0,time=0;

	FView(String str){
		init();
		start(str);
	}

	void init(){
		pMain.add(pCenter,"Center");
	}

	void start(String strPath)
	{
		file=new File(strPath);
		list=file.listFiles();
		at.setValueArr(list.length);
		for(int i=0;i<list.length;++i)
		{
			size=list[i].length();
			time=list[i].lastModified();
			for(int j=0;j<3;++j)
			{
				switch(j)
				{
				case 0:
					at.setValueAt(list[i].getName(),i,j);
					break;
				case 1:
					if(list[i].isFile())
						at.setValueAt(Long.toString((long)Math.round(size/1024.0))+"Kb",i,j);
					break;
				case 2:
					at.setValueAt(getFormatString(time),i,j); 
					break;



				}
			}
		}
		jt.repaint();
		pCenter.setVisible(false);
		pCenter.setVisible(true);
	}

	String getLastName(String name)
	{
		int pos=name.lastIndexOf(".");
		String result=name.substring(pos+1,name.length());
		return result;
	}
	String getFormatString(long time)
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		Date d=new Date(time);
		String temp = sdf.format(d);
		return temp;
	}
	JPanel getTablePanel()
	{
		return pMain;
	}
}



class ATable extends AbstractTableModel
{
	public static String title[]=new String[3];
	public String val[][]=new String[1][3];
	ATable(String ti[]) {
		title=ti;
	}
	public void setValueArr(int i)
	{
		val=new String[i][4];
	}
	public int getRowCount()
	{
		return val.length;
	}
	public int getColumnCount()
	{
		return val[0].length;
	}
	public String getColumnName(int column )
	{
		return title[column];
	}
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		if(columnIndex==0)
			return true;
		else
			return false;
	}
	public Object getValueAt(int row, int column)
	{
		return val[row][column];
	}
	public void setValueAt(String aValue, int rowIndex, int columnIndex ){
		val[rowIndex][columnIndex] = aValue;
	}
}