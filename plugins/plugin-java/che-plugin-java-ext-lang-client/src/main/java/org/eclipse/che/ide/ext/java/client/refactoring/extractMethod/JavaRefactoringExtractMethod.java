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

import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.dialogs.DialogFactory;
import org.eclipse.che.ide.api.editor.link.HasLinkedMode;
import org.eclipse.che.ide.api.editor.link.LinkedMode;
import org.eclipse.che.ide.api.editor.link.LinkedModel;
import org.eclipse.che.ide.api.editor.link.LinkedModelGroup;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.event.FileEvent;
import org.eclipse.che.ide.api.event.FileEvent.FileEventHandler;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.ide.ext.java.client.JavaLocalizationConstant;
import org.eclipse.che.ide.ext.java.client.refactoring.RefactoringUpdater;
import org.eclipse.che.ide.ext.java.client.refactoring.extractMethod.wizard.ExtractMethodPresenter;
import org.eclipse.che.ide.ext.java.client.refactoring.service.RefactoringServiceClient;
import org.eclipse.che.ide.ext.java.jdt.text.Document;
import org.eclipse.che.ide.ext.java.shared.dto.LinkedModeModel;
import org.eclipse.che.ide.ext.java.shared.dto.refactoring.CreateExtractMethodRefactoring;
import org.eclipse.che.ide.ext.java.shared.dto.refactoring.ExtractMethodRefactoringSession;

import static org.eclipse.che.ide.api.event.FileEvent.FileOperation.CLOSE;

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

//        if (isActiveLinkedEditor) {
//            createExtractMethodRefactorSession();
//        } else {
//            textEditor = textEditorPresenter;
//
//            createLinkedExtractMethodRefactorSessin();
//        }
//
//        isActiveLinkedEditor = !isActiveLinkedEditor;

        linkedEditor = (HasLinkedMode)textEditorPresenter;
        textEditorPresenter.setFocus();

    }

//    private void createExtractMethodSession() {
//        final CreateExtractMethodRefactoring refactoringSession = createExtractMethodDto(textEditor, false);
//
//        Promise<ExtractMethodRefactoringSession> createExtractMethodPromise = refactoringServiceClient.createExtractMethodRefactoring(refactoringSession);
//        createExtractMethodPromise.then(new Operation<ExtractMethodRefactoringSession>() {
//            @Override
//            public void apply(ExtractMethodRefactoringSession session) throws OperationException {
//                extractMethodPresenter.show(session);
//                if (mode != null) {
//                    mode.exitLinkedMode(false);
//                }
//            }
//        }).catchError(new Operation<PromiseError>() {
//            @Override
//            public void apply(PromiseError arg) throws OperationException {
//                showError();
//            }
//        });
//    }

    private void showError() {
        dialogFactory.createMessageDialog("Extract Method", locale.failedToExtractMethod(), null).show();
        if (mode != null)
            mode.exitLinkedMode(false);
    }

//    private void createLinkedExtractMethodSession() {
//        final CreateExtractMethodRefactoring refactoringSession = createExtractMethodRefactoringDto(textEditor, true);
//
//        Promise<ExtractMethodRefactoringSession> createExtractMethodPromise = refactoringServiceClient.createExtractMethodRefactoring(refactoringSession);
//
//        createExtractMethodPromise.then(new Operation<ExtractMethodRefactoringSession>() {
//            @Override
//            public void apply(ExtractMethodRefactoringSession arg) throws OperationException {
//                activateLinkedModeIntoEditor(arg, textEditor.getDocument());
//            }
//        }).catchError(new Operation<PromiseError>() {
//            @Override
//            public void apply(PromiseError arg) throws OperationException {
//                isActiveLinkedEditor = false;
//                showError();
//            }
//        });
//    }


    @Override
    public void onFileOperation(FileEvent event) {
        if (event.getOperationType() == CLOSE && textEditor != null && textEditor.getDocument() != null &&
                textEditor.getDocument().getFile().getLocation().equals(event.getFile().getLocation())) {
            isActiveLinkedEditor = false;
        }
    }

    public boolean isActiveLinkedEditor() {
        return isActiveLinkedEditor;
    }

//    private void activateLinkedModeIntoEditor(final ExtractMethodRefactoringSession session, final Document document) {
//        mode = linkedEditor.getLinkedMode();
//        LinkedModel model = linkedEditor.createLinkedModel();
//        LinkedModeModel linkedModeModel = session.getLinkedModeModel();
//        List<LinkedModelGroup> groups = new ArrayList<>();
//        for (LinkedPositionGroup positionGroup : linkedModeModel.getGroups()) {
//            LinkedModelGroup group = linkedEditor.createLinkedGroup();
//            LinkedData data = positionGroup.getData();
//            if (data != null) {
//                LinkedModelData modelData = linkedEditor.createLinkedModelData();
//                modelData.setType("link");
//                modelData.setValues(data.getValues());
//                group.setData(modelData);
//            }
//            List<Position> positions = new ArrayList<>();
//            for (Region region : positionGroup.getPositions()) {
//                positions.add(new Position(region.getOffset(), region.getLength()));
//            }
//            group.setPositions(positions);
//            groups.add(group);
//        }
//        model.setGroups(groups);
//        disableAutoSave();
//
//        mode.enterLinkedMode(model);
//
//        mode.addListener(new LinkedMode.LinkedModeListener() {
//            @Override
//            public void onLinkedModeExited(boolean successful, int start, int end) {
//                boolean isSuccessful = false;
//                try {
//                    if (successful) {
//                        isSuccessful = true;
//                        newName = document.getContentRange(start, end - start);
//                        performRename(session);
//                    }
//                } finally {
//                    mode.removeListener(this);
//                    isActiveLinkedEditor = false;
//                    if (!isSuccessful && linkedEditor instanceof EditorWithAutoSave) {
//                        ((EditorWithAutoSave)linkedEditor).enableAutoSave();
//                    }
//                }
//            }
//        });
//    }
}
