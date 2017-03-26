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

/**
 * Created by awesome on 26.03.17.
 */
public interface ExtractMethodSettings extends RefactoringSession {


    boolean isUpdateQualifiedNames();

    void setUpdateQualifiedNames(boolean update);

    String getFilePatterns();

    void setFilePatterns(String patterns);

    boolean isDeclareStatic();

    void setDeclareStatic(boolean declareStatic);

    boolean foldParemeters();

    void setFoldParemeters(boolean foldParemeters);

    boolean declareVarArgs();

    void setDeclareVarArgs(boolean declareVarArgs);

    int getMachStrategy();

    /**
     *
     * @param strategy must be one of {@link org.eclipse.che.ide.ext.java.shared.dto.refactoring.RenameSettings.MachStrategy} values.
     */
    void setMachStrategy(int strategy);

    //ITextUpdating

    /**
     * This method is used to inform the refactoring object whether references
     * in regular (non JavaDoc) comments and string literals should be updated.
     */
    boolean isUpdateTextualMatches();

    void setUpdateTextualMatches(boolean update);


    enum MachStrategy {
        EXACT(1), EMBEDDED(2), SUFFIX(3);
        private int value;
        MachStrategy(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }
    }
}
