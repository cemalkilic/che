/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.java.client.refactoring.extractMethod.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
//import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Button;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.theme.Style;
import org.eclipse.che.ide.ext.java.client.JavaLocalizationConstant;
import org.eclipse.che.ide.ext.java.client.JavaResources;
import org.eclipse.che.ide.ext.java.shared.dto.refactoring.RefactoringStatus;
import org.eclipse.che.ide.ext.java.shared.dto.refactoring.RefactoringStatusEntry;
import org.eclipse.che.ide.ui.window.Window;

import java.util.List;

@Singleton
public class ExtractMethodViewImpl extends Window implements ExtractMethodView {
    interface ExtractMethodViewImplUiBinder extends UiBinder<Widget, ExtractMethodViewImpl> {

    }

    private static ExtractMethodViewImplUiBinder UI_Bınder = GWT.create(ExtractMethodViewImplUiBinder.class);

    private final JavaResources javaResources;

    @UiField(provided = true)
    final JavaLocalizationConstant locale;

    @UiField
    FlowPanel headerPanelToHide;
    @UiField
    TextBox methodName;
    @UiField
    FlowPanel subSettings;
    @UiField
    CheckBox declareStatic;
    @UiField
    CheckBox foldParameters;
    @UiField
    CheckBox declareVarArgs;
    @UiField
    ListBox modifierBox;
    @UiField
    ListBox returnTypeBox;
    @UiField
    FlexTable parametersTable;
    @UiField
    SimplePanel icon;
    @UiField
    Label       errorLabel;

    @Override
    public boolean isFoldParameters() {
        return foldParameters.getValue();
    }

    @Override
    public boolean isDeclareVarArgs() {
        return declareVarArgs.getValue();
    }




    private ActionDelegate delegate;
    /*private Button up;
    private Button down;*/

    @Inject
    public ExtractMethodViewImpl(JavaLocalizationConstant locale,
                                 JavaResources javaResorces) {
        this.locale = locale;
        this.javaResources = javaResorces;

        setWidget(UI_Bınder.createAndBindUi(this));

        //createButtons(locale);

        //updaterFullNames
        methodName.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent keyUpEvent) {
                delegate.validateName();
            }
        });
    }

    /*private void createButtons(JavaLocalizationConstant locale) {
        up = createButton(locale.)
    }*/

    /** {@inheritDoc} */
    @Override
    public void show() {
        methodName.getElement().setAttribute("spellcheck", "false");
        methodName.addStyleName(javaResources.css().errorBorder());

        super.show();

        new Timer() {
            @Override
            public void run() {
                setFocus();
            }
        }.schedule(100);;
    }

    /** {@inheritDoc} */
    @Override
    protected void onClose() {
        delegate.onCancelButtonClicked();

        super.onClose();
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return methodName.getName();
    }


    /** {@inheritDoc} */
    public void setOldName(String name) {
        methodName.setText(name);
    }

    /** {@inheritDoc} */
    public void clearErrorLabel() {
        methodName.removeStyleName(javaResources.css().errorBorder());
        errorLabel.setText("");
    }

    /** {@inheritDoc} */
    @Override
    public void showStatusMessage(RefactoringStatus status) {
        errorLabel.getElement().getStyle().setColor(Style.getMainFontColor());
        showMessage(status);
    }

    @Override
    public void setFocus() {
        methodName.selectAll();
        methodName.setFocus(true);
    }

    private void showMessage(RefactoringStatus status) {
        RefactoringStatusEntry statusEntry = getEntryMatchingSeverity(status.getSeverity(), status.getEntries());
        if (statusEntry != null) {
            errorLabel.setText(statusEntry.getMessage());
        } else {
            errorLabel.setText("");
        }
    }

    /**
     * Returns the first entry which severity is equal or greater than the
     * given severity. If more than one entry exists that matches the
     * criteria the first one is returned. Returns <code>null</code> if no
     * entry matches.
     *
     * @param severity
     *         the severity to search for. Must be one of <code>FATAL
     *         </code>, <code>ERROR</code>, <code>WARNING</code> or <code>INFO</code>
     * @param entries
     *         list of refactoring status
     * @return the entry that matches the search criteria
     */
    private RefactoringStatusEntry getEntryMatchingSeverity(int severity, List<RefactoringStatusEntry> entries) {
        for (RefactoringStatusEntry entry : entries) {
            if (entry.getSeverity() >= severity)
                return entry;
        }
        return null;
    }

}
