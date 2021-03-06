/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.jdt.internal.corext;

import org.eclipse.osgi.util.NLS;

public final class CorextMessages extends NLS {

    private static final String BUNDLE_NAME = CorextMessages.class.getName();//$NON-NLS-1$
    public static String Resources_outOfSyncResources;
    public static String Resources_outOfSync;
    public static String Resources_modifiedResources;
    public static String Resources_fileModified;
    public static String JavaDocLocations_migrate_operation;
    public static String JavaDocLocations_error_gettingAttachedJavadoc;
    public static String JavaDocLocations_error_gettingJavadoc;
    public static String JavaDocLocations_error_readXML;
    public static String JavaDocLocations_migratejob_name;
    public static String JavaDocLocations_noAttachedJavadoc;
    public static String JavaDocLocations_noAttachedSource;
    public static String JavaDocLocations_noAttachments;
    public static String JavaDocLocations_noInformation;
    public static String History_error_serialize;
    public static String History_error_read;
    public static String TypeInfoHistory_consistency_check;

    static {
        NLS.initializeMessages(BUNDLE_NAME, CorextMessages.class);
    }

    public static String JavaModelUtil_applyedit_operation;

    private CorextMessages() {
        // Do not instantiate
    }
}
