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
import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.api.action.AbstractPerspectiveAction;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.event.ActivePartChangedEvent;
import org.eclipse.che.ide.api.event.ActivePartChangedHandler;
import org.eclipse.che.ide.api.filetypes.FileTypeRegistry;
import org.eclipse.che.ide.ext.java.client.JavaLocalizationConstant;
import org.eclipse.che.ide.ext.java.client.refactoring.rename.JavaRefactoringRename;

//import java.util.List;


@Singleton
public class ExtractMethodRefactoringAction extends AbstractPerspectiveAction implements ActivePartChangedHandler {

    private final EditorAgent           editorAgent;
    private final AppContext            appContext;
    private final FileTypeRegistry      fileTypeRegistry;
    JavaRefactoringExtractMethod javaRefactoringExtractMethod;

    private boolean editorInFocus;

    @Inject
    public ExtractMethodRefactoringAction(EditorAgent editorAgent,
                                          EventBus eventBus,
                                          JavaLocalizationConstant locale,
                                          JavaRefactoringExtractMethod javaRefactoringExtractMethod,
                                          AppContext appContext,
                                          FileTypeRegistry fileTypeRegistry) {
        super(null, locale.extractMethodRefactoringActionName(),locale.extractMethodRefactoringActionDescription());
        this.editorAgent = editorAgent;
        this.javaRefactoringExtractMethod = javaRefactoringExtractMethod;
        this.appContext = appContext;
        this.fileTypeRegistry = fileTypeRegistry;
        this.editorInFocus = false;

        eventBus.addHandler(ActivePartChangedEvent.TYPE, this);
    }

    @Override
    public void onActivePartChanged(ActivePartChangedEvent event) {
        if (editorInFocus) {
            final EditorPartPresenter editorPart = editorAgent.getActiveEditor();
            if (editorPart == null || !(editorPart instanceof TextEditor)) {
                return;
            }

            javaRefactoringExtractMethod.refactor((TextEditor)editorPart);
        }
    }

    @Override
    public void updateInPerspective(ActionEvent event) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
