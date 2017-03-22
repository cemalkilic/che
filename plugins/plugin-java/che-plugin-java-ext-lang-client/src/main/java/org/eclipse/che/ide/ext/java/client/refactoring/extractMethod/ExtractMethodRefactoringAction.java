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

import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.api.action.AbstractPerspectiveAction;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.event.ActivePartChangedEvent;
import org.eclipse.che.ide.api.event.ActivePartChangedHandler;
import org.eclipse.che.ide.ext.java.client.JavaLocalizationConstant;

import javax.validation.constraints.NotNull;
import java.util.List;

@Singleton
public class ExtractMethodRefactoringAction extends AbstractPerspectiveAction implements ActivePartChangedHandler{

    @Inject
    public ExtractMethodRefactoringAction(@Nullable List<String> perspectives,
                                          JavaLocalizationConstant locale) {
        super(perspectives, locale.extractMethodRefactoringActionName(), extractMethodRefactoringActionDescription());
    }

    @Override
    public void onActivePartChanged(ActivePartChangedEvent event) {

    }

    @Override
    public void updateInPerspective(@NotNull ActionEvent event) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
