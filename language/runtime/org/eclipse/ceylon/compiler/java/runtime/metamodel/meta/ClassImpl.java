/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.meta;

import java.util.ArrayList;

import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.language.ObjectArrayIterable;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.Sequenced;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.ConstructorDispatch;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.DefaultValueProvider;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.CallableConstructorDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassWithInitializerDeclarationConstructor;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.FunctionOrValueDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ValueConstructorDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;

import ceylon.language.Array;
import ceylon.language.AssertionError;
import ceylon.language.Entry;
import ceylon.language.Iterable;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.model.CallableConstructor;
import ceylon.language.meta.model.FunctionModel;
import ceylon.language.meta.model.IncompatibleTypeException;
import ceylon.language.meta.model.InvocationException;
import ceylon.language.meta.model.ValueConstructor;
import ceylon.language.meta.model.ValueModel;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
    })
public class ClassImpl<Type, Arguments extends Sequential<? extends Object>> 
    extends ClassOrInterfaceImpl<Type>
    implements ceylon.language.meta.model.Class<Type, Arguments>, DefaultValueProvider {

    @Ignore
    public final TypeDescriptor $reifiedArguments;
    private Object instance;
    private final ceylon.language.meta.model.Type<?> container;
    private volatile boolean initialized = false;
    private ConstructorDispatch<Type,Arguments> dispatch = null;
    
    // FIXME: get rid of duplicate instantiations of AppliedClassType when the type in question has no type parameters
    public ClassImpl(@Ignore TypeDescriptor $reifiedType, 
                        @Ignore TypeDescriptor $reifiedArguments,
                        org.eclipse.ceylon.model.typechecker.model.Type producedType, 
                        ceylon.language.meta.model.Type<?> container, Object instance) {
        super($reifiedType, producedType);
        this.$reifiedArguments = $reifiedArguments;
        this.container = container;
        this.instance = instance;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::ClassDeclaration")
    public ceylon.language.meta.declaration.ClassDeclaration getDeclaration() {
        return (ceylon.language.meta.declaration.ClassDeclaration) super.getDeclaration();
    }

    protected boolean hasConstructors() {
        org.eclipse.ceylon.model.typechecker.model.Class decl = (org.eclipse.ceylon.model.typechecker.model.Class) producedType.getDeclaration();
        return decl.hasConstructors();
    }
    
    protected boolean hasEnumerated() {
        org.eclipse.ceylon.model.typechecker.model.Class decl = (org.eclipse.ceylon.model.typechecker.model.Class) producedType.getDeclaration();
        return decl.hasEnumerated();
    }
    
    void checkConstructor() {
        ClassDeclarationImpl classDeclaration = (ClassDeclarationImpl)declaration;
        if(classDeclaration.getAbstract())
            throw new InvocationException("Abstract class cannot be instantiated");
        if(classDeclaration.getAnonymous())
            throw new InvocationException("Object class cannot be instantiated");
        if (producedType.getDeclaration().isAbstraction())
            throw new InvocationException("Class with overloaded constructors cannot be instantiated");
    }
    
    ConstructorDispatch<Type, Arguments> getDispatch() {
        if (!initialized) {
            synchronized(this) {
                if (!initialized) {
                    checkConstructor();
                    Reference reference;
                    if (!hasConstructors() && !hasEnumerated()) {
                        reference = producedType;
                    } else {
                        reference = ((org.eclipse.ceylon.model.typechecker.model.Class)declaration.declaration).getDefaultConstructor().appliedReference(producedType, null);
                    }
                    this.dispatch = new ConstructorDispatch<Type,Arguments>(
                            reference,
                            this, null,
                            ((Class)producedType.getDeclaration()).getFirstParameterList().getParameters(), 
                            instance);
                    this.initialized = true;
                }
            }
        }
        return dispatch;
    }
    
    @Ignore
    @Override
    public Type $call$() {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$call$();
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }

    @Ignore
    @Override
    public Type $call$(Object arg0) {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$call$(arg0);
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }

    @Ignore
    @Override
    public Type $call$(Object arg0, Object arg1) {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$call$(arg0, arg1);
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }

    @Ignore
    @Override
    public Type $call$(Object arg0, Object arg1, Object arg2) {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$call$(arg0, arg1, arg2);
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }

    @Ignore
    @Override
    public Type $call$(Object... args) {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$call$((Object[])args);
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }
    
    @Override
    @Ignore
    public Type $callvariadic$() {
        return $call$();
    }
    
    @Override
    @Ignore
    public Type $callvariadic$(Sequential<?> varargs) {
        return $call$(varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0,
            Sequential<?> varargs) {
        return $call$(arg0, varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0,
            Object arg1, Sequential<?> varargs) {
        return $call$(arg0, arg1, varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0,
            Object arg1, Object arg2, Sequential<?> varargs) {
        return $call$(arg0, arg1, arg2, varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object... argsAndVarargs) {
        return $call$((Object[])argsAndVarargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0) {
        return $call$(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0, Object arg1) {
        return $call$(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2) {
        return $call$(arg0, arg1, arg2, empty_.get_());
    }

    @Ignore
    @Override
    public short $getVariadicParameterIndex$() {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$getVariadicParameterIndex$();
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }

    @Ignore
    @Override
    public Type apply(){
        return apply(empty_.get_());
    }

    
    @Override
    public int hashCode() {
        int result = 1;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        result = 37 * result + (instance == null ? 0 : instance.hashCode());
        result = 37 * result + getDeclaration().hashCode();
        result = 37 * result + getTypeArgumentWithVariances().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ClassImpl == false)
            return false;
        ClassImpl<?,?> other = (ClassImpl<?,?>) obj;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        return getDeclaration().equals(other.getDeclaration())
                && Util.eq(instance, other.instance)
                && getTypeArgumentWithVariances().equals(other.getTypeArgumentWithVariances());
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>|ceylon.language::Null")
    public ceylon.language.meta.model.Type<?> getContainer(){
        return container;
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(ClassImpl.class, $reifiedType, $reifiedArguments);
    }
    
    @Override
    @TypeInfo("ceylon.language.meta.model::CallableConstructor<Type,Arguments>|ceylon.language::Null")
    public CallableConstructor<Type, Arguments> getDefaultConstructor() {
        if (hasConstructors() || hasEnumerated()) {
            Object ctor = getConstructor($reifiedArguments, "");
            if (ctor instanceof CallableConstructor) {
                return ((CallableConstructor<Type, Arguments>)ctor);
            } else {
                return null;
            }
        } else {
            return new ClassInitializerConstructor<>(this);
        }
    }
    
    @Override
    @TypeParameters(@TypeParameter(value="Arguments", satisfies="ceylon.language::Sequential<ceylon.language::Anything>"))
    @TypeInfo("ceylon.language.meta.model::CallableConstructor<Type,Arguments>|ceylon.language.meta.model::ValueConstructor<Type>|ceylon.language::Null")
    public <Arguments extends Sequential<? extends Object>> java.lang.Object getConstructor(
            @Ignore
            TypeDescriptor $reified$Arguments,
            @Name("name")
            String name) {
        checkInit();
        final ceylon.language.meta.declaration.Declaration ctor = ((ClassDeclarationImpl)declaration).getConstructorDeclaration(name);
        if (ctor instanceof CallableConstructorDeclaration) {
            if (ctor instanceof CallableConstructorDeclarationImpl && 
                    ((CallableConstructorDeclarationImpl)ctor).declaration.isShared()
                    || ctor instanceof ClassWithInitializerDeclarationConstructor) {
                return getDeclaredConstructor($reified$Arguments, name); 
            } else {
                return null;
            }
        }
        else if (ctor instanceof ValueConstructorDeclaration) {
            if (((ValueConstructorDeclarationImpl)ctor).declaration.isShared()) {
                return getDeclaredConstructor($reified$Arguments, name);
            } else {
                return null;
            }
        }
        return null;
    }
    
    @Override
    @TypeParameters(@TypeParameter(value="Arguments", satisfies="ceylon.language::Sequential<ceylon.language::Anything>"))
    @TypeInfo("ceylon.language.meta.model::CallableConstructor<Type,Arguments>|ceylon.language.meta.model::ValueConstructor<Type>|ceylon.language::Null")
    public <Arguments extends Sequential<? extends Object>> java.lang.Object getDeclaredConstructor(
            @Ignore
            TypeDescriptor $reified$Arguments,
            @Name("name")
            String name) {
        try {
            return getDeclaredConstructorInternal($reified$Arguments, name);
        }
        catch (IncompatibleTypeException ite) {
            return null;
        }
    }
    
    @Ignore
    public <Arguments extends Sequential<? extends Object>> java.lang.Object getDeclaredConstructorInternal(
            TypeDescriptor $reified$Arguments,
            String name) {
        checkInit();
        final ceylon.language.meta.declaration.Declaration ctor = ((ClassDeclarationImpl)declaration).getConstructorDeclaration(name);
        if(ctor == null)
            return null;
        if (ctor instanceof CallableConstructorDeclaration) {
            FunctionOrValueDeclarationImpl ctorImpl = (FunctionOrValueDeclarationImpl)ctor;
            org.eclipse.ceylon.model.typechecker.model.Type reifiedArguments = Metamodel.getProducedType($reified$Arguments);
            org.eclipse.ceylon.model.typechecker.model.Type argumentsType = Metamodel.getProducedTypeForArguments(
                    declaration.declaration.getUnit(), 
                    (Functional) ctorImpl.declaration, 
                    ctorImpl.declaration.appliedReference(producedType, null));
            if (ctor instanceof ClassWithInitializerDeclarationConstructor) {
                TypeDescriptor actualReifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.declaration.getUnit(), (Functional)((ClassWithInitializerDeclarationConstructor)ctor).declaration, this.producedType);
                Metamodel.checkReifiedTypeArgument("getDeclaredConstructor", "CallableConstructor<$1,$2>",
                        //        // this line is bullshit since it's always true, but otherwise we can't substitute the error message above :(
                                Variance.OUT, this.producedType, $reifiedType,
                                Variance.IN, Metamodel.getProducedType(actualReifiedArguments), $reified$Arguments);
                return new ClassInitializerConstructor<>(this);
            }
            else {
                CallableConstructorDeclarationImpl callableCtor = (CallableConstructorDeclarationImpl)ctor;
    //            org.eclipse.ceylon.model.typechecker.model.Type constructorType = callableCtor.constructor.appliedType(this.producedType, Collections.<org.eclipse.ceylon.model.typechecker.model.Type>emptyList());
    
                //return new AppliedConstructor<Type,Args>(this.$reifiedType, actualReifiedArguments, this, constructorType, ctor, this.instance);
                //Reference reference = ((Function)callableCtor.declaration).getReference();
                Reference reference;
                if (callableCtor.declaration instanceof Function) {
                    reference = ((Function)callableCtor.declaration).appliedTypedReference(producedType, null);
                } else if (callableCtor.declaration instanceof org.eclipse.ceylon.model.typechecker.model.Class) {
                    reference = ((org.eclipse.ceylon.model.typechecker.model.Class)callableCtor.declaration).appliedReference(producedType, null);
                } else if (callableCtor.declaration instanceof org.eclipse.ceylon.model.typechecker.model.Constructor) {
                    reference = ((org.eclipse.ceylon.model.typechecker.model.Constructor)callableCtor.declaration).appliedReference(producedType, null);
                } else {
                    throw Metamodel.newModelError("Unexpect declaration " +callableCtor.declaration);
                }
                // anonymous classes don't have parameter lists
                TypeDescriptor actualReifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.declaration.getUnit(), (Functional)callableCtor.declaration, reference);
                // This is all very ugly but we're trying to make it cheaper and friendlier than just checking the full type and showing
                // implementation types to the user, such as AppliedMemberClass
                Metamodel.checkReifiedTypeArgument("getConstructor", "Constructor<$1,$2>",
                        // this line is bullshit since it's always true, but otherwise we can't substitute the error message above :(
                        Variance.OUT, this.producedType, $reifiedType,
                        Variance.IN, Metamodel.getProducedType(actualReifiedArguments), $reified$Arguments);
    
                Metamodel.checkReifiedTypeArgument("apply", "CallableConstructor<$1,$2>", 
                        Variance.OUT, producedType, $reifiedType, 
                        Variance.IN, Metamodel.getProducedTypeForArguments(
                                declaration.declaration.getUnit(), 
                                (Functional)callableCtor.declaration, reference), $reified$Arguments);
                return new CallableConstructorImpl<Type,Sequential<? extends java.lang.Object>>(
                        this.$reifiedType, 
                        $reified$Arguments,
                        reference, 
                        callableCtor, 
                        this, instance);
            }
        } else if (ctor instanceof ValueConstructorDeclaration){
            ValueConstructorDeclarationImpl callableCtor = (ValueConstructorDeclarationImpl)ctor;
//            org.eclipse.ceylon.model.typechecker.model.Type constructorType = callableCtor.constructor.appliedType(this.producedType, Collections.<org.eclipse.ceylon.model.typechecker.model.Type>emptyList());
            TypedDeclaration val = (TypedDeclaration)callableCtor.constructor.getContainer().getDirectMember(callableCtor.constructor.getName(), null, false);
            return new ValueConstructorImpl<Type>(
                    this.$reifiedType,
                    callableCtor, val.getTypedReference(),
                    this, instance);
        } else {
            throw new AssertionError("Constructor neither CallableConstructorDeclaration nor ValueConstructorDeclaration");
        }
    }

    @Override
    public Type apply(Sequential<? extends Object> arguments) {
        return getDispatch().apply(arguments);
    }

    @Override
    public Type namedApply(
            Iterable<? extends Entry<? extends ceylon.language.String, ? extends Object>, ? extends Object> arguments) {
        return getDispatch().namedApply(arguments);
    }

    @Override
    public Object getDefaultParameterValue(Parameter parameter,
            Array<Object> values, int collectedValueCount) {
        return getDispatch().getDefaultParameterValue(parameter, values, collectedValueCount);
    }
    
    @Override
    public <Arguments extends Sequential<? extends Object>> Sequential<? extends FunctionModel<Type, Arguments>> getCallableConstructors(
            TypeDescriptor reified$Arguments) {
        return getCallableConstructors(reified$Arguments, (Sequential)empty_.get_());
    }
    
    @Override
    public <Arguments extends Sequential<? extends Object>> Sequential<? extends FunctionModel<Type, Arguments>> getCallableConstructors(
            TypeDescriptor reified$Arguments,
            @Sequenced
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        return Metamodel.getConstructors(this, true, true, reified$Arguments, annotations);
    }
    
    @Override
    public <Arguments extends Sequential<? extends Object>> Sequential<? extends FunctionModel<Type, Arguments>> getDeclaredCallableConstructors(
            TypeDescriptor reified$Arguments) {
        return getDeclaredCallableConstructors(reified$Arguments, (Sequential)empty_.get_());
    }
    
    @Override
    public <Arguments extends Sequential<? extends Object>> Sequential<? extends FunctionModel<Type, Arguments>> getDeclaredCallableConstructors(
            TypeDescriptor reified$Arguments,
            @Sequenced
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        return Metamodel.getConstructors(this, false, true, reified$Arguments, annotations);
    }
    
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getValueConstructors() {
        return getValueConstructors((Sequential)empty_.get_());
    }
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getValueConstructors(
            @Sequenced
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        return Metamodel.getConstructors(this, true, false, null, annotations);
    }
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getDeclaredValueConstructors() {
        return getDeclaredValueConstructors((Sequential)empty_.get_());
    }
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getDeclaredValueConstructors(
            @Sequenced
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        return Metamodel.getConstructors(this, false, false, null, annotations);
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<Type>")
    public ceylon.language.Sequential<? extends Type> getCaseValues(){
        Class classDecl = (Class)declaration.declaration;
        if (classDecl.hasEnumerated()) {
            // if enumerated => not abstract => can't have type cases,
            // can only possibly have value constructor cases
            if (classDecl.getCaseTypes() == null 
                    || classDecl.getCaseTypes().isEmpty()) {
                // it's not a closed enum of value constructors
                return (Sequential)empty_.get_();
            }
            ArrayList<Type> ctors = new ArrayList<>();
            for (ceylon.language.meta.declaration.Declaration d : ((ClassDeclarationImpl)declaration).constructors()) {
                Declaration dd = null;
                if (d instanceof CallableConstructorDeclarationImpl) {
                    continue;
                } else if (d instanceof ValueConstructorDeclarationImpl) {
                    dd = ((ValueConstructorDeclarationImpl)d).declaration;
                }
                // ATM this is an AND WRT annotation types: all must be present
                ctors.add(((ValueConstructor<Type>)getDeclaredConstructor(TypeDescriptor.NothingType, d.getName())).get());
            }
            
            Object[] array = ctors.toArray(new Object[ctors.size()]);
            ObjectArrayIterable<ceylon.language.meta.declaration.Declaration> iterable = 
                    new ObjectArrayIterable<ceylon.language.meta.declaration.Declaration>(
                            this.$reifiedType,
                            (Object[]) array);
            return (ceylon.language.Sequential) iterable.sequence();
        } else {
            return super.getCaseValues();
        }
    }
}
