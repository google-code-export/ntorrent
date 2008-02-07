/*****************************************************************************
 * Java Plug-in Framework (JPF)
 * Copyright (C) 2006-2007 Dmitry Olshansky
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *****************************************************************************/
package org.java.plugin.tools.mocks;

import org.java.plugin.registry.Identity;

/**
 * @version $Id$
 */
public abstract class MockIdentity implements Identity {
    private String id;
    
    /**
     * @see org.java.plugin.registry.Identity#getId()
     */
    public String getId() {
        return id;
    }

    /**
     * @param value the id to set
     * @return this instance
     */
    public MockIdentity setId(final String value) {
        id = value;
        return this;
    }
}
