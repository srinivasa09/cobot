package com.peddle.digital.cobot.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class JavaCodeUtil {

    final static Logger logger = Logger.getLogger(JavaCodeUtil.class);

    public static void convertJavaCode(File src, File dest) {
	BufferedReader reader=null;
	PrintWriter writer = null;

	try {

	    String fileName = src.getAbsolutePath();
	    reader = new BufferedReader(new FileReader(src.getAbsolutePath()));
	    writer = new PrintWriter(dest);

	    String className = fileName.substring(fileName.lastIndexOf("\\")+1).replace(".java","");

	    String line = reader.readLine();
	    boolean initconvert = false;
	    boolean addedImports=false;
	    CountHolder count=new CountHolder(0);

	    ArrayList<String> javaCodeList = new ArrayList<String>();

	    while (line != null) {

		if(initconvert)
		{		   
		    line = updateTestMethodContents(line,count);
		}

		if( !addedImports && line.startsWith("import ")) {
		    addImports(javaCodeList);
		    addedImports=true;
		}

		//rename test method
		else if(line.contains("public void test"))
		{
		    initconvert=true;
		    line =  "public void test(java.util.List<String> data) {";
		}

		else if(line.contains("public class"))
		{   
		    line =  "public class "+className +"{";
		}

		else if(line.startsWith("package com.example.tests"))
		{   
		    line =  "";
		}

		javaCodeList.add(line);
		line = reader.readLine();
	    }
	    
	    reader.close();
	    javaCodeList.remove(javaCodeList.size() - 1);

	    addMainMethod(javaCodeList,className);

	    javaCodeList.add("}");

	    for(String entry: javaCodeList)
	    {
		writer.println(entry);
		System.out.println(entry);
	    }

	    writer.flush();
	    writer.close();

	} catch (IOException e) {

	}
	finally {
	    if(reader != null)
	    {
		try {
		    reader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    writer.close();
	}
    }

    public static void addImports(ArrayList<String> javaCodeList)
    {
	BufferedReader javaImportsReader= null;

	try {

	    InputStream javaMainstream = JavaCodeUtil.class.getResourceAsStream("/templates/java_imports.txt");
	    javaImportsReader = new BufferedReader(new InputStreamReader(javaMainstream));

	    String line = javaImportsReader.readLine();

	    while (line != null) {
		javaCodeList.add(line);
		line = javaImportsReader.readLine();
	    }
	}catch(IOException e) {
	    if(javaImportsReader != null)
	    {
		try {
		    javaImportsReader.close();

		} catch (IOException ex) {

		    e.printStackTrace();
		}
	    }}
    }

    public  static void addMainMethod(ArrayList<String> javaCodeList, String className)
    {
	BufferedReader javaMainReader=null;
	try {
	    InputStream javaMainstream = JavaCodeUtil.class.getResourceAsStream("/templates/java_main.txt");

	    javaMainReader = new BufferedReader(new InputStreamReader(javaMainstream));

	    String line = javaMainReader.readLine();

	    while (line != null) {

		if(line.contains("<CLASS_NAME>"))
		{
		    line = line.replaceAll("<CLASS_NAME>", className) ;
		}

		javaCodeList.add(line);
		line = javaMainReader.readLine();
	    }
	}catch(IOException e) {
	    if(javaMainReader != null)
	    {
		try {
		    javaMainReader.close();

		} catch (IOException err) {

		    err.printStackTrace();
		}
	    }

	}
    }
    
    public static String updateTestMethodContents(String line,CountHolder count )
    {
	 if(line.contains("driver.get(")) 
	    {
		line= line.replace("driver.get(", "driver.navigate().to(");
	    }

	    else if(line.contains(".sendKeys("))
	    {
		String  prefix = line.substring(0, line.lastIndexOf(".sendKeys(")+1);
		String  suffix = line.substring(line.lastIndexOf(".sendKeys(")+1);
		int quoteIndex = suffix.indexOf("\"");
		if(quoteIndex!=-1)
		{
		    String ops = suffix.substring(0,suffix.indexOf("\""));
		    ops=ops+"data.get("+count.value+"));";
		    line = prefix+ops;
		}
		count.value++;
	    }

	    else if(line.contains("selectByVisibleText("))
	    {
		String  prefix = line.substring(0, line.lastIndexOf(".selectByVisibleText(")+1);
		String  suffix = line.substring(line.lastIndexOf(".selectByVisibleText(")+1);
		int quoteIndex = suffix.indexOf("\"");
		if(quoteIndex!=-1)
		{
		    String ops = suffix.substring(0,suffix.indexOf("\""));
		    ops=ops+"data.get("+count.value+"));";
		    
		    line = prefix+ops;
		}
		count.value++;
	    }
	 return line;
    }
}

class CountHolder {
    
    public int value;
    public CountHolder(int value)
    {
	this.value=value;
    }
} 