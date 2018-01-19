package com.mvc.ethereum.utils;

import org.web3j.codegen.SolidityFunctionWrapperGenerator;
import org.web3j.crypto.ContractUtils;
import org.web3j.utils.Strings;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * contrace util
 *
 * @author qiyichen
 * @create 2018/1/18 16:42
 */
public class ContractUtil {

    public static String solidityBaseDir;
    public static String tempDirPath;
    static final String JAVA_TYPES_ARG = "--javaTypes";

    public static void codeGeneration(
            String contractName, String inputFileName, String packageName)
            throws Exception {

        SolidityFunctionWrapperGenerator.main(Arrays.asList(
                JAVA_TYPES_ARG,
                solidityBaseDir + File.separator + contractName + File.separator
                        + "build" + File.separator + inputFileName + ".bin",
                solidityBaseDir + File.separator + contractName + File.separator
                        + "build" + File.separator + inputFileName + ".abi",
                "-p", packageName,
                "-o", tempDirPath
        ).toArray(new String[0]));
    }

    public void verifyGeneratedCode(String sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (StandardJavaFileManager fileManager =
                     compiler.getStandardFileManager(diagnostics, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager
                    .getJavaFileObjectsFromStrings(Arrays.asList(sourceFile));
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null, fileManager, diagnostics, null, null, compilationUnits);
            assertTrue("Generated contract contains compile time error", task.call());
        }
    }
}
