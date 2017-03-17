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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import org.eclipse.che.ide.ext.java.shared.dto.model.CompilationUnit;
import org.eclipse.che.ide.ui.window.Window;

/**
 * Created by cemal on 12.03.2017.
 */
public class OverridableMethodsImpl extends Window implements OverridableMethods {
    interface OverridableMethodsImplUiBinder extends UiBinder<Widget, OverridableMethodsImpl> {
    }

    private static OverridableMethodsImplUiBinder UI_BINDER = GWT.create(OverridableMethodsImplUiBinder.class);

    // TODO_cemal implement the whole class

    @Override
    public void setMethods(CompilationUnit compilationUnit) {

    }

    @Override
    public void setDelegate(ActionDelegate delegate) {

    }

    @Override
    public void close() {

    }
}
