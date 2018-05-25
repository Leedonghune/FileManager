import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;

public class AddListener implements ItemListener {
	JLabel subTitle;
	String title[];
	
	AddListener(JLabel subTitle) {
		this.subTitle = subTitle;
	}
	AddListener(String title[]) {
		this.title = title;
	}
	
	
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		String str = (String)arg0.getItem();
		if(str.equals("�ѱ���")) {
			subTitle.setText("���� Ž����");
			ATable.title[0] = "�̸�";
			ATable.title[1] = "ũ��";
			ATable.title[2] = "������ ��¥";
			
		}
		if(str.equals("ENGLISH")) {
			subTitle.setText("FILE READER");
			ATable.title[0] = "NAME";
			ATable.title[1] = "SIZE";
			ATable.title[2] = "MODIFY DATE";
		}
	}
	

}
