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
package org.eclipse.che.ide.ext.java.client.refactoring.extractMethod;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import org.eclipse.che.ide.api.dialogs.DialogFactory;
import org.eclipse.che.ide.api.editor.link.HasLinkedMode;
import org.eclipse.che.ide.api.editor.link.LinkedMode;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.event.FileEvent;
import org.eclipse.che.ide.api.event.FileEvent.FileEventHandler;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.ide.ext.java.client.JavaLocalizationConstant;
import org.eclipse.che.ide.ext.java.client.refactoring.RefactoringUpdater;
import org.eclipse.che.ide.ext.java.client.refactoring.extractMethod.wizard.ExtractMethodPresenter;
import org.eclipse.che.ide.ext.java.client.refactoring.service.RefactoringServiceClient;

/**
 * Created by awesome on 26.03.17.
 */

@Singleton
public class JavaRefactoringExtractMethod implements FileEventHandler {
    private final ExtractMethodPresenter extractMethodPresenter;
    private final RefactoringUpdater       refactoringUpdater;
    private final JavaLocalizationConstant locale;
    private final RefactoringServiceClient refactoringServiceClient;
    private final DtoFactory               dtoFactory;
    private final DialogFactory            dialogFactory;
    private final NotificationManager      notificationManager;

    private boolean       isActiveLinkedEditor;
    private TextEditor    textEditor;
    private LinkedMode    mode;
    private HasLinkedMode linkedEditor;
    private String        newName;

    @Inject
    public JavaRefactoringExtractMethod(ExtractMethodPresenter extractMethodPresenter,
                                        RefactoringUpdater refactoringUpdater,
                                        JavaLocalizationConstant locale,
                                        RefactoringServiceClient refactoringServiceClient,
                                        DtoFactory dtoFactory,
                                        EventBus eventBus,
                                        DialogFactory dialogFactory,
                                        NotificationManager notificationManager) {
        this.extractMethodPresenter = extractMethodPresenter;
        this.refactoringUpdater = refactoringUpdater;
        this.locale = locale;
        this.dialogFactory = dialogFactory;
        this.refactoringServiceClient = refactoringServiceClient;
        this.dtoFactory = dtoFactory;
        this.notificationManager = notificationManager;

        isActiveLinkedEditor = false;

        eventBus.addHandler(FileEvent.TYPE, this);
    }

    public void refactor(final TextEditor textEditorPresenter) {
        if (!(textEditorPresenter instanceof HasLinkedMode)) {
            return;
        }

    }


    @Override
    public void onFileOperation(FileEvent event) {

    }
}
