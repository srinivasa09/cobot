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
	BufferedReader javaMainReader=null;
	PrintWriter writer = null;

	try {

	    String fileName = src.getAbsolutePath();
	    reader = new BufferedReader(new FileReader(src.getAbsolutePath()));
	    writer = new PrintWriter(dest);

	    String className = fileName.substring(fileName.lastIndexOf("\\")+1).replace(".java","");

	    String line = reader.readLine();
	    boolean initconvert = false;
	    boolean addedImports=false;
	    int countvariables=0;
	    String createdClassName="";

	    ArrayList<String> javaCodeList = new ArrayList<String>();

	    while (line != null) {

		if(initconvert)
		{		   
		    if(line.contains("driver.get(")) 
		    {
			line= line.replace("driver.get(", "driver.navigate().to(");
		    }

		    if(line.contains(".sendKeys("))
		    {
			String  prefix = line.substring(0, line.lastIndexOf(".sendKeys(")+1);
			String  suffix = line.substring(line.lastIndexOf(".sendKeys(")+1);
			int quoteIndex = suffix.indexOf("\"");
			if(quoteIndex!=-1)
			{
			    String ops = suffix.substring(0,suffix.indexOf("\""));
			    ops=ops+"data.get("+countvariables+"));";
			    //System.out.println(ops);

			    line = prefix+ops;

			}
			countvariables++;
		    }

		    if(line.contains("selectByVisibleText("))
		    {
			String  prefix = line.substring(0, line.lastIndexOf(".selectByVisibleText(")+1);
			String  suffix = line.substring(line.lastIndexOf(".selectByVisibleText(")+1);
			int quoteIndex = suffix.indexOf("\"");
			if(quoteIndex!=-1)
			{
			    String ops = suffix.substring(0,suffix.indexOf("\""));
			    ops=ops+"data.get("+countvariables+"));";
			    //System.out.println(ops);
			    line = prefix+ops;
			}
			countvariables++;
		    }


		}
		
		if( !addedImports && line.startsWith("import ")) {
		    addImports(javaCodeList);
		    addedImports=true;
		}

		//rename test method
		if(line.contains("public void test"))
		{
		    initconvert=true;
		    line =  "public void test(java.util.List<String> data) {";
		}

		if(line.contains("public class"))
		{
		    createdClassName  =  line.replace("public class ", "").replace("{", "").trim();   
		    line =  "public class "+className +"{";
		}

		javaCodeList.add(line);
		line = reader.readLine();
	    }
	    reader.close();
	    javaCodeList.remove(javaCodeList.size() - 1);
	    //writer.flush();
	    //writer.close();

	    InputStream javaMainstream = JavaCodeUtil.class.getResourceAsStream("/templates/java_main.txt");

	    javaMainReader = new BufferedReader(new InputStreamReader(javaMainstream));

	    line = javaMainReader.readLine();

	    while (line != null) {

		if(line.contains("<CLASS_NAME>"))
		{
		    line = line.replaceAll("<CLASS_NAME>", className) ;
		}

		javaCodeList.add(line);
		line = javaMainReader.readLine();
	    }
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

	    if(javaMainReader != null)
	    {
		try {
		    javaMainReader.close();

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
}
