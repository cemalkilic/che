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

import org.eclipse.che.dto.shared.DTO;

/**
 * Created by awesome on 26.03.17.
 */
@DTO
public interface ExtractMethodSettings extends RefactoringSession {

    boolean isUpdateQualifiedNames();

    void setUpdateQualifiedNames(boolean update);

    String getFilePatterns();

    void setFilePatterns(String patterns);

    boolean isDeclareStatic();

    void setDeclareStatic(boolean declareStatic);

    boolean isFoldParameters();

    void setDeclareVarArgs(boolean declareVarArgs);

    void setFoldParameters(boolean foldParameters);

    boolean isDeclareVarArgs();


    //ITextUpdating

    /**
     * This method is used to inform the refactoring object whether references
     * in regular (non JavaDoc) comments and string literals should be updated.
     */
    boolean isUpdateTextualMatches();

    void setUpdateTextualMatches(boolean update);

}
