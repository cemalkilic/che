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
package org.eclipse.che.ide.ext.java.client.navigation.overrideablemethods;

import com.google.common.base.Optional;
import com.google.gwt.core.client.Scheduler;
import com.google.inject.Inject;
import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.PromiseError;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.editor.OpenEditorCallbackImpl;
import org.eclipse.che.ide.api.editor.document.Document;
import org.eclipse.che.ide.api.editor.text.LinearRange;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.resources.*;
import org.eclipse.che.ide.ext.java.client.navigation.filestructure.FileStructurePresenter;
import org.eclipse.che.ide.ext.java.client.navigation.service.JavaNavigationService;
import org.eclipse.che.ide.ext.java.client.resource.SourceFolderMarker;
import org.eclipse.che.ide.ext.java.client.util.Flags;
import org.eclipse.che.ide.ext.java.client.util.JavaUtil;
import org.eclipse.che.ide.ext.java.shared.JarEntry;
import org.eclipse.che.ide.ext.java.shared.dto.ClassContent;
import org.eclipse.che.ide.ext.java.shared.dto.Region;
import org.eclipse.che.ide.ext.java.shared.dto.model.CompilationUnit;
import org.eclipse.che.ide.ext.java.shared.dto.model.Member;
import org.eclipse.che.ide.ext.java.shared.dto.model.Method;
import org.eclipse.che.ide.resource.Path;
import org.eclipse.che.ide.ui.loaders.request.LoaderFactory;
import org.eclipse.che.ide.ui.loaders.request.MessageLoader;
import org.eclipse.che.ide.util.loging.Log;

/**
 * Created by cemal on 12.03.2017.
 */
@Singleton
public class OverridableMethodsPresenter implements OverridableMethods.ActionDelegate{

    private final OverridableMethods view;
    private final JavaNavigationService javaNavigationService;
    private final AppContext context;
    private final EditorAgent editorAgent;
    private final MessageLoader loader;

    public static boolean OVERRIDABLE_ACTIVE = false;

    private TextEditor activeEditor;
    private int        cursorOffset;
    private Document document;

    @Inject
    public OverridableMethodsPresenter(OverridableMethods view,
                                       JavaNavigationService javaNavigationService,
                                       AppContext context,
                                       EditorAgent editorAgent,
                                       LoaderFactory loaderFactory) {
        this.view = view;
        this.javaNavigationService = javaNavigationService;
        this.context = context;
        this.editorAgent = editorAgent;
        this.loader = loaderFactory.newLoader();
        this.view.setDelegate(this);
    }

    /**
     * Shows the overridable methods of the opened class.
     *
     * @param editorPartPresenter
     *         the active editor
     */
    public void show(EditorPartPresenter editorPartPresenter) {
        loader.show();
        view.setTitle(editorPartPresenter.getEditorInput().getFile().getName());

        if (!(editorPartPresenter instanceof TextEditor)) {
            Log.error(getClass(), "Open Declaration support only TextEditor as editor");
            return;
        }

        activeEditor = ((TextEditor)editorPartPresenter);
        cursorOffset = activeEditor.getCursorOffset();
        VirtualFile file = activeEditor.getEditorInput().getFile();

        if (file instanceof Resource) {
            final Optional<Project> project = ((Resource)file).getRelatedProject();

            final Optional<Resource> srcFolder = ((Resource)file).getParentWithMarker(SourceFolderMarker.ID);

            if (!srcFolder.isPresent()) {
                return;
            }

            final String fqn = JavaUtil.resolveFQN((Container)srcFolder.get(), (Resource)file);


            javaNavigationService.getCompilationUnit(project.get().getLocation(), fqn, true).then(
                    new Operation<CompilationUnit>() {
                        @Override
                        public void apply(CompilationUnit unit) throws OperationException {
                            OVERRIDABLE_ACTIVE = true;
                            view.setMethods(unit);
                            loader.hide();
                            view.show();
                            Log.info(getClass(), "apply icinde");
                        }
                    }).catchError(new Operation<PromiseError>() {
                @Override
                public void apply(PromiseError arg) throws OperationException {
                    Log.error(getClass(), arg.getMessage());
                    loader.hide();
                }
            });

        }
    }

    // TODO_cemal implement actionPerformed
    // when clicked, create the selected method in current file
    /** {@inheritDoc} */
    @Override
    public void actionPerformed(Member member) {
        this.document = activeEditor.getDocument();

        StringBuilder methodDef = new StringBuilder();

        // only methods can be overridable, not fields
        // so if the member is not Method, there is nothing to do
        if(member instanceof Method){
            Method method = (Method) member;

            // add @override annotation to beginning
            methodDef.append("@Override\n");

            int flags = method.getFlags();

            // append the modifier
            if(Flags.isProtected(flags)){
                methodDef.append("protected ");
            } else if(Flags.isPublic(flags)){
                methodDef.append("public ");
            } else if(Flags.isPackageDefault(flags)){
                //nothing
            }

            // append return type
            methodDef.append(method.getReturnType()).append(" ");

            // append method name and bracket
            methodDef.append(method.getElementName());
            methodDef.append("{").append("\n");

            // append the method body as
            // super.<method_name>();
            methodDef.append("super.")
                    .append(method.getElementName())
                    .append("();");

            // append the closing bracket
            methodDef.append("}");


        }
        else{
            // do nothing
        }

        // select the end of document and
        // add the created methodDef.

    }

    @Override
    public void onEscapeClicked() {
        activeEditor.setFocus();
        setCursor(activeEditor, cursorOffset);
    }
    private void setCursor(EditorPartPresenter editor, int offset) {
        if (editor instanceof TextEditor) {
            ((TextEditor)editor).getDocument().setSelectedRange(LinearRange.createWithStart(offset).andLength(0), true);
        }
    }
}
