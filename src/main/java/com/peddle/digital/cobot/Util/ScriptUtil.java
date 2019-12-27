package com.peddle.digital.cobot.Util;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class ScriptUtil {
    
    final static Logger logger = Logger.getLogger(ScriptUtil.class);
    
    
    public static void transformScriptFile(String scriptFile, String scriptsRepoDir, String scriptRootDir)
    {
	
	String scriptFromRepo = scriptsRepoDir+scriptFile;
	String transformedJavaFile = scriptRootDir+"/"+scriptFile;

	File file = new File(transformedJavaFile);
	
	if(!file.getParentFile().exists())
	{
	    file.getParentFile().mkdirs();
	}

	JavaCodeUtil.convertJavaCode(new File(scriptFromRepo),new File(transformedJavaFile));
    }
    
    public static void compileScriptFile(String scriptFile, String scriptRootDir) throws Exception
    {
	
	String transformedJavaFile = scriptRootDir+"/"+scriptFile;

	String classpath = System.getProperty("java.class.path");
	
	Process compileSript = Runtime.getRuntime().exec("javac -cp \"" + classpath +" \" " + transformedJavaFile);
	compileSript.waitFor();

	InputStream errorStream = compileSript.getErrorStream();

	StringWriter scriptCompileWriter = new StringWriter();
	IOUtils.copy(errorStream, scriptCompileWriter, "UTF-8");
	String compileErrString = scriptCompileWriter.toString();
	logger.error(compileErrString);
	errorStream.close();
	logger.info("compilation Finished");

    }
    
    public static void runScriptFile(String scriptFile, String content , String scriptRootDir) throws Exception
    {
        String scriptclassFile = scriptFile.replace(".java","");
        
        String classpath = System.getProperty("java.class.path");
	
        Process runscript = Runtime.getRuntime().exec("java -cp \"" + classpath +";"+scriptRootDir+" \" " +scriptclassFile + " "+content);
	runscript.waitFor();
	InputStream errorStream = runscript.getErrorStream();
	StringWriter scriptRunWriter = new StringWriter();
	IOUtils.copy(errorStream, scriptRunWriter, "UTF-8");
	String runErrorStr = scriptRunWriter.toString();
	errorStream.close();
	logger.error(runErrorStr);
    }
    
}
