package com.dforensic.plugin.manal.wizards;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class apkDialog extends TitleAreaDialog {

	  private Text path;
	  private String sPath;
	  

	  public apkDialog(Shell parentShell) {
	    super(parentShell);
	  }

	  @Override
	  public void create() {
	    super.create();
	    setTitle("Input apk file.");
	  }

	  @Override
	  protected Control createDialogArea(Composite parent) {
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);   
	    GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("Apk file path:");

		path = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 460;
		
		path.setLayoutData(gd);

		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		
	    return area;
	  }
	  
	  private void handleBrowse()
	  {
			FileDialog fileDialog = new FileDialog(getShell());
			fileDialog.setText("Select File");
			fileDialog.setFilterExtensions(new String[] { "*.apk" });
			fileDialog.setFilterNames(new String[] { "apk files(*.apk)" });
			String selected = fileDialog.open();
			saveInput(selected);
			this.path.setText(this.sPath);
	  }
	  
	  
	  private void saveInput(String selected) {
	    sPath = selected;

	    //System.out.println(sPath);
	  }

	  @Override
	  protected void okPressed() {
	    if(this.sPath != null)
	    	this.path.setText(this.sPath);
	    super.okPressed();
	  }

	  public String getPath() {
	    return sPath;
	  }

		  
}
