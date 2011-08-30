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
package javax.ws.rs.core;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * TODO javadoc.
 *
 * @author Marek Potociar
 * @since 2.0
 */
public interface RequestHeaders extends Headers {

    /**
     * request-header =
     *         Accept                   ; Section 14.1
     *         Accept-Charset           ; Section 14.2
     *         Accept-Encoding          ; Section 14.3
     *         Accept-Language          ; Section 14.4
     *         Authorization            ; Section 14.8
     *         Expect                   ; Section 14.20
     *         From                     ; Section 14.22
     *         Host                     ; Section 14.23
     *         If-Match                 ; Section 14.24
     *         If-Modified-Since        ; Section 14.25
     *         If-None-Match            ; Section 14.26
     *         If-Range                 ; Section 14.27
     *         If-Unmodified-Since      ; Section 14.28
     *         Max-Forwards             ; Section 14.31
     *         Proxy-Authorization      ; Section 14.34
     *         Range                    ; Section 14.35
     *         Referer                  ; Section 14.36
     *         TE                       ; Section 14.39
     *         User-Agent               ; Section 14.43
     */
    /**
     * TODO javadoc.
     *
     * @param <T> actual request headers builder type.
     * @author Marek Potociar
     * @since 2.0
     */
    public static interface Builder<T extends RequestHeaders.Builder> extends Headers.Builder<T> {

        /**
         * Add acceptable media types.
         *
         * @param types an array of the acceptable media types
         * @return updated request headers builder.
         */
        T accept(MediaType... types);

        /**
         * Add acceptable media types.
         *
         * @param types an array of the acceptable media types
         * @return updated request headers builder.
         */
        T accept(String... types);

        /**
         * Add acceptable languages.
         *
         * @param locales an array of the acceptable languages
         * @return updated request headers builder.
         */
        T acceptLanguage(Locale... locales);

        /**
         * Add acceptable languages.
         *
         * @param locales an array of the acceptable languages
         * @return updated request headers builder.
         */
        T acceptLanguage(String... locales);

        /**
         * Add a cookie to be set.
         *
         * @param cookie to be set.
         * @return updated request headers builder.
         */
        T cookie(Cookie cookie);
    }

    /**
     * Get a list of media types that are acceptable for the response.
     *
     * @return a read-only list of requested response media types sorted according
     * to their q-value, with highest preference first.
     */
    public List<MediaType> getAcceptableMediaTypes();

    /**
     * Get a list of languages that are acceptable for the response.
     *
     * @return a read-only list of acceptable languages sorted according
     * to their q-value, with highest preference first.
     */
    public List<Locale> getAcceptableLanguages();

    /**
     * Get any cookies that accompanied the request.
     * @return a read-only map of cookie name (String) to Cookie.
     * @throws java.lang.IllegalStateException if called outside the scope of a request
     */
    public Map<String, Cookie> getCookies();
}
