package org.eclipse.ceylon.compiler.js;

import org.eclipse.ceylon.common.tool.ToolError;


public class CeylonRunJsException extends ToolError {

    private static final long serialVersionUID = 1L;

    public CeylonRunJsException(String message) {
        super(message);
    }

    public CeylonRunJsException(String message, int exitCode) {
        super(message, exitCode);
    }
}