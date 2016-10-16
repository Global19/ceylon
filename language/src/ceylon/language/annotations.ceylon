import ceylon.language.meta.declaration {
    Module,
    Package,
    Import,
    ClassDeclaration,
    ClassOrInterfaceDeclaration,
    ConstructorDeclaration,
    FunctionDeclaration,
    Declaration,
    ValueDeclaration,
    FunctionOrValueDeclaration,
    AliasDeclaration
}

"The annotation class for the [[annotation]] meta-annotation."
shared final sealed annotation class AnnotationAnnotation()
        satisfies OptionalAnnotation<AnnotationAnnotation,
                    ClassDeclaration
                  | FunctionDeclaration> {}

"Annotation to mark a class as an *annotation class*, or a 
 top-level function as an *annotation constructor*."
see (`interface Annotation`)
shared annotation AnnotationAnnotation annotation()
        => AnnotationAnnotation();

"The annotation class for the [[shared]] annotation."
shared final sealed annotation class SharedAnnotation()
        satisfies OptionalAnnotation<SharedAnnotation,
                    FunctionOrValueDeclaration
                  | ClassOrInterfaceDeclaration
                  | ConstructorDeclaration
                  | Package | Import> {}

"Annotation to mark a declaration as shared. A `shared` 
 declaration is visible outside the block of code in which 
 it is declared."
shared annotation SharedAnnotation shared()
        => SharedAnnotation();

"The annotation class for the [[variable]] annotation."
shared final sealed annotation class VariableAnnotation()
        satisfies OptionalAnnotation<VariableAnnotation,
                    ValueDeclaration> {}

"Annotation to mark a value as variable. A `variable` value 
 may be assigned multiple times."
shared annotation VariableAnnotation variable()
        => VariableAnnotation();

"The annotation class for the [[abstract]] annotation."
shared final sealed annotation class AbstractAnnotation()
        satisfies OptionalAnnotation<AbstractAnnotation,
                    ClassDeclaration
                  | ConstructorDeclaration> {}

"Annotation to mark a class as abstract. An `abstract` class
 may have `formal` members, but may not be directly 
 instantiated. An enumerated class must be `abstract`."
shared annotation AbstractAnnotation abstract()
        => AbstractAnnotation();

"The annotation class for the [[final]] annotation."
shared final sealed annotation class FinalAnnotation()
        satisfies OptionalAnnotation<FinalAnnotation,
                    ClassDeclaration> {}

"Annotation to mark a class as final. A `final` class may 
 not be extended. Marking a class as `final` affects disjoint
 type analysis."
shared annotation FinalAnnotation final()
        => FinalAnnotation();
                
"The annotation class for the [[sealed]] annotation."
since("1.1.0")
shared final sealed annotation class SealedAnnotation()
        satisfies OptionalAnnotation<SealedAnnotation,
                    ClassOrInterfaceDeclaration
                  | ConstructorDeclaration> {}
                
"Annotation to mark an interface, class, or constructor as 
 sealed. A `sealed` interface may not be satisfied outside 
 of the module in which it is defined. A `sealed` class may 
 not be extended or instantiated outside of the module in 
 which it is defined. A `sealed` constructor may not be
 invoked outside of the module in which it is defined."
since("1.1.0")
shared annotation SealedAnnotation sealed()
        => SealedAnnotation();

"The annotation class for the [[actual]] annotation."
shared final sealed annotation class ActualAnnotation()
        satisfies OptionalAnnotation<ActualAnnotation,
                    FunctionOrValueDeclaration
                  | ClassDeclaration> {}

"Annotation to mark a member of a type as refining a member 
 of a supertype."
shared annotation ActualAnnotation actual()
        => ActualAnnotation();

"The annotation class for the [[formal]] annotation."
shared final sealed annotation class FormalAnnotation()
        satisfies OptionalAnnotation<FormalAnnotation,
                    FunctionOrValueDeclaration
                  | ClassDeclaration> {}

"Annotation to mark a member whose implementation must be 
 provided by subtypes."
shared annotation FormalAnnotation formal()
        => FormalAnnotation();

"The annotation class for the [[default]] annotation."
shared final sealed annotation class DefaultAnnotation()
        satisfies OptionalAnnotation<DefaultAnnotation,
                    FunctionOrValueDeclaration
                  | ClassDeclaration> {}

"Annotation to mark a member whose implementation may be 
 refined by subtypes. Non-`default` declarations may not be 
 refined."
shared annotation DefaultAnnotation default()
        => DefaultAnnotation();

"The annotation class for the [[static]] annotation."
shared final sealed annotation class StaticAnnotation()
        satisfies OptionalAnnotation<StaticAnnotation,
                    FunctionOrValueDeclaration
                  | ClassOrInterfaceDeclaration
                  | AliasDeclaration> {}

"Annotation to mark a member of a toplevel class as static. 
 A `static` member does not have access to any current 
 instance of the class or interface, and must occur before 
 any non-`static` member declarations in the body of the 
 class or interface declaration.
 
 For example:
 
     class Hello {
         shared static void hello() => print(\"hello\");
         shared new() {}
     }
     
     shared void run() => Hello.hello();"
shared annotation StaticAnnotation static()
        => StaticAnnotation();

"The annotation class for the [[late]] annotation."
shared final sealed annotation class LateAnnotation()
        satisfies OptionalAnnotation<LateAnnotation,
                    ValueDeclaration> {}

"Annotation to disable definite initialization analysis for 
 a toplevel value, or for an attribute of a class.
 
 - In the case of a class attribute, the attribute may not
   be initialized by its declaration, and may be left 
   unassigned by the class initializer.
 - In the case of a toplevel value, the value may not be
   initialized by its declaration.
 
 A `late` value may be assigned by any code to which it is
 visible, but repeated assignment produces an 
 [[InitializationError]].
 
 Evaluation of a `late` value cannot be guaranteed sound by
 the compiler, and so evaluation of a `late` value before 
 initialization produces an [[InitializationError]]."
shared annotation LateAnnotation late()
        => LateAnnotation();

"The annotation class for the [[native]] annotation."
shared final sealed annotation class NativeAnnotation(backends)
        satisfies OptionalAnnotation<NativeAnnotation> {
    "The compiler backend that this native annotation applies 
     to, or the empty string to declare the annotated element 
     is a native header."
    since("1.2.0")
    shared String* backends;
}

"Annotation to mark a module, import, or declaration as 
 platform-native.
 
 For example, this code defines a native function in a 
 cross-platform module:
 
     import java.lang { System }
     
     native void hello();
     
     native (\"jvm\") void hello() {
         System.out.println(\"hello\");
     }
     
     native (\"js\") void hello() {
         dynamic {
             console.log(\"hello\");
         }
     }"
shared annotation NativeAnnotation native(
    since("1.2.0")
    String* backends)
        => NativeAnnotation(*backends);

/*"The annotation class for [[inherited]]."
shared final sealed annotation class InheritedAnnotation()
        satisfies OptionalAnnotation<InheritedAnnotation, 
            ClassDeclaration> {}

"Annotation to mark an annotation class as a *inherited*."
shared annotation InheritedAnnotation inherited() 
        => InheritedAnnotation();*/

"The annotation class for the [[doc]] annotation."
shared final sealed annotation class DocAnnotation(
    "Documentation, in Markdown syntax, about the annotated 
     program element"
    shared String description)
        satisfies OptionalAnnotation<DocAnnotation> {}

"Annotation to specify API documentation of a program
 element."
shared annotation DocAnnotation doc(
    "Documentation, in Markdown syntax, about the annotated element"
    String description) 
        => DocAnnotation(description);

"The annotation class for the [[see]] annotation."
shared final sealed annotation class SeeAnnotation(
    "The program elements being referred to."
    shared Declaration* programElements)
        satisfies SequencedAnnotation<SeeAnnotation> {}

"Annotation to specify references to other program elements
 related to the annotated API."
shared annotation SeeAnnotation see(
    "The program elements being referred to."
    Declaration* programElements)
        => SeeAnnotation(*programElements);

"The annotation class for the [[by]] annotation."
shared final sealed annotation class AuthorsAnnotation(
    "The authors, in Markdown syntax, of the annotated 
     program element"
    shared String* authors)
        satisfies OptionalAnnotation<AuthorsAnnotation> {}

"Annotation to document the authors of an API."
shared annotation AuthorsAnnotation by(
    "The authors, in Markdown syntax, of the annotated 
     program element"
    String* authors)
        => AuthorsAnnotation(*authors);

"The annotation class for the [[throws]] annotation."
shared final sealed annotation class ThrownExceptionAnnotation(
    "The [[Exception]] type that is thrown."
    shared Declaration type,
    "A description, in Markdown syntax, of the circumstances 
     that cause this exception to be thrown."
    shared String when)
        satisfies SequencedAnnotation<ThrownExceptionAnnotation,
                    FunctionOrValueDeclaration
                  | ClassDeclaration
                  | ConstructorDeclaration> {}

"Annotation to document the exception types thrown by a 
 function, value, class, or constructor."
shared annotation ThrownExceptionAnnotation throws(
    "The [[Exception]] type that is thrown."
    Declaration type,
    "A description, in Markdown syntax, of the circumstances 
     that cause this exception to be thrown."
    String when = "")
        => ThrownExceptionAnnotation(type, when);

"The annotation class for the [[deprecated]] annotation."
shared final sealed annotation class DeprecationAnnotation(
    "A description, in Markdown syntax, of why the program 
     element is deprecated, and of what alternatives are 
     available."
    shared String description)
        satisfies OptionalAnnotation<DeprecationAnnotation> {
    "A description, in Markdown syntax, of why the program 
     element is deprecated, and what alternatives are 
     available, or `null`."
    shared String? reason
            => !description.empty then description;
}

"Annotation to mark program elements which should not be 
 used anymore."
shared annotation DeprecationAnnotation deprecated(
    "A description, in Markdown syntax, of why the program 
     element is deprecated, and what alternatives are 
     available."
    String reason = "")
        => DeprecationAnnotation(reason);

"The annotation class for the [[tagged]] annotation."
shared final sealed annotation class TagsAnnotation(
    "The tags, in plain text."
    shared String* tags)
        satisfies OptionalAnnotation<TagsAnnotation> {}

"Annotation to categorize an API by tag."
shared annotation TagsAnnotation tagged(
    "The tags, in plain text."
    String* tags)
        => TagsAnnotation(*tags);

"The annotation class for the [[aliased]] annotation."
since("1.2.0")
shared final sealed annotation class AliasesAnnotation(
    "The aliases, in plain text."
    shared String* aliases)
        satisfies OptionalAnnotation<AliasesAnnotation> {}

"Annotation to specify a list of aliases that tools such as auto-completion and
 quick-fixes should consider, to help users find a declaration using its aliases."
since("1.2.0")
shared annotation AliasesAnnotation aliased(
    "The aliases, in plain text."
    String* aliases)
        => AliasesAnnotation(*aliases);

"The annotation class for the [[license]] annotation."
shared final sealed annotation class LicenseAnnotation(
    "The name, text, or URL of the license."
    shared String description)
        satisfies OptionalAnnotation<LicenseAnnotation,
                    Module> {}

"Annotation to specify the URL of the license of a module or 
 package."
shared annotation LicenseAnnotation license(
    "The name, text, or URL of the license."
    String description)
        => LicenseAnnotation(description);

"The annotation class for the [[optional]] annotation."
shared final sealed annotation class OptionalImportAnnotation()
        satisfies OptionalAnnotation<OptionalImportAnnotation,
                    Import> {}

"Annotation to specify that a module can be executed even if 
 the annotated dependency is not available."
shared annotation OptionalImportAnnotation optional()
        => OptionalImportAnnotation();

"The annotation class for the [[suppressWarnings]] 
 annotation."
since("1.2.0")
shared final sealed annotation class SuppressWarningsAnnotation(
    "The warning types to suppress."
    shared String* warnings)
        satisfies OptionalAnnotation<SuppressWarningsAnnotation, 
                    FunctionOrValueDeclaration
                  | ClassOrInterfaceDeclaration
                  | ConstructorDeclaration
                  | Module | Package | Import> {}

"Annotation to suppress compilation warnings of the 
 [[specified types|warnings]] when typechecking the 
 annotated program element."
since("1.2.0")
shared annotation SuppressWarningsAnnotation suppressWarnings(
    "The warning types to suppress.
     
     Allowed warning types are:
     `filenameNonAscii`,
     `filenameCaselessCollision`,
     `deprecation`, 
     `disjointEquals`,
     `disjointContainment`,
     `compilerAnnotation`,
     `doclink`,
     `expressionTypeNothing`,
     `expressionTypeCallable`,
     `expressionTypeIterable`,
     `unusedDeclaration`,
     `unusedImport`,
     `ceylonNamespace`,
     `javaNamespace,` 
     `suppressedAlready`, 
     `suppressesNothing`, 
     `unknownWarning`, 
     `ambiguousAnnotation`,
     `javaAnnotationElement`,
     `syntaxDeprecation`,
     `smallIgnored`,
     `literalNotSmall`,
     `disjointEquals`,
     `disjointContainment`,
     `redundantNarrowing`,
     `redundantIteration`,
     `missingImportPrefix`,
     `uncheckedTypeArguments`,
     `inferredNotNull`."
    String* warnings) 
        => SuppressWarningsAnnotation(*warnings);

"The annotation class for the [[serializable]] annotation."
since("1.2.0")
shared final annotation class SerializableAnnotation()
        satisfies OptionalAnnotation<SerializableAnnotation,
                    ClassDeclaration> {
}

"Annotation to specify that a class is serializable.
  
 A serializable class may have instances that cannot be 
 serialized if those instances have reachable references to 
 instances of non-serializable classes."
since("1.2.0")
shared annotation SerializableAnnotation serializable() 
        => SerializableAnnotation();


"The annotation class for the [[small]] annotation."
since("1.3.0")
shared final annotation class SmallAnnotation()
        satisfies OptionalAnnotation<SmallAnnotation,
                    FunctionOrValueDeclaration> {
}

"Annotation to hint to the compiler that an `Integer` or 
 `Float` typed value or function should be represented using 
 a 32-bit signed integer or 32-bit IEEE float if possible."
since("1.3.0")
shared annotation SmallAnnotation small() 
        => SmallAnnotation();


"The annotation class for the [[service]] annotation."
since("1.3.0")
shared final annotation class ServiceAnnotation(contract)
        satisfies SequencedAnnotation<ServiceAnnotation,
                    ClassDeclaration> {
    "The service interface or class that the annotated class 
     provides."
    shared ClassOrInterfaceDeclaration contract;
}

"Annotation marking a class as implementing a service. The
 class must be a non-abstract, shared, toplevel class.
 
 For example, if `Manager` is an interface, this code
 declares an implementation of `Manager`:
 
     service (`Manager`)
     shared class DefautManager() satisfies Manager {}
 
 Service implementations can be found at runtime using 
 [[ceylon.language.meta.declaration::Module.findServiceProviders]].
 
     {Manager*} managers = `module`.findServiceProviders(`Manager`);
     assert (exists manager = managers.first);"
since("1.3.0")
shared annotation ServiceAnnotation service(
    "The service interface or class that the annotated class 
     provides."
    ClassOrInterfaceDeclaration contract) 
        => ServiceAnnotation(contract);

"The annotation class for the [[since]] annotation."
since("1.3.0")
shared final sealed annotation class SinceAnnotation(
    "The version of the module when this declaration was added."
    shared String version)
        satisfies OptionalAnnotation<SinceAnnotation> {}

"Annotation to indicate at which moment the annotated declaration
 was added to the module."
since("1.3.0")
shared annotation SinceAnnotation since(
    "The version of the module when this declaration was added."
    String version) 
        => SinceAnnotation(version);
