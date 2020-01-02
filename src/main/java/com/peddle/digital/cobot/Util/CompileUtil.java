package com.peddle.digital.cobot.Util;

import javax.json.Json;
import javax.json.JsonObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;




public class CompileUtil {
    
    public void test()
    {
	 //WebDriver  driver = new FirefoxDriver();
	WebDriver driver = new ChromeDriver();
	driver.manage().window().maximize();
	
	JsonObject dev = Json.createObjectBuilder().
                add("developer", "duke").
                build();
	
    }

        public static void main(String[] args)
        {
           
    	JsonObject dev = Json.createObjectBuilder().
                add("developer", "duke").
                build();
    	System.out.println(dev.toString());
        }
    
    
//     public void checkAndUpdateInputType(String elemeintID, java.util.List<String> data, Integer count)
//     {
//	 String type = driver.findElement(By.id(elemeintID)).getAttribute("type");
//	 if(type != null && type.equals("radio"))
//         {
//	     driver.findElement(By.xpath("//input[@value='"+ data.get(count) +"']")).click();
//	     count++;
//         }
//	 else
//	 {
//	     driver.findElement(By.id(elemeintID)).click();
//	 }
//     }
     
//	public static void copyFromScriptRepoToRootDir(String scriptFile)
//	{
//		
//		testcases.root=C:/dump/code/test1/src/test/java
//				testcases.packagedir=com/example/tests/
//				testcases.package=com.example.tests
//
//				testcases.repo=C:/dump/code/repo/
		
//		String scriptsDir = env.getProperty("scripts.dir");
//		String scriptRootDir = env.getProperty("testcases.root");
//		String scriptUpload = env.getProperty("atestcases.packagedir");
//		String statusCallBackUrl = env.getProperty("cobot.status.callback.url");
//	}

}
