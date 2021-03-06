/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for local interfaces that we've pulled to toplevel but should be local.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocalContainer {
    /**
     * The path from the current package to the container of this local interface. The elements
     * are names of Ceylon declarations.
     */
    String[] path();
    
    /**
     * The Java class name of the companion class, relative to the nearest Java class container of this
     * local interface (after we've put it back where it belongs in the model).
     */
    String companionClassName() default "";
}
