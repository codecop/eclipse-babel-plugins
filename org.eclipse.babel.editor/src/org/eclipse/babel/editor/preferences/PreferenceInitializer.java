/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.preferences;

import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

/**
 * Initializes default preferences.
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class PreferenceInitializer extends
        AbstractPreferenceInitializer {

    /**
     * Constructor.
     */
    public PreferenceInitializer() {
        super();
    }

    /**
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer
     *      #initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        Preferences prefs = MessagesEditorPlugin.getDefault().getPluginPreferences();

        //General
        prefs.setDefault(MsgEditorPreferences.UNICODE_UNESCAPE_ENABLED, true);
        prefs.setDefault(MsgEditorPreferences.FIELD_TAB_INSERTS, true);
        prefs.setDefault(MsgEditorPreferences.KEY_TREE_HIERARCHICAL, true);
        prefs.setDefault(MsgEditorPreferences.KEY_TREE_EXPANDED, true);
        prefs.setDefault(MsgEditorPreferences.SUPPORT_FRAGMENTS, true);
        prefs.setDefault(MsgEditorPreferences.LOADING_ONLY_FRAGMENT_RESOURCES, false);
        
        //Formatting
        prefs.setDefault(MsgEditorPreferences.UNICODE_ESCAPE_ENABLED, true);
        prefs.setDefault(MsgEditorPreferences.UNICODE_ESCAPE_UPPERCASE, true);
        
        prefs.setDefault(MsgEditorPreferences.SPACES_AROUND_EQUALS_ENABLED, true);
        
        prefs.setDefault(MsgEditorPreferences.GROUP__LEVEL_SEPARATOR, "."); //$NON-NLS-1$
        prefs.setDefault(MsgEditorPreferences.ALIGN_EQUALS_ENABLED, true);
        prefs.setDefault(MsgEditorPreferences.SHOW_SUPPORT_ENABLED, true);
        prefs.setDefault(MsgEditorPreferences.KEY_TREE_HIERARCHICAL, true);
        
        prefs.setDefault(MsgEditorPreferences.GROUP_KEYS_ENABLED, true);
        prefs.setDefault(MsgEditorPreferences.GROUP_LEVEL_DEEP, 1);
        prefs.setDefault(MsgEditorPreferences.GROUP_SEP_BLANK_LINE_COUNT, 1);
        prefs.setDefault(MsgEditorPreferences.GROUP_ALIGN_EQUALS_ENABLED, true);

        prefs.setDefault(MsgEditorPreferences.WRAP_LINE_LENGTH, 80);
        prefs.setDefault(MsgEditorPreferences.WRAP_INDENT_LENGTH, 8);

        prefs.setDefault(MsgEditorPreferences.NEW_LINE_STYLE, 
                MsgEditorPreferences.NEW_LINE_UNIX);

        prefs.setDefault(MsgEditorPreferences.KEEP_EMPTY_FIELDS, false);
        prefs.setDefault(MsgEditorPreferences.SORT_KEYS, true);
        
        // Reporting/Performance
        prefs.setDefault(MsgEditorPreferences.REPORT_MISSING_VALUES, true);
        prefs.setDefault(MsgEditorPreferences.REPORT_DUPL_VALUES, true);
        prefs.setDefault(MsgEditorPreferences.REPORT_SIM_VALUES_WORD_COMPARE, true);
        prefs.setDefault(MsgEditorPreferences.REPORT_SIM_VALUES_PRECISION, 0.75d);
        
        prefs.setDefault(MsgEditorPreferences.EDITOR_TREE_HIDDEN, false);
        
    }

}