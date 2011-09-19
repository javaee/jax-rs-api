/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package jaxrs.examples.client.cache;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.ws.rs.client.Configuration;
import javax.ws.rs.client.Feature;

/**
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public class CachingFeature implements Feature {

    @Override
    public void onEnable(Configuration configuration) {
        if (setEnabledFlag(configuration, true)) {
            return;
        }

        final HashMap<String, CacheEntry> cache = new HashMap<String, CacheEntry>();
        final AtomicBoolean flag = new AtomicBoolean(true);
        configuration.register(new CacheEntryLocator(cache, flag), new CacheResponseHandler(cache, flag));
    }

    @Override
    public void onDisable(Configuration configuration) {
        setEnabledFlag(configuration, false);
    }

    private boolean setEnabledFlag(Configuration configuration, boolean value) {
        for (Object provider : configuration.getProviderInstances()) {
            if (provider instanceof CacheEntryLocator) {
                CacheEntryLocator.class.cast(provider).enabledFlag.set(value);
                return true;
            } else if (provider instanceof CacheResponseHandler) {
                CacheResponseHandler.class.cast(provider).enabledFlag.set(value);
                return true;
            }
        }
        return false;
    }
}
