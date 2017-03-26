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

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import org.eclipse.che.api.promises.client.Function;
import org.eclipse.che.api.promises.client.FunctionException;
import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.dialogs.DialogFactory;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.ide.ext.java.client.JavaLocalizationConstant;
import org.eclipse.che.ide.ext.java.client.refactoring.RefactorInfo;
import org.eclipse.che.ide.ext.java.client.refactoring.RefactoringUpdater;
import org.eclipse.che.ide.ext.java.client.refactoring.preview.PreviewPresenter;
import org.eclipse.che.ide.ext.java.client.refactoring.service.RefactoringServiceClient;
import org.eclipse.che.ide.ext.java.shared.dto.refactoring.*;

import static com.google.common.base.Preconditions.checkState;
import static org.eclipse.che.ide.api.notification.StatusNotification.DisplayMode.FLOAT_MODE;
import static org.eclipse.che.ide.api.notification.StatusNotification.Status.FAIL;
import static org.eclipse.che.ide.ext.java.shared.dto.refactoring.RefactoringStatus.ERROR;
import static org.eclipse.che.ide.ext.java.shared.dto.refactoring.RefactoringStatus.FATAL;
import static org.eclipse.che.ide.ext.java.shared.dto.refactoring.RefactoringStatus.INFO;
import static org.eclipse.che.ide.ext.java.shared.dto.refactoring.RefactoringStatus.OK;
import static org.eclipse.che.ide.ext.java.shared.dto.refactoring.RefactoringStatus.WARNING;


/**
 * Created by awesome on 26.03.17.
 */
public class ExtractMethodPresenter implements ExtractMethodView.ActionDelegate {
    private final ExtractMethodView view;
    private final JavaLocalizationConstant locale;
    private final RefactoringUpdater refactoringUpdater;
    private final EditorAgent editorAgent;
    private final NotificationManager notificationManager;
    private final AppContext appContext;
    private final PreviewPresenter previewPresenter;
    private final DtoFactory dtoFactory;
    private final RefactoringServiceClient refactoringService;
    private final DialogFactory dialogFactory;
    private final EventBus eventBus;

    private ExtractMethodRefactoringSession extractMethodRefactoringSession;
    private RefactorInfo refactorInfo;

    @Inject
    public ExtractMethodPresenter(ExtractMethodView view,
                                  JavaLocalizationConstant locale,
                                  EditorAgent editorAgent,
                                  RefactoringUpdater refactoringUpdater,
                                  AppContext appContext,
                                  NotificationManager notificationManager,
                                  PreviewPresenter previewPresenter,
                                  RefactoringServiceClient refactorService,
                                  DtoFactory dtoFactory,
                                  DialogFactory dialogFactory,
                                  EventBus eventBus) {
        this.view = view;
        this.locale = locale;
        this.refactoringUpdater = refactoringUpdater;
        this.editorAgent = editorAgent;
        this.notificationManager = notificationManager;
        this.eventBus = eventBus;
        this.view.setDelegate(this);
        this.appContext = appContext;
        this.previewPresenter = previewPresenter;
        this.refactoringService = refactorService;
        this.dtoFactory = dtoFactory;
        this.dialogFactory = dialogFactory;
    }

    /**
     * Show Rename window with the special information.
     *
     * @param refactorInfo
     *         information about the rename operation
     */
    public void show(RefactorInfo refactorInfo) {
        this.refactorInfo = refactorInfo;
        final CreateRenameRefactoring createRenameRefactoring = createRenameRefactoringDto(refactorInfo);

        Promise<RenameRefactoringSession> createRenamePromise = refactorService.createRenameRefactoring(createRenameRefactoring);
        createRenamePromise.then(new Operation<RenameRefactoringSession>() {
            @Override
            public void apply(RenameRefactoringSession session) throws OperationException {
                show(session);
            }
        }).catchError(new Operation<PromiseError>() {
            @Override
            public void apply(PromiseError arg) throws OperationException {
                notificationManager.notify(locale.failedToRename(), arg.getMessage(), FAIL, FLOAT_MODE);
            }
        });
    }

    private CreateRenameRefactoring createRenameRefactoringDto(RefactorInfo refactorInfo) {
    }

    @Override
    public void onPreviewButtonClicked() {
        showPreview();
    }

    @Override
    public void onAcceptButtonClicked() {
        applyChanges();
    }

    @Override
    public void onCancelButtonClicked() {
        setEditorFocus();
    }

    @Override
    public void validateName() {
        ValidateNewName validateNewName = dtoFactory.createDto(ValidateNewName.class);
        validateNewName.setSessionId(extractMethodRefactoringSession.getSessionId());
        validateNewName.setNewName(view.getName());

        refactoringService.validateNewName(validateNewName).then(new Operation<RefactoringStatus>() {
            @Override
            public void apply(RefactoringStatus arg) throws OperationException {
                switch (arg.getSeverity()) {
                    case OK:
                        view.setEnableAcceptButton(true);
                        view.setEnablePreviewButton(true);
                        view.clearErrorLabel();
                        break;
                    case INFO:
                        view.setEnableAcceptButton(true);
                        view.setEnablePreviewButton(true);
                        view.showStatusMessage(arg);
                        break;
                    default:
                        view.setEnableAcceptButton(false);
                        view.setEnablePreviewButton(false);
                        view.showErrorMessage(arg);
                        break;
                }
            }
        }).catchError(new Operation<PromiseError>() {
            @Override
            public void apply(PromiseError arg) throws OperationException {
                notificationManager.notify(locale.failedToExtractMethod(), arg.getMessage(), FAIL, FLOAT_MODE);
            }
        });
    }

    private void prepareWizard() {
        view.clearErrorLabel();
        view.setEnableAcceptButton(false);
        view.setEnablePreviewButton(false);
    }

    private void showPreview() {
        RefactoringSession session = dtoFactory.createDto(RefactoringSession.class);
        session.setSessionId(extractMethodRefactoringSession.getSessionId());

        prepareRenameChanges(session).then(new Operation<ChangeCreationResult>() {
            @Override
            public void apply(ChangeCreationResult arg) throws OperationException {
                if (arg.isCanShowPreviewPage() || arg.getStatus().getSeverity() <= 3) {
                    previewPresenter.show(renameRefactoringSession.getSessionId(), refactorInfo);
                    previewPresenter.setTitle(locale.renameItemTitle());
                    view.hide();
                } else {
                    view.showErrorMessage(arg.getStatus());
                }
            }
        }).catchError(new Operation<PromiseError>() {
            @Override
            public void apply(PromiseError arg) throws OperationException {
                notificationManager.notify(locale.failedToRename(), arg.getMessage(), FAIL, FLOAT_MODE);
            }
        });
    }

    private void applyChanges() {
        final RefactoringSession session = dtoFactory.createDto(RefactoringSession.class);
        session.setSessionId(renameRefactoringSession.getSessionId());

        prepareRenameChanges(session).then(new Operation<ChangeCreationResult>() {
            @Override
            public void apply(ChangeCreationResult arg) throws OperationException {
                int severityCode = arg.getStatus().getSeverity();

                switch (severityCode) {
                    case WARNING:
                    case ERROR:
                        showWarningDialog(session, arg);
                        break;
                    case FATAL:
                        if (!arg.isCanShowPreviewPage()) {
                            view.showErrorMessage(arg.getStatus());
                        }
                        break;
                    default:
                        applyRefactoring(session);
                }
            }
        }).catchError(new Operation<PromiseError>() {
            @Override
            public void apply(PromiseError arg) throws OperationException {
                notificationManager.notify(locale.failedToRename(), arg.getMessage(), FAIL, FLOAT_MODE);
            }
        });
    }

    private Promise<ChangeCreationResult> prepareExtractMethodChanges(final RefactoringSession session) {
        ExtractMethodSettings extractMethodSettings = createExtractMethodSettingsDto(session);

        return refactoringService.setExtractMethodSettings(extractMethodSettings).thenPromise(new Function<Void, Promise<ChangeCreationResult>>() {
            @Override
            public Promise<ChangeCreationResult> apply(Void arg) throws FunctionException {
                return refactorService.createChange(session);
            }
        });
    }

    private ExtractMethodSettings createExtractMethodSettingsDto(RefactoringSession session) {
        ExtractMethodSettings extractMethodSettings = dtoFactory.createDto(ExtractMethodSettings.class);
        extractMethodSettings.setSessionId(session.getSessionId());
        extractMethodSettings.setDeclareStatic(view.isDeclareStatic());
        extractMethodSettings.setFoldParemeters(view.isFoldParameters());
        extractMethodSettings.setDeclareVarArgs(view.isDeclareVarArgs());

        return extractMethodSettings;
    }
}