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
import org.eclipse.che.ide.api.editor.text.LinearRange;
import org.eclipse.che.ide.api.editor.texteditor.TextEditor;
import org.eclipse.che.ide.api.resources.*;
import org.eclipse.che.ide.ext.java.client.navigation.filestructure.FileStructurePresenter;
import org.eclipse.che.ide.ext.java.client.navigation.service.JavaNavigationService;
import org.eclipse.che.ide.ext.java.client.resource.SourceFolderMarker;
import org.eclipse.che.ide.ext.java.client.util.JavaUtil;
import org.eclipse.che.ide.ext.java.shared.JarEntry;
import org.eclipse.che.ide.ext.java.shared.dto.ClassContent;
import org.eclipse.che.ide.ext.java.shared.dto.Region;
import org.eclipse.che.ide.ext.java.shared.dto.model.CompilationUnit;
import org.eclipse.che.ide.ext.java.shared.dto.model.Member;
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

    private TextEditor activeEditor;
    private int        cursorOffset;

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
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final Member member) {
    }

    @Override
    public void onEscapeClicked() {
    }

    private void setCursorPosition(Region region) {
    }

    private void setCursor(EditorPartPresenter editor, int offset) {
    }
}
