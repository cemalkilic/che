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

import com.google.common.base.Predicate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.data.tree.Node;
import org.eclipse.che.ide.api.data.tree.NodeInterceptor;
import org.eclipse.che.ide.ext.java.client.JavaLocalizationConstant;
import org.eclipse.che.ide.ext.java.client.navigation.factory.NodeFactory;
import org.eclipse.che.ide.ext.java.shared.dto.model.CompilationUnit;
import org.eclipse.che.ide.ext.java.shared.dto.model.Type;
import org.eclipse.che.ide.ui.smartTree.*;
import org.eclipse.che.ide.ui.window.Window;

import javax.validation.constraints.NotNull;
import java.util.Collections;

import static com.google.common.collect.Iterables.all;
import static org.eclipse.che.ide.ui.smartTree.SelectionModel.Mode.SINGLE;

/**
 * Created by cemal on 12.03.2017.
 */
@Singleton
public class OverridableMethodsImpl extends Window implements OverridableMethods {
    interface OverridableMethodsImplUiBinder extends UiBinder<Widget, OverridableMethodsImpl> {
    }

    private static OverridableMethodsImplUiBinder UI_BINDER = GWT.create(OverridableMethodsImplUiBinder.class);

    // TODO_cemal implement the whole class

    private final NodeFactory nodeFactory = null;
    private final Tree tree = null;

    private OverridableMethods.ActionDelegate delegate;

    @UiField
    DockLayoutPanel treeContainer;

    @UiField(provided = true)
    final JavaLocalizationConstant locale = null;

    @Inject
    public OverridableMethodsImpl(NodeFactory nodeFactory, JavaLocalizationConstant locale) {

    }

    /** {@inheritDoc} */
    @Override
    public void setMethods(CompilationUnit compilationUnit) {

    }

    /** {@inheritDoc} */
    @Override
    public void close() {
    }

    /** {@inheritDoc} */
    @Override
    public void show() {
    }

    /** {@inheritDoc} */
    @Override
    public void hide() {
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(OverridableMethods.ActionDelegate delegate) {
        this.delegate = delegate;
    }


}
