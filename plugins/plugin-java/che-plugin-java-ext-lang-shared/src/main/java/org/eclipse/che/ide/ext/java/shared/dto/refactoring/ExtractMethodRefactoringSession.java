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
package org.eclipse.che.ide.ext.java.shared.dto.refactoring;

import org.eclipse.che.ide.ext.java.shared.dto.LinkedModeModel;

/**
 * Created by awesome on 26.03.17.
 */
public interface ExtractMethodRefactoringSession extends RefactoringSession {
    LinkedModeModel getLinkedModeModel();
}
