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
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.data.tree.Node;
import org.eclipse.che.ide.api.data.tree.NodeInterceptor;
import org.eclipse.che.ide.ext.java.client.JavaLocalizationConstant;
import org.eclipse.che.ide.ext.java.client.dto.DtoClientImpls;
import org.eclipse.che.ide.ext.java.client.navigation.factory.NodeFactory;
import org.eclipse.che.ide.ext.java.shared.dto.model.CompilationUnit;
import org.eclipse.che.ide.ext.java.shared.dto.model.Type;
import org.eclipse.che.ide.ui.smartTree.*;
import org.eclipse.che.ide.ui.window.Window;
import org.eclipse.che.ide.util.loging.Log;

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


    private final NodeFactory nodeFactory;
    private final Tree tree;

    private OverridableMethods.ActionDelegate delegate;

    @UiField
    DockLayoutPanel treeContainer;

    @UiField(provided = true)
    final JavaLocalizationConstant locale;

    private Predicate<Node> LEAFS = new Predicate<Node>() {
        @Override
        public boolean apply(Node input) {
            return input.isLeaf();
        }
    };

    @Inject
    public OverridableMethodsImpl(NodeFactory nodeFactory, JavaLocalizationConstant locale) {
        super(false);
        this.nodeFactory = nodeFactory;
        this.locale = locale;
        setWidget(UI_BINDER.createAndBindUi(this));

        // storage for nodes
        NodeStorage storage = new NodeStorage(new NodeUniqueKeyProvider() {
            @Override
            public String getKey(@NotNull Node item) {
                return String.valueOf(item.hashCode());
            }
        });

        // loader for nodes
        NodeLoader loader = new NodeLoader(Collections.<NodeInterceptor>emptySet());

        tree = new Tree(storage, loader);
        tree.setAutoExpand(false);
        tree.getSelectionModel().setSelectionMode(SINGLE);

        KeyboardNavigationHandler handler = new KeyboardNavigationHandler() {
            @Override
            public void onEnter(NativeEvent evt) {
                hide();
            }
        };

        tree.addDomHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                if (all(tree.getSelectionModel().getSelectedNodes(), LEAFS)) {
                    hide();
                }
            }
        }, DoubleClickEvent.getType());

        handler.bind(tree);

        treeContainer.add(tree);
    }


    /** {@inheritDoc} */
    // TODO_cemal implement the listing function
    @Override
    public void setMethods(CompilationUnit compilationUnit) {
        tree.getNodeStorage().clear();
        //Log.info(getClass(), "cu.getTypes() ", compilationUnit.getTypes());
        //Log.info(getClass(), "cu.getTypes().get(0)", compilationUnit.getTypes().get(0));
       // Log.info(getClass(), "cu.getImports", compilationUnit.getImports());

        Log.info(getClass(), "compilationUnit.getSuperTypes(): ", compilationUnit.getSuperTypes());
        for(Type t: compilationUnit.getSuperTypes()){
            CompilationUnit cu = DtoClientImpls.CompilationUnitImpl.fromJsonString(t.toString());
            tree.getNodeStorage().add(nodeFactory.create(t, cu, false, false));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        hide();
        OverridableMethodsPresenter.OVERRIDABLE_ACTIVE = false;
    }

    /** {@inheritDoc} */
    @Override
    public void show() {
        super.show(tree);
        if (!tree.getRootNodes().isEmpty()) {
            tree.getSelectionModel().select(tree.getRootNodes().get(0), false);
        }
        tree.expandAll();
    }

    /** {@inheritDoc} */
    @Override
    public void hide() {
        super.hide();
        delegate.onEscapeClicked();
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(OverridableMethods.ActionDelegate delegate) {
        this.delegate = delegate;
    }


}
