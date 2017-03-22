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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.ext.java.client.navigation.service.JavaNavigationService;
import org.eclipse.che.ide.ext.java.shared.dto.model.Member;
import org.eclipse.che.ide.ui.loaders.request.LoaderFactory;
import org.eclipse.che.ide.ui.loaders.request.MessageLoader;

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
     * Shows the overrideable methods of the opened class.
     *
     * @param editorPartPresenter
     *         the active editor
     */
    public void show(EditorPartPresenter editorPartPresenter) {
    }

    // TODO_cemal implement the logic
    @Override
    public void actionPerformed(Member member) {

    }

    @Override
    public void onEscapeClicked() {

    }
}
