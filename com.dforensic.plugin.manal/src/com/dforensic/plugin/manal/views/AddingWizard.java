package com.dforensic.plugin.manal.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class AddingWizard extends Wizard implements INewWizard{

	private AddingWizardPage page;
	private ISelection selection;
	public AddingWizard()
	{
		super();
		setNeedsProgressMonitor(true);
	}
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		
	}

	@Override
	public void addPages() {
		
		page = new AddingWizardPage(selection);
		System.out.println("test");
		addPage(page);
	}

	@Override
	public boolean canFinish() {
		
		return false;
	}

	@Override
	public void createPageControls(Composite arg0) {
		
		
	}

	@Override
	public void dispose() {
		
		
	}

	@Override
	public IWizardContainer getContainer() {
		
		return null;
	}

	@Override
	public Image getDefaultPageImage() {
		
		return null;
	}

	@Override
	public IDialogSettings getDialogSettings() {
		
		return null;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage arg0) {
		
		return null;
	}

	@Override
	public IWizardPage getPage(String arg0) {
	
		return null;
	}

	@Override
	public int getPageCount() {
		
		return 0;
	}

	@Override
	public IWizardPage[] getPages() {
		
		return null;
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage arg0) {
		
		return null;
	}

	@Override
	public IWizardPage getStartingPage() {
		
		return null;
	}

	@Override
	public RGB getTitleBarColor() {
		
		return null;
	}

	@Override
	public String getWindowTitle() {
		
		return null;
	}

	@Override
	public boolean isHelpAvailable() {
		
		return false;
	}

	@Override
	public boolean needsPreviousAndNextButtons() {
		
		return false;
	}

	@Override
	public boolean needsProgressMonitor() {
		
		return false;
	}

	@Override
	public boolean performCancel() {
		return false;
	}

	@Override
	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String fileName = page.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void setContainer(IWizardContainer arg0) {
		
		
	}
	
	
	private void doFinish(
			String containerName,
			String fileName,
			IProgressMonitor monitor)
			throws CoreException {
			// create a sample file
			monitor.beginTask("Creating " + fileName, 2);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource resource = root.findMember(new Path(containerName));
			if (!resource.exists() || !(resource instanceof IContainer)) {
				throwCoreException("Container \"" + containerName + "\" does not exist.");
			}
			IContainer container = (IContainer) resource;
			final IFile file = container.getFile(new Path(fileName));
			try {
				InputStream stream = openContentStream();
				if (file.exists()) {
					file.setContents(stream, true, true, monitor);
				} else {
					file.create(stream, true, monitor);
				}
				stream.close();
			} catch (IOException e) {
			}
			monitor.worked(1);
			monitor.setTaskName("Opening file for editing...");
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page =
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, file, true);
					} catch (PartInitException e) {
					}
				}
			});
			monitor.worked(1);
		}
	
	/**** make myself ****/
	private InputStream openContentStream() {
		String contents =
			"This is the initial file contents for *.man file that should be word-sorted in the Preview page of the multi-page editor";
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "Manal", IStatus.OK, message, null);
		throw new CoreException(status);
	}


}
