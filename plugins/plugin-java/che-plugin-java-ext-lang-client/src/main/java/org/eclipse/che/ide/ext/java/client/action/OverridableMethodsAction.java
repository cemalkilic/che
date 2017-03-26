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
package org.eclipse.che.ide.ext.java.client.action;

import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.editor.EditorAgent;
import org.eclipse.che.ide.api.filetypes.FileTypeRegistry;
import org.eclipse.che.ide.ext.java.client.JavaLocalizationConstant;
import org.eclipse.che.ide.ext.java.client.JavaResources;
import org.eclipse.che.ide.ext.java.client.navigation.overrideablemethods.OverridableMethodsPresenter;


import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Action for open overridable methods window.
 * Created by cemal on 12.03.2017.
 */
@Singleton
public class OverridableMethodsAction extends JavaEditorAction{

    private final OverridableMethodsPresenter overridableMethodsPresenter;
    private final EditorAgent editorAgent;
    
	@Inject
    public OverridableMethodsAction(OverridableMethodsPresenter overridableMethodsPresenter,
                                    JavaLocalizationConstant locale,
                                    EditorAgent editorAgent,
                                    JavaResources resources,
                                    FileTypeRegistry fileTypeRegistry) {
        super(locale.overridableMethodsActionName(),
                locale.overridableMethodsActionDescription(),
                resources.fileNavigation(),
                editorAgent,
                fileTypeRegistry);

        this.overridableMethodsPresenter = overridableMethodsPresenter;
        this.editorAgent = editorAgent;

    }

    /** {@inheritDoc} */ 
    // TODO_cemal show the OverridableMethodsPresenter dialog
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
