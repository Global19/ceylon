/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tool;

import java.lang.reflect.Method;

/**
 * Model of a command line argument accepted by a plugin
 * @author tom
 * @param <A>
 */
public class ArgumentModel<A> {
    private ToolModel<?> toolModel;
    private Method setter;
    private Class<A> type;
    private String argumentName;
    private Multiplicity multiplicity;
    private OptionModel<A> option;
    private ArgumentParser<A> parser;
    
    public ToolModel<?> getToolModel() {
        return toolModel;
    }
    public void setToolModel(ToolModel<?> toolModel) {
        this.toolModel = toolModel;
    }
    public Multiplicity getMultiplicity() {
        return multiplicity;
    }
    public void setMultiplicity(Multiplicity multiplicity) {
        this.multiplicity = multiplicity;
    }
    public Method getSetter() {
        return setter;
    }
    public void setSetter(Method setter) {
        this.setter = setter;
    }
    
    public ArgumentParser<A> getParser() {
        return parser;
    }
    public void setParser(ArgumentParser<A> parser) {
        this.parser = parser;
    }
    public Class<A> getType() {
        return type;
    }

    public void setType(Class<A> type) {
        this.type = type;
    }
    public String getName() {
        return argumentName;
    }
    public void setName(String name) {
        this.argumentName = name;
    }
    public OptionModel<A> getOption() {
        return option;
    }
    public void setOption(OptionModel<A> option) {
        this.option = option;
    }
    public String toString() {
        return option != null ? option + "'s argument" : "argument " + argumentName;
    }
    
}
