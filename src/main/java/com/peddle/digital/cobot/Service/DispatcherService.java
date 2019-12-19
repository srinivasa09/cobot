package com.peddle.digital.cobot.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.peddle.digital.cobot.model.Job;
import com.peddle.digital.cobot.repository.JobRepository;

import io.github.bonigarcia.wdm.WebDriverManager;



@Component
public class DispatcherService {

	@Autowired
	JobRepository jobRepository;

	@Autowired
	private Environment env;

	public void processJob(String content, String appJobID, Job job) throws Exception {

		JSONObject obj = new JSONObject(content);
		//String targetSystem = obj.getString("TargetSystemURL");

//		if(targetSystem != null && targetSystem.contains(TargetSystems.JIRA))
//		{
//			String statusCallBackUrl = env.getProperty("cobot.status.callback.url");
//			com.cobot.testcases.JiraAddUserTest jira = new com.cobot.testcases.JiraAddUserTest();
//			jira.test(content, appJobID, statusCallBackUrl);
//
//			//			String adminCred = obj.getString("AdminCredentails");
//			//			String newUser = obj.getString("NewUser");
//			//
//			//			String decode = Base64Utils.decode(adminCred);
//			//			String[] split = decode.split(":");
//			//
//			//			if(split.length ==2)
//			//			{
//			//				String adminUser =  split[0];
//			//				String adminPass =  split[1];
//			//				if(adminUser== null ||  adminPass == null)
//			//				{
//			//					jobRepository.updateStatus(STATUS.Failed.toString(),job.getId());
//			//				}
//			//				else
//			//				{
//			//					 Map<String, Object> inputData = new HashMap<String, Object>();
//			//					 inputData.put("url", targetSystem);
//			//				     inputData.put("adminUserName", adminUser);
//			//				     inputData.put("adminPassword", adminPass);
//			//				     inputData.put("newUser", newUser);
//			//				     inputData.put("jobId", appJobID);
//			//					 com.cobot.testcases.JiraAddUserTest jira = new com.cobot.testcases.JiraAddUserTest();
//			//					
//			//				}
//			//			}
//		}

		if(job.getScriptFileName() != null)
		{
		        WebDriverManager.firefoxdriver().setup();
			//System.setProperty("webdriver.gecko.driver","C:/dump/git/CoBotAutomation/Drivers/geckodriver.exe");
			compileScriptFile(job.getScriptFileName(), content);
		}
	}

	public  String compileScriptFile(String scriptFile, String Content) throws Exception
	{

		//		testcases.root=C:/dump/code/test1/src/test/java
		//				testcases.packagedir=com/example/tests/
		//				testcases.package=com.example.tests
		//
		//				testcases.repo=C:/dump/code/repo/
		
		JSONObject obj = new JSONObject(Content);
		JSONArray jsonArray = obj.getJSONArray("data");
		List<Object> list = jsonArray.toList();
		List<String> data= new ArrayList<String> ();

		 for(Object a: list){
			 data.add(String.valueOf(a));
		 }
		 System.out.println(data);
		
		String scriptsDir = env.getProperty("scripts.dir");
		String scriptRootDir = env.getProperty("testcases.root");
		String scriptpackagedDir = env.getProperty("testcases.packagedir");
		String scriptpackage = env.getProperty("testcases.package");
		
		String from = scriptsDir+scriptFile;
		String to = scriptRootDir+"/"+scriptpackagedDir+scriptFile;
		
		File file = new File(to);
		
		if(!file.getParentFile().exists())
		{
		    file.getParentFile().mkdirs();
		}
		   
	
		//copyFile(from,to);
		convertJavaCode(new File(from),new File(to));

		String seleniumDependencies = env.getProperty("testcases.selenium.dependencies");
		
		String classpath = System.getProperty("java.class.path");
		System.out.println(classpath);
		
		
		Process p = Runtime.getRuntime().exec("javac -cp \"" + classpath +" \" " + to);
		p.waitFor();
		
		InputStream inputStream = p.getInputStream();
		
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputStream, writer, "UTF-8");
		String theString = writer.toString();
		
		
		 InputStream errorStream = p.getErrorStream();
		
		StringWriter writer1 = new StringWriter();
		IOUtils.copy(errorStream, writer1, "UTF-8");
		String theString1 = writer1.toString();
                System.out.println(theString1);
                
		
		System.out.println("compilation Finished");
		
	 	File root = new File(scriptRootDir); 
	 	
		URLClassLoader child = new URLClassLoader(
		        new URL[] { root.toURI().toURL() },
		        this.getClass().getClassLoader()
		);
		
		String javaClass  = scriptFile.replace(".java", "") ;
		
		Class classToLoad = Class.forName(scriptpackage + "."+javaClass, true, child);
		Method method =  classToLoad.getMethod("test", java.util.List.class);
		Method methodSetup =  classToLoad.getMethod("setUp");
		Object instance = classToLoad.newInstance();
		methodSetup.invoke(instance);
		Object result = method.invoke(instance,data);

		return "success";
	}


	public static void copyFile(String from, String to) throws IOException{
		Path src = Paths.get(from);
		Path dest = Paths.get(to);
		Files.copy(src, dest);
	}

	public static void convertJavaCode(File src, File dest) {
		BufferedReader reader;
		try {

			String fileName = src.getAbsolutePath();
			reader = new BufferedReader(new FileReader(src.getAbsolutePath()));
			PrintWriter writer = new PrintWriter(dest);

			String className = fileName.substring(fileName.lastIndexOf("\\")+1).replace(".java","");

			String line = reader.readLine();
			boolean initconvert = false;
			int countvariables=0;

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

				//rename test method
				if(line.contains("public void testUntitledTestCase()"))
				{
					initconvert=true;
					line =  "public void test(java.util.List<String> data) {";
				}

				if(line.contains("public class"))
				{
				        
					line =  "public class "+className +"{";
				}

				System.out.println(line);
				writer.println(line);
				// read next line
				line = reader.readLine();
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


