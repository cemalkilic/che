package org.eclipse.che.ide.ext.java.client.navigation.overrideablemethods;

import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.ext.java.shared.dto.model.CompilationUnit;
import org.eclipse.che.ide.ext.java.shared.dto.model.Member;

/**
 * Created by cemal on 12.03.2017.
 */
public interface OverridableMethods extends View<OverridableMethods.ActionDelegate> {


    /**
     * Set a title of the navigation window.
     *
     * @param title
     *         new window's title
     */
    void setTitle(String title);

    /**
     * Show overrideable methods of the opened class.
     *
     * @param compilationUnit
     *         compilation unit of the current source file
     */
    void setMethods(CompilationUnit compilationUnit);

    /** Closes window. */
    void close();

    /**
     * Show window.
     */
    void show();

    interface ActionDelegate {
        /**
         * Closes window and select a region of the active element in the editor.
         *
         * @param member
         *         selected member
         */
        void actionPerformed(Member member);

        /**
         * Performs some actions(e.c. set cursor in previous position in active editor) when user click on escape button to close
         * file structure dialog.
         */
        void onEscapeClicked();
    }
}
