package com.peddle.digital.cobot.Util;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.peddle.digital.cobot.Service.DispatcherService;

public class ComplieScriptUtil {
    
    final static Logger logger = Logger.getLogger(ComplieScriptUtil.class);
    
    public  static String compileScriptFile(String scriptFile, String content,String scriptsDir ,String scriptRootDir,String scriptpackagedDir,String scriptpackage) throws Exception
    {
	JSONObject obj = new JSONObject(content);
	JSONArray jsonArray = obj.getJSONArray("data");
	List<Object> list = jsonArray.toList();
	List<String> data= new ArrayList<String> ();

	for(Object a: list){
	    data.add(String.valueOf(a));
	}
	System.out.println(data);

	String from = scriptsDir+scriptFile;
	String to = scriptRootDir+"/"+scriptpackagedDir+scriptFile;

	File file = new File(to);
	

	if(!file.getParentFile().exists())
	{
	    file.getParentFile().mkdirs();
	}

	JavaCodeUtil.convertJavaCode(new File(from),new File(to));

	String classpath = System.getProperty("java.class.path");
	
	Process compileSript = Runtime.getRuntime().exec("javac -cp \"" + classpath +" \" " + to);
	compileSript.waitFor();

	InputStream errorStream = compileSript.getErrorStream();

	StringWriter scriptCompileWriter = new StringWriter();
	IOUtils.copy(errorStream, scriptCompileWriter, "UTF-8");
	String compileErrString = scriptCompileWriter.toString();
	System.out.println(compileErrString);
	errorStream.close();
	logger.info("compilation Finished");

	
	
	String scriptclassFile = scriptpackage+"."+scriptFile.replace(".java","");
	
        Process runscript = Runtime.getRuntime().exec("java -cp \"" + classpath +";"+scriptRootDir+" \" " +scriptclassFile + " "+content);
	runscript.waitFor();
	errorStream = runscript.getErrorStream();
	StringWriter scriptRunWriter = new StringWriter();
	IOUtils.copy(errorStream, scriptRunWriter, "UTF-8");
	String runErrorStr = scriptRunWriter.toString();
	errorStream.close();
	System.out.println(runErrorStr);
	
	
//	StringWriter compileStringWriter = new StringWriter();
//	IOUtils.copy(errorStream, compileStringWriter, "UTF-8");
//	String compileString = compileStringWriter.toString();
//	System.out.println(compileString);
	
//	URLClassLoader child = new URLClassLoader(
//		new URL[] { root.toURI().toURL() },
//		ComplieScriptUtil.class.getClassLoader()
//		);
//
//	String javaClass  = scriptFile.replace(".java", "") ;
//
//	Class classToLoad = Class.forName(scriptpackage + "."+javaClass, true, child);
//	Method method =  classToLoad.getMethod("test", java.util.List.class);
//	Method methodSetup =  classToLoad.getMethod("setUp");
//	Object instance = classToLoad.newInstance();
//	methodSetup.invoke(instance);
//	Object result = method.invoke(instance,data);

	return "";
    }

}
