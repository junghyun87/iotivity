/*
 * Copyright 2015 Samsung Electronics All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package oic.simulator.serviceprovider.view.dialogs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import oic.simulator.serviceprovider.Activator;
import oic.simulator.serviceprovider.model.Device;
import oic.simulator.serviceprovider.utils.Constants;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This dialog is used for loading the RAML file.
 */
public class AddResourceToDevices extends TitleAreaDialog {

    private CheckboxTreeViewer treeViewer;

    List<Device>               sourceList;

    Set<Device>                selectedDeviceList;

    public AddResourceToDevices(Shell parentShell, List<Device> sourceList) {
        super(parentShell);
        this.sourceList = sourceList;
        selectedDeviceList = new HashSet<Device>();
    }

    @Override
    public void create() {
        super.create();
        setTitle("Add To Devices");
        setMessage("Select one or more devices to which the single resource will be added.");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite compLayout = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(compLayout, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout();
        layout.verticalSpacing = 10;
        layout.marginTop = 10;
        container.setLayout(layout);

        Label lbl = new Label(container, SWT.NONE);
        lbl.setText("Select Resources from:");
        GridData gd;
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        lbl.setLayoutData(gd);

        Group resourceGroup = new Group(container, SWT.NONE);

        Color color = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

        resourceGroup.setLayout(new GridLayout());
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.heightHint = 300;
        gd.horizontalSpan = 2;
        resourceGroup.setLayoutData(gd);

        treeViewer = new CheckboxTreeViewer(resourceGroup);
        treeViewer.getTree().setBackground(color);
        gd = new GridData();
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
        gd.grabExcessVerticalSpace = true;
        gd.verticalAlignment = SWT.FILL;
        // gd.heightHint = 300;
        treeViewer.getTree().setLayoutData(gd);
        treeViewer.setContentProvider(new TreeContentProvider());
        treeViewer.setLabelProvider(new TreeLabelProvider());
        treeViewer.setInput(new Object());
        treeViewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent e) {
                Device dev = (Device) e.getElement();
                if (null != dev) {
                    if (e.getChecked()) {
                        selectedDeviceList.add(dev);
                        System.out.println("Checked" + dev.getDeviceName());
                    } else {
                        selectedDeviceList.remove(dev);
                        System.out.println("Unchecked:" + dev.getDeviceName());
                    }
                }
            }
        });

        return compLayout;
    }

    public Set<Device> getSelectedDeviceList() {
        return selectedDeviceList;
    }

    class TreeContentProvider implements ITreeContentProvider {

        @Override
        public void dispose() {
        }

        @Override
        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        }

        @Override
        public Object[] getChildren(Object parent) {
            return null;
        }

        @Override
        public Object[] getElements(Object parent) {
            if (null == sourceList) {
                sourceList = new ArrayList<Device>();
            }
            return sourceList.toArray();
        }

        @Override
        public Object getParent(Object child) {
            return null;
        }

        @Override
        public boolean hasChildren(Object parent) {
            return false;
        }
    }

    class TreeLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            Device dev = (Device) element;
            return dev.getDeviceName();
        }

        @Override
        public Image getImage(Object element) {
            if (element instanceof Device) {
                return Activator.getDefault().getImageRegistry()
                        .get(Constants.DEVICE);
            } else {
                return null;
            }
        }
    }

    @Override
    protected void okPressed() {
        if (selectedDeviceList.isEmpty()) {
            MessageDialog.openInformation(
                    Display.getDefault().getActiveShell(),
                    "No selection done.", "No devices are selected.");
            return;
        }
        close();
    }

    @Override
    public boolean isHelpAvailable() {
        return false;
    }
}
