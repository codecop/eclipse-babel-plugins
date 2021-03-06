/*******************************************************************************
 * Copyright (c) 2012 Martin Reiterer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Martin Reiterer - Initial implementation
 ******************************************************************************/
package org.eclipse.babel.tapiji.tools.java.ui.autocompletion;

import org.eclipse.babel.editor.wizards.IResourceBundleWizard;
import org.eclipse.babel.tapiji.tools.core.Logger;
import org.eclipse.babel.tapiji.tools.core.ui.ResourceBundleManager;
import org.eclipse.babel.tapiji.tools.core.ui.builder.I18nBuilder;
import org.eclipse.babel.tapiji.tools.core.ui.utils.ResourceUtils;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;

public class CreateResourceBundleProposal implements IJavaCompletionProposal {

    private IResource resource;
    private int start;
    private int end;
    private String key;
    private final String newBunldeWizard = "org.eclipse.babel.editor.wizards.ResourceBundleWizard";

    public CreateResourceBundleProposal(String key, IResource resource,
            int start, int end) {
        this.key = ResourceUtils.deriveNonExistingRBName(key,
                ResourceBundleManager.getManager(resource.getProject()));
        this.resource = resource;
        this.start = start;
        this.end = end;
    }

    public String getDescription() {
        return "Creates a new Resource-Bundle with the id '" + key + "'";
    }

    public String getLabel() {
        return "Create Resource-Bundle '" + key + "'";
    }

    @SuppressWarnings("deprecation")
    protected void runAction() {
        // First see if this is a "new wizard".
        IWizardDescriptor descriptor = PlatformUI.getWorkbench()
                .getNewWizardRegistry().findWizard(newBunldeWizard);
        // If not check if it is an "import wizard".
        if (descriptor == null) {
            descriptor = PlatformUI.getWorkbench().getImportWizardRegistry()
                    .findWizard(newBunldeWizard);
        }
        // Or maybe an export wizard
        if (descriptor == null) {
            descriptor = PlatformUI.getWorkbench().getExportWizardRegistry()
                    .findWizard(newBunldeWizard);
        }
        try {
            // Then if we have a wizard, open it.
            if (descriptor != null) {
                IWizard wizard = descriptor.createWizard();

                if (!(wizard instanceof IResourceBundleWizard)) {
                    return;
                }

                IResourceBundleWizard rbw = (IResourceBundleWizard) wizard;
                String[] keySilbings = key.split("\\.");
                String rbName = keySilbings[keySilbings.length - 1];
                String packageName = "";

                rbw.setBundleId(rbName);

                // Set the default path according to the specified package name
                String pathName = "";
                if (keySilbings.length > 1) {
                    try {
                        IJavaProject jp = JavaCore
                                .create(resource.getProject());
                        packageName = key.substring(0, key.lastIndexOf("."));

                        for (IPackageFragmentRoot fr : jp
                                .getAllPackageFragmentRoots()) {
                            IPackageFragment pf = fr
                                    .getPackageFragment(packageName);
                            if (pf.exists()) {
                                pathName = pf.getResource().getFullPath()
                                        .removeFirstSegments(0).toOSString();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        pathName = "";
                    }
                }

                try {
                    IJavaProject jp = JavaCore.create(resource.getProject());
                    if (pathName.trim().equals("")) {
                        for (IPackageFragmentRoot fr : jp
                                .getAllPackageFragmentRoots()) {
                            if (!fr.isReadOnly()) {
                                pathName = fr.getResource().getFullPath()
                                        .removeFirstSegments(0).toOSString();
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    pathName = "";
                }

                rbw.setDefaultPath(pathName);

                WizardDialog wd = new WizardDialog(Display.getDefault()
                        .getActiveShell(), wizard);

                wd.setTitle(wizard.getWindowTitle());
                if (wd.open() == WizardDialog.OK) {
                    try {
                        resource.getProject().build(
                                IncrementalProjectBuilder.FULL_BUILD,
                                I18nBuilder.BUILDER_ID, null, null);
                    } catch (CoreException e) {
                        Logger.logError(e);
                    }

                    ITextFileBufferManager bufferManager = FileBuffers
                            .getTextFileBufferManager();
                    IPath path = resource.getRawLocation();
                    try {
                        bufferManager.connect(path, LocationKind.NORMALIZE,
                                null);
                        ITextFileBuffer textFileBuffer = bufferManager
                                .getTextFileBuffer(path, LocationKind.NORMALIZE);
                        IDocument document = textFileBuffer.getDocument();

                        if (document.get().charAt(start - 1) == '"'
                                && document.get().charAt(start) != '"') {
                            start--;
                            end++;
                        }
                        if (document.get().charAt(end + 1) == '"'
                                && document.get().charAt(end) != '"') {
                            end++;
                        }

                        document.replace(start, end - start, "\""
                                + (packageName.equals("") ? "" : packageName
                                        + ".") + rbName + "\"");

                        textFileBuffer.commit(null, false);
                    } catch (Exception e) {
                    } finally {
                        try {
                            bufferManager.disconnect(path, null);
                        } catch (CoreException e) {
                        }
                    }
                }
            }
        } catch (CoreException e) {
        }
    }

    @Override
    public void apply(IDocument document) {
        this.runAction();
    }

    @Override
    public String getAdditionalProposalInfo() {
        return getDescription();
    }

    @Override
    public IContextInformation getContextInformation() {
        return null;
    }

    @Override
    public String getDisplayString() {
        return getLabel();
    }

    @Override
    public Point getSelection(IDocument document) {
        return null;
    }

    @Override
    public Image getImage() {
        return PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage();
    }

    @Override
    public int getRelevance() {
        // TODO Auto-generated method stub
        if (end - start == 0) {
            return 99;
        } else {
            return 1099;
        }
    }

}
