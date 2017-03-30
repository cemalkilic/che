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

import com.google.inject.ImplementedBy;

import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.ext.java.shared.dto.refactoring.RefactoringStatus;

@ImplementedBy(ExtractMethodViewImpl.class)
interface ExtractMethodView extends View<ExtractMethodView.ActionDelegate> {

    /** Returns new name */
    String getName();

    void setFocus();

    void show();

    void showStatusMessage(RefactoringStatus status);

    void setEnablePreviewButton(boolean isEnable);

    /**
     * Set enable scope of the Accept button.
     *
     * @param isEnable
     *         enable state of scope property
     */
    void setEnableAcceptButton(boolean isEnable);

    /**
     * Show error message into bottom of view.
     *
     * @param status
     *         status of error move operation
     */
    void showErrorMessage(RefactoringStatus status);

    /** Set empty text into error label */
    void clearErrorLabel();

    boolean isDeclareVarArgs();

    boolean isFoldParameters();

    boolean isDeclareStatic();

    String getFilePatterns();

    void hide();


    interface ActionDelegate {
        /** Performs some actions in response to user's clicking on the 'Preview' button. */
        void onPreviewButtonClicked();

        /** Performs some actions in response to user's clicking on the 'Accept' button. */
        void onAcceptButtonClicked();

        /** Performs some actions in response to user's clicking on the 'Cancel' button. */
        void onCancelButtonClicked();

        /** Validates refactored name. */
        void validateName();
    }
}
