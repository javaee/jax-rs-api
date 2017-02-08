/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

package javax.ws.rs.sse;

/**
 * Server-sent event subscription representation, which links
 * {@link SseEventSource} and its subscribers.
 * <p>
 * The methods in this interface are intended to be invoked only by their
 * Subscribers; usages in other contexts have undefined effects.
 */
public interface SseSubscription {

    /**
     * Adds the given number {@code n} of items to the current
     * unfulfilled demand for this subscription.  If {@code n} is
     * negative, the Subscriber will receive an {@code onError}
     * signal with an {@link IllegalArgumentException} argument.
     * Otherwise, the Subscriber will receive up to {@code n}
     * additional {@code onNext} invocations (or fewer if
     * terminated).
     *
     * @param n the increment of demand; a value of {@code
     *          Long.MAX_VALUE} may be considered as effectively unbounded
     */
    public void request(long n);

    /**
     * Causes the Subscriber to (eventually) stop receiving
     * messages.  Implementation is best-effort -- additional
     * messages may be received after invoking this method.
     * A cancelled subscription need not ever receive an
     * {@code onComplete} or {@code onError} signal.
     */
    public void cancel();
}
