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
package org.eclipse.babel.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import org.eclipse.babel.core.util.BabelUtils;


/**
 * A convenience base class for observable beans.
 * This class follows the conventions and recommendations as described
 * in the <a href="http://java.sun.com/products/javabeans/docs/spec.html">SUN
 * JavaBean specifications</a>.
 *
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public abstract class Model implements Serializable {
    /** Handles <code>PropertyChangeListeners</code>. */
    private transient PropertyChangeSupport changeSupport;

    /**
     * Adds a PropertyChangeListener to the listener list.  The listener is 
     * registered for all properties of this class.<p>
     * Adding a <code>null</code> listener has no effect.
     * 
     * @param listener the PropertyChangeListener to be added
     */
    public final synchronized void addPropertyChangeListener(
            final PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list. This method
     * should be used to remove PropertyChangeListeners that were registered
     * for all bound properties of this class.<p>
     * Removing a <code>null</code> listener has no effect.
     *
     * @param listener the PropertyChangeListener to be removed
     */
    public final synchronized void removePropertyChangeListener(
            final PropertyChangeListener listener) {
        if (listener == null || changeSupport == null) {
            return;
        }
        changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Adds a PropertyChangeListener to the listener list for a specific
     * property.<p>
     *
     * Adding a <code>null</code> listener has no effect.
     *
     * @param propertyName one of the property names for this implementation.
     * @param listener     the PropertyChangeListener to be added
     */
    public final synchronized void addPropertyChangeListener(
            final String propertyName, final PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list for a specific
     * property. <p>
     * Removing a <code>null</code> listener has no effect.
     * 
     * @param propertyName      a valid property name
     * @param listener          the PropertyChangeListener to be removed
     */
    public final synchronized void removePropertyChangeListener(
            final String propertyName, final PropertyChangeListener listener) {
        if (listener == null || changeSupport == null) {
            return;
        }
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }


    /**
     * Returns an array of all the property change listeners registered on 
     * this component.
     *
     * @return all of this component's <code>PropertyChangeListener</code>s
     *         or an empty array if no property change
     *         listeners are currently registered
     */
    public final synchronized
    		PropertyChangeListener[] getPropertyChangeListeners() {
        if (changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return changeSupport.getPropertyChangeListeners();
    }


    /**
     * Returns an array of all the listeners which have been associated
     * with the named property.
     *
     * @param propertyName   the name of the property to lookup listeners
     * @return all of the <code>PropertyChangeListeners</code> associated with
     *         the named property or an empty array if no listeners have
     *         been added
     */
    public final synchronized
    		PropertyChangeListener[] getPropertyChangeListeners(
    				String propertyName) {
        if (changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return changeSupport.getPropertyChangeListeners(propertyName);
    }

    /**
     * Support for reporting bound property changes for Object properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(
            final String propertyName,
            final Object oldValue,
            final Object newValue) {
        if (changeSupport == null) {
            return;
        }
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Support for reporting bound property changes for boolean properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(
            final String propertyName,
            final boolean oldValue,
            final boolean newValue) {
        if (changeSupport == null) {
            return;
        }
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Support for reporting bound property changes for events.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param event the event that triggered the change
     */
    protected final void firePropertyChange(
            final PropertyChangeEvent event) {
        if (changeSupport == null) {
            return;
        }
        changeSupport.firePropertyChange(event);
    }
    
    /**
     * Support for reporting bound property changes for integer properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(
            final String propertyName,
            final double oldValue,
            final double newValue) {
        firePropertyChange(
                propertyName, new Double(oldValue), new Double(newValue));
    }

    /**
     * Support for reporting bound property changes for integer properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(
            final String propertyName,
            final float oldValue,
            final float newValue) {
        firePropertyChange(
                propertyName, new Float(oldValue), new Float(newValue));
    }

    /**
     * Support for reporting bound property changes for integer properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(
            final String propertyName,
            final int oldValue,
            final int newValue) {
        if (changeSupport == null) {
            return;
        }
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Support for reporting bound property changes for integer properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName      the property whose value has changed
     * @param oldValue          the property's previous value
     * @param newValue          the property's new value
     */
    protected final void firePropertyChange(
            final String propertyName,
            final long oldValue,
            final long newValue) {
        firePropertyChange(
                propertyName, new Long(oldValue), new Long(newValue));
    }

    /**
     * Indicates that an arbitrary set of bound properties have changed.
     * Sends a PropertyChangeEvent with property name, old and new value
     * set <code>null</code> to any registered PropertyChangeListeners.
     *
     * @see java.beans.PropertyChangeEvent
     */
    protected final void fireMultiplePropertiesChanged() {
        firePropertyChange(null, null, null);
    }

    /**
     * Checks and answers if the two objects are both <code>null</code>
     * or equal.
     *
     * @param o1        the first object to compare
     * @param o2        the second object to compare
     * @return boolean  true if and only if both objects are <code>null</code>
     *    or equal
     */
    protected final boolean equals(final Object o1, final Object o2) {
        return BabelUtils.equals(o1, o2);
    }
    /**
     * Checks and answers if the two booleans are the same.
     *
     * @param b1        the first boolean to compare
     * @param b2        the second boolean to compare
     * @return boolean  true if and only if both boolean are the same
     */
    protected final boolean equals(final boolean b1, final boolean b2) {
        return b1 == b2;
    }

    protected final boolean isChangedFromNull(PropertyChangeEvent event) {
        return (event.getOldValue() == null && event.getNewValue() != null);
    }
    protected final boolean isChangedToNull(PropertyChangeEvent event) {
        return (event.getOldValue() != null && event.getNewValue() == null);
    }
}
