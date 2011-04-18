/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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
package javax.ws.rs.client;

/**
 * An abstract class providing support for registering and managing a chain
 * of {@link ClientFilter} instances.
 * <p />
 * A single {@link ClientFilter} instance MUST occur at most once in any
 * {@code Filterable} instance, otherwise unexpected results may occur.
 * If it is necessary to add the same type of {@link ClientFilter} more than once
 * to the same {@code Filterable} instance or to more than one {@code Filterable}
 * instance then a new instance of that {@code ClientFilter} MUST be created and
 * used.
 * <p />
 * The instance of this class is constructed with a root {@link ClientHandler}.
 * It is the last client handler invoked in the request processing filter chain
 * and is responsible for the ultimate  handling of the HTTP request and returning
 * the HTTP response represented as a {@link ClientResponse} instance.
 *
 * @author Paul Sandoz
 * @author Marek Potociar
 * @since 2.0
 */
public abstract class Filterable {

    private final ClientHandler root;
    private ClientHandler head;

    /**
     * Construct with a root client handler.
     *
     * @param rootHandler the root client handler responsible for handling the request
     *     and returning a response.
     */
    protected Filterable(ClientHandler rootHandler) {
        this.root = this.head = rootHandler;
    }

    /**
     * Construct from an existing filterable instance.
     * 
     * @param that the filterable instance to copy.
     */
    protected Filterable(Filterable that) {
        this.root = that.root;
        this.head = that.head;
    }

    /**
     * Add a {@link ClientFilter} instance to top of the filter chain.
     * <p />
     * Subsequent calls to {@link #getHeadHandler()} will return the most recently
     * added client filter instance.
     * <p />
     * A single {@link ClientFilter} instance MUST occur at most once in any
     * {@code Filterable} instance, otherwise unexpected results may occur.
     * If it is necessary to add the same type of {@link ClientFilter} more than once
     * to the same {@code Filterable} instance or to more than one {@code Filterable}
     * instance then a new instance of that {@code ClientFilter} MUST be created and
     * used.
     * 
     * @param filter the client filter instance to add to the filter chain.
     */
    public void addFilter(ClientFilter filter) {
        filter.setNext(head);
        this.head = filter;
    }

    /**
     * Remove a client filter instance from the chain.
     * 
     * @param filter the client filter instance to remove.
     * @return {code true} if the filter instance was found in the filter chain
     *     and removed. Otherwise, if the filter instance was not found in the
     *     filter chain, returns {@code false}.
     */
    public boolean removeFilter(ClientFilter filter) {
        // No filters added
        if (head == root) {
            return false;
        }

        // Filter is at the head
        if (head == filter) {
            head = filter.getNext();
            return true;
        }

        ClientFilter e = (ClientFilter) head;
        while (e.getNext() != filter) {
            if (e.getNext() == root) {
                return false;
            }

            e = (ClientFilter) e.getNext();
        }

        e.setNext(filter.getNext());
        return true;
    }

    /**
     * Check if a given filter instance is present in the filter chain.
     *
     * @param filter the filter instance to search for in the filter chain.
     * @return return {@code true} if the filter instance is present, otherwise
     *     {@code false}.
     */
    public boolean isFilterPreset(ClientFilter filter) {
        if (head == root) {
            return false;
        }

        if (head == filter) {
            return true;
        }

        ClientFilter e = (ClientFilter) head;
        while (e.getNext() != filter) {
            if (e.getNext() == root) {
                return false;
            }

            e = (ClientFilter) e.getNext();
        }

        return true;
    }

    /**
     * Remove all filters from the filter chain.
     * <p />
     * After invoking this method, the head {@link ClientHandler} will be pointing to
     * the root client handler instance used to construct this filterable instance.
     *
     * @see #getHeadHandler()
     */
    public void removeAllFilters() {
        this.head = root;
    }

    /**
     * Get the head {@link ClientHandler} instance.
     * <p />
     * If the filter chain is not empty, the head client handler points to the
     * head of the filter chain. Otherwise the root client handler used to construct
     * this filterable instance is returned.
     * 
     * @return the head client handler of the filter chain.
     */
    public ClientHandler getHeadHandler() {
        return head;
    }
}
