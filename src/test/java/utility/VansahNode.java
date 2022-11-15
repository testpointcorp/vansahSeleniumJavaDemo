package utility;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;


public class VansahNode {

	//--------------------------- ENDPOINTS -------------------------------------------------------------------------------
	private static final String API_VERSION     = "v1";
	private static final String VANSAH_URL      = "https://prod.vansahnode.app";
	private static final String ADD_TEST_RUN    = VANSAH_URL + "/api/" + API_VERSION+ "/run";
	private static final String ADD_TEST_LOG    = VANSAH_URL + "/api/" + API_VERSION+ "/logs";
	private static final String UPDATE_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION+ "/logs/";
	private static final String REMOVE_TEST_LOG = VANSAH_URL + "/api/" + API_VERSION+ "/logs/";
	private static final String REMOVE_TEST_RUN = VANSAH_URL + "/api/" + API_VERSION+ "/run/";
	private static final String TEST_SCRIPT     = VANSAH_URL + "/api/" + API_VERSION+ "/testCase/list/testScripts";
	//--------------------------------------------------------------------------------------------------------------------


	//--------------------------- INFORM YOUR UNIQUE VANSAH TOKEN HERE ---------------------------------------------------
	private static final String VANSAH_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb20udmFuc2FoLmppcmEudmFuc2FoLXBsdWdpbiIsImlhdCI6MTY2ODQ4MzY2NCwic3ViIjoiNjE5ZGMzNmJkNTk4NmMwMDZhZDE3YjVlIiwiZXhwIjoyNjY4NDgzNjY0LCJhdWQiOlsiY2Q3YTJhZmQtYzgyYy0zYzY2LTgxMDItZWZmOGIwN2E5MjExIl0sInR5cGUiOiJjb25uZWN0In0.TWylCkTRCmmTWoZKzyIBuEG39b1bGX1mQMhzJJd2bmQ";


	//--------------------------------------------------------------------------------------------------------------------


	//--------------------------- IF YOU ARE USING VANSAH BINDING BEHIND A PROXY, INFORM THE DETAILS HERE ----------------
	private static final String hostAddr = "";
	private static final String portNo = "";
	//--------------------------------------------------------------------------------------------------------------------	


	//--------------------------- INFORM IF YOU WANT TO UPDATE VANSAH HERE -----------------------------------------------
	// 0 = NO RESULTS WILL BE SENT TO VANSAH
	// 1 = RESULTS WILL BE SENT TO VANSAH
	private static final String updateVansah = "1";
	//--------------------------------------------------------------------------------------------------------------------	


	//--------------------------------------------------------------------------------------------------------------------
	private String TESTFOLDERS_ID;  //Mandatory (GUID Test folder Identifer) Optional if issue_key is provided
	private String JIRA_ISSUE_KEY;  //Mandatory (JIRA ISSUE KEY) Optional if Test Folder is provided
	private String SPRINT_NAME; //Mandatory (SPRINT KEY)
	private String CASE_KEY;   //CaseKey ID (Example - TEST-C1) Mandatory
	private String RELEASE_NAME;  //Release Key (JIRA Release/Version Key) Mandatory
	private String ENVIRONMENT_NAME; //Enivronment ID from Vansah for JIRA app. (Example SYS or UAT ) Mandatory
	private int RESULT_KEY;    // Result Key such as (Result value. Options: (0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested)) Mandatory
	private boolean SEND_SCREENSHOT;   // true or false If Required to take a screenshot of the webPage that to be tested.
	private String COMMENT;  //Actual Result 	
	private int STEP_ORDER;   //Test Step index	
	private String TEST_RUN_IDENTIFIER; //To be generated by API request
	private String TEST_LOG_IDENTIFIER; //To be generated by API request
	private String FILE;
	private File image;
	private HttpClientBuilder clientBuilder;
	private CredentialsProvider credsProvider;
	private Map<String, String> headers = new HashMap<>();
	private HashMap<String,Integer> resultAsName = new HashMap<>();
	private JSONObject requestBody = null;

	/**
	 * @param tESTFOLDERS_ID
	 * @param jIRA_ISSUE_KEY
	 * @param SPRINT_NAME
	 * @param RELEASE_NAME
	 * @param ENVIRONMENT_NAME
	 */
	//------------------------ VANSAH INSTANCE CREATION---------------------------------------------------------------------------------
	//Creates an Instance of vansahnode, to set TESTFOLDERS_ID and to set JIRA_ISSUEKEY
	public VansahNode (String TESTFOLDERS_ID, String JIRA_ISSUE_KEY) {
		super();
		this.TESTFOLDERS_ID = TESTFOLDERS_ID;
		this.JIRA_ISSUE_KEY = JIRA_ISSUE_KEY;
		//Creating key Object Pair for Test Result
		resultAsName.put("NA",0);
		resultAsName.put("FAILED",1);
		resultAsName.put("PASSED",2);
		resultAsName.put("UNTESTED",3);
	}
	public VansahNode() {
		super();
		//Creating key Object Pair for Test Result
		resultAsName.put("NA",0);
		resultAsName.put("FAILED",1);
		resultAsName.put("PASSED",2);
		resultAsName.put("UNTESTED",3);
	}


	//------------------------ VANSAH ADD TEST RUN(TEST RUN IDENTIFIER CREATION) -------------------------------------------
	//POST prod.vansahnode.app/api/v1/run --> https://apidoc.vansah.com/#0ebf5b8f-edc5-4adb-8333-aca93059f31c
	//creates a new test run Identifier which is then used with the other testing methods: 1) add_test_log 2) remove_test_run

	//For JIRA ISSUES
	public void addTestRunFromJIRAIssue(String testcase) throws Exception {
		this.CASE_KEY = testcase;
		this.SEND_SCREENSHOT = false;
		connectToVansahRest("addTestRunFromJIRAIssue", null);
	}

	//For TestFolders
	public void addTestRunFromTestFolder(String testcase) throws Exception {
		this.CASE_KEY = testcase;
		this.SEND_SCREENSHOT = false;
		connectToVansahRest("addTestRunFromTestFolder", null);
	}
	//------------------------------------------------------------------------------------------------------------------------



	//-------------------------- VANSAH ADD TEST LOG (LOG IDENTIFIER CREATION ------------------------------------------------
	//POST prod.vansahnode.app/api/v1/logs --> https://apidoc.vansah.com/#8cad9d9e-003c-43a2-b29e-26ec2acf67a7
	//adds a new test log for the test case_key. Requires "test_run_identifier" from add_test_run

	public void addTestLog(int result, String comment, Integer testStepRow, boolean sendScreenShot, WebDriver driver) throws Exception {

		//0 = N/A, 1 = FAIL, 2 = PASS, 3 = Not tested
		this.RESULT_KEY = result;
		this.COMMENT = comment;
		this.STEP_ORDER = testStepRow;
		this.SEND_SCREENSHOT = sendScreenShot;
		connectToVansahRest("addTestLog", driver);
	}
	public void addTestLog(String result, String comment, Integer testStepRow, boolean sendScreenShot, WebDriver driver) throws Exception {

		//0 = N/A, 1 = FAILED, 2 = PASSED, 3 = UNTESTED

		this.RESULT_KEY = resultAsName.get(result.toUpperCase());
		this.COMMENT = comment;
		this.STEP_ORDER = testStepRow;
		this.SEND_SCREENSHOT = sendScreenShot;
		connectToVansahRest("addTestLog", driver);
	}
	//-------------------------------------------------------------------------------------------------------------------------



	//------------------------- VANSAH ADD QUICK TEST --------------------------------------------------------------------------
	//POST prod.vansahnode.app/api/v1/run --> https://apidoc.vansah.com/#0ebf5b8f-edc5-4adb-8333-aca93059f31c
	//creates a new test run and a new test log for the test case_key. By calling this endpoint, 
	//you will create a new log entry in Vansah with the respective overal Result. 
	//(0 = N/A, 1= FAIL, 2= PASS, 3 = Not Tested). Add_Quick_Test is useful for test cases in which there are no steps in the test script, 
	//where only the overall result is important.

	//For JIRA ISSUES
	public void addQuickTestFromJiraIssue(String testcase, int result,String comment, boolean sendScreenShot, WebDriver driver) throws Exception {

		//0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested
		this.CASE_KEY = testcase;
		this.RESULT_KEY = result;
		this.COMMENT = comment;
		this.SEND_SCREENSHOT = sendScreenShot;
		connectToVansahRest("addQuickTestFromJiraISSUE", driver);
	}
	//For TestFolders
	public void addQuickTestFromTestFolders(String testcase, int result,String comment, boolean sendScreenShot, WebDriver driver) throws Exception {

		//0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested
		this.CASE_KEY = testcase;
		this.RESULT_KEY = result;
		this.COMMENT = comment;
		this.SEND_SCREENSHOT = sendScreenShot;
		connectToVansahRest("addQuickTestFromTestFolders", driver);
	}

	//------------------------------------------------------------------------------------------------------------------------------


	//------------------------------------------ VANSAH REMOVE TEST RUN *********************************************
	//POST prod.vansahnode.app/api/v1/run/{{test_run_identifier}} --> https://apidoc.vansah.com/#2f004698-34e9-4097-89ab-759a8d86fca8
	//will delete the test log created from add_test_run or add_quick_test

	public void removeTestRun() throws Exception {
		connectToVansahRest("removeTestRun", null);
	}
	//------------------------------------------------------------------------------------------------------------------------------

	//------------------------------------------ VANSAH REMOVE TEST LOG *********************************************
	//POST remove_test_log https://apidoc.vansah.com/#789414f9-43e7-4744-b2ca-1aaf9ee878e5
	//will delete a test_log_identifier created from add_test_log or add_quick_test

	public void removeTestLog() throws Exception {	
		connectToVansahRest("removeTestLog", null);
	}
	//------------------------------------------------------------------------------------------------------------------------------


	//------------------------------------------ VANSAH UPDATE TEST LOG ------------------------------------------------------------
	//POST update_test_log https://apidoc.vansah.com/#ae26f43a-b918-4ec9-8422-20553f880b48
	//will perform any updates required using the test log identifier which is returned from add_test_log or add_quick_test

	public void updateTestLog(int result, String comment, boolean sendScreenShot, WebDriver driver) throws Exception {

		//0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested
		this.RESULT_KEY = result;
		this.COMMENT = comment;
		this.SEND_SCREENSHOT = sendScreenShot;
		connectToVansahRest("updateTestLog", driver);
	}
	public void updateTestLog(String result, String comment, boolean sendScreenShot, WebDriver driver) throws Exception {

		//0 = N/A, 1= FAIL, 2= PASS, 3 = Not tested
		this.RESULT_KEY = resultAsName.get(result.toUpperCase());
		this.COMMENT = comment;
		this.SEND_SCREENSHOT = sendScreenShot;
		connectToVansahRest("updateTestLog", driver);
	}
	//----------------------------------------------VANSAH GET TEST SCRIPT-----------------------------------------------------------
	//GET test_script https://apidoc.vansah.com/#91fe16a8-b2c4-440a-b5e6-96cb15f8e1a3
	//Returns the test script for a given case_key

	public int testStepCount(String case_key) {


		try {
			headers.put("Authorization",VANSAH_TOKEN);
			headers.put("Content-Type","application/json");

			clientBuilder = HttpClientBuilder.create();
			// Detecting if the system using any proxy setting.


			if (hostAddr.equals("") && portNo.equals("")) {
				Unirest.setHttpClient(clientBuilder.build());
			} else {
				System.out.println("Proxy Server");
				credsProvider = new BasicCredentialsProvider();
				clientBuilder.useSystemProperties();
				clientBuilder.setProxy(new HttpHost(hostAddr, Integer.parseInt(portNo)));
				clientBuilder.setDefaultCredentialsProvider(credsProvider);
				clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
				Unirest.setHttpClient(clientBuilder.build());
			}
			HttpResponse<JsonNode> get;
			get = Unirest.get(TEST_SCRIPT).headers(headers).queryString("caseKey", case_key).asJson();
			if (get.getBody().toString().equals("[]")) {
				System.out.println("Unexpected Response From Server: " + get.getBody().toString());
			} else {
				JSONObject jsonobjInit = new JSONObject(get.getBody().toString());
				boolean success = jsonobjInit.getBoolean("success");
				String vansah_message = jsonobjInit.getString("message");

				if (success) {

					int testRows = jsonobjInit.getJSONArray("data").length();
					System.out.println("NUMBER OF STEPS: " + testRows);
					return testRows;

				} else {
					System.out.println("Error - Response From Vansah: " + vansah_message);
					return 0;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;

	}

	//******************** MAIN METHOD THAT CONNECTS TO VANSAH (CENTRAL PLACE FOR QUICK TEST AND QUICK TEST UPDATE) ******************************************
	private void connectToVansahRest(String type, WebDriver driver) {

		//Define Headers here
		headers.put("Authorization",VANSAH_TOKEN);
		headers.put("Content-Type","application/json");
		HttpResponse<JsonNode> jsonRequestBody = null;

		if (updateVansah.equals("1")) {

			try {
				clientBuilder = HttpClientBuilder.create();
				// Detecting if binder is being used behind any proxy setting.
				if (hostAddr.equals("") && portNo.equals("")) {
					Unirest.setHttpClient(clientBuilder.build());
				} else {
					System.out.println("Proxy Server");credsProvider = new BasicCredentialsProvider();
					clientBuilder.useSystemProperties();clientBuilder.setProxy(new HttpHost(hostAddr, Integer.parseInt(portNo)));
					clientBuilder.setDefaultCredentialsProvider(credsProvider); clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
					Unirest.setHttpClient(clientBuilder.build());
				}

				if (SEND_SCREENSHOT) {
					try {
						System.out.print("Taking screenshot...");
						WebDriver augmentedDriver = new Augmenter().augment(driver);
						image = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
						String encodstring = encodeFileToBase64Binary(image);
						FILE = encodstring;
						System.out.println("Screenshot succesfully taken.");
					} catch (Exception e) {
						System.out.println("Taking Screenshot failed: " + e.toString());
					}
				}


				if(type == "addTestRunFromJIRAIssue") {

					requestBody = new JSONObject();
					requestBody.accumulate("case", testCase());
					requestBody.accumulate("asset", jiraIssueAsset());
					if(properties().length()!=0) {
						requestBody.accumulate("properties", properties());
					}

					//System.out.print(requestBody);

					jsonRequestBody = Unirest.post(ADD_TEST_RUN).headers(headers).body(requestBody).asJson();

				}
				if(type == "addTestRunFromTestFolder") {
					requestBody = new JSONObject();
					requestBody.accumulate("case", testCase());
					requestBody.accumulate("asset", testFolderAsset());
					if(properties().length()!=0) {
						requestBody.accumulate("properties", properties());
					}

					//System.out.print(requestBody);

					jsonRequestBody = Unirest.post(ADD_TEST_RUN).headers(headers).body(requestBody).asJson();

				}


				if(type == "addTestLog") {
					requestBody =  addTestLogProp();
					if(SEND_SCREENSHOT) {
						
						requestBody.append("attachments", addAttachment(FILE));
						//System.out.println(requestBody);
					}
					//System.out.println(requestBody);
					jsonRequestBody = Unirest.post(ADD_TEST_LOG).headers(headers).body(requestBody).asJson();
				}


				if(type == "addQuickTestFromJiraISSUE") {

					requestBody = new JSONObject();
					requestBody.accumulate("case", testCase());
					requestBody.accumulate("asset", jiraIssueAsset());
					if(properties().length()!=0) {
						requestBody.accumulate("properties", properties());
					}
					requestBody.accumulate("result", resultObj(RESULT_KEY));
					if(SEND_SCREENSHOT) {
						requestBody.append("attachments", addAttachment(FILE));
					}
					requestBody.accumulate("actualResult", COMMENT);;
					
					//System.out.println(requestBody);

					jsonRequestBody = Unirest.post(ADD_TEST_RUN).headers(headers).body(requestBody).asJson();
				}
				if(type == "addQuickTestFromTestFolders") {
					requestBody = new JSONObject();
					requestBody.accumulate("case", testCase());
					requestBody.accumulate("asset", testFolderAsset());
					if(properties().length()!=0) {
						requestBody.accumulate("properties", properties());
					}
					requestBody.accumulate("result", resultObj(RESULT_KEY));
					if(SEND_SCREENSHOT) {
						requestBody.append("attachments", addAttachment(FILE));
					}
					requestBody.accumulate("actualResult", COMMENT);;
					
					//System.out.println(requestBody);


					jsonRequestBody = Unirest.post(ADD_TEST_RUN).headers(headers).body(requestBody).asJson();
				}


				if(type == "removeTestRun") {
					jsonRequestBody = Unirest.delete(REMOVE_TEST_RUN+TEST_RUN_IDENTIFIER).headers(headers).asJson();
				}


				if(type == "removeTestLog") {
					jsonRequestBody = Unirest.delete(REMOVE_TEST_LOG+TEST_LOG_IDENTIFIER).headers(headers).asJson();
				}


				if(type == "updateTestLog") {
					requestBody = new JSONObject();
					requestBody.accumulate("result", resultObj(RESULT_KEY));
					requestBody.accumulate("actualResult", COMMENT);
					if(SEND_SCREENSHOT) {
						requestBody.append("attachments", addAttachment(FILE));
					}
					//System.out.println(requestBody);
					jsonRequestBody = Unirest.put(UPDATE_TEST_LOG+TEST_LOG_IDENTIFIER).headers(headers).body(requestBody).asJson();
				}


				JSONObject fullBody = jsonRequestBody.getBody().getObject();
				if (jsonRequestBody.getBody().toString().equals("[]")) {
					System.out.println("Unexpected Response From Vansah with empty response: " + jsonRequestBody.getBody().toString());


				} else {
					JSONObject jsonobjInit = new JSONObject(jsonRequestBody.getBody().toString());
					boolean success = jsonobjInit.getBoolean("success");
					String vansah_message = jsonobjInit.getString("message");
					System.out.println("(" + StringUtils.capitalize(type) + ") Return: " + success + ". Vansah Message: " + vansah_message );

					if (success){

						if(type == "addTestRunFromJIRAIssue") {
							TEST_RUN_IDENTIFIER = fullBody.getJSONObject("data").getJSONObject("run").get("identifier").toString();
							System.out.println("Test Run Identifier: " + TEST_RUN_IDENTIFIER);
						}
						if(type == "addTestRunFromTestFolder") {
							TEST_RUN_IDENTIFIER = fullBody.getJSONObject("data").getJSONObject("run").get("identifier").toString();
							System.out.println("Test Run Identifier: " + TEST_RUN_IDENTIFIER);
						}

						if(type == "addTestLog") {
							TEST_LOG_IDENTIFIER = fullBody.getJSONObject("data").getJSONObject("log").get("identifier").toString();
							System.out.println("Test Log Identifier: " + TEST_LOG_IDENTIFIER);
						}

					}else{
						System.out.println("Response From Vansah: " + vansah_message);					
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	//To convert Image to base64
	private static String encodeFileToBase64Binary(File file) {
		String encodedfile = null;
		try {
			@SuppressWarnings("resource")
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = Base64.getEncoder().encodeToString(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return encodedfile;
	}
	
	//Setter and Getter's 
	
	//To Set the TestFolderID 
	public void setTESTFOLDERS_ID(String tESTFOLDERS_ID) {
		this.TESTFOLDERS_ID = tESTFOLDERS_ID;
	}
	
	//To Set the JIRA_ISSUE_KEY
	public void setJIRA_ISSUE_KEY(String jIRA_ISSUE_KEY) {
		this.JIRA_ISSUE_KEY = jIRA_ISSUE_KEY;
	}
	
	//To Set the SPRINT_NAME
	public void setSPRINT_NAME(String SPRINT_NAME) {
		this.SPRINT_NAME = SPRINT_NAME;
	}

	//To Set the RELEASE_NAME
	public void setRELEASE_NAME(String RELEASE_NAME) {
		this.RELEASE_NAME = RELEASE_NAME;
	}

	//To Set the ENVIRONMENT_NAME
	public void setENVIRONMENT_NAME(String ENVIRONMENT_NAME) {
		this.ENVIRONMENT_NAME = ENVIRONMENT_NAME;
	}

	//JSONObject - Test Run Properties 
	private JSONObject properties(){
		JSONObject environment = new JSONObject();
		environment.accumulate("name",ENVIRONMENT_NAME);

		JSONObject release = new JSONObject();
		release.accumulate("name",RELEASE_NAME);

		JSONObject sprint = new JSONObject();
		sprint.accumulate("name",SPRINT_NAME);

		JSONObject Properties = new JSONObject();
		if(SPRINT_NAME!=null) {
			if(SPRINT_NAME.length()>=2) {
				Properties.accumulate("sprint", sprint);
			}
		}
		if(RELEASE_NAME!=null) {
			if(RELEASE_NAME.length()>=2) {
				Properties.accumulate("release", release);
			}
		}
		if(ENVIRONMENT_NAME!=null) {
			if(ENVIRONMENT_NAME.length()>=2) {
				Properties.accumulate("environment", environment);
			}
		}

		if(Properties.length()==0) {
			System.out.println("Test Run Properties is null");
		}
		else {
			System.out.println("Test Run Properties is added successfully with the Request");
		}

		return Properties;
	}


	//JSONObject - To add TestCase Key
	private JSONObject testCase() {

		JSONObject testCase = new JSONObject();
		if(CASE_KEY!=null) {
			if(CASE_KEY.length()>=2) {
				testCase.accumulate("key", CASE_KEY);
			}
		}
		else {
			System.out.println("Please Provide Valid TestCase Key");
		}

		return testCase;
	}
	//JSONObject - To add Result ID
	private JSONObject resultObj(int result) {
		
		JSONObject resultID = new JSONObject();
		
		resultID.accumulate("id", result);
		
		
		return resultID;
	}
	//JSONObject - To add JIRA Issue name
	private JSONObject jiraIssueAsset() {

		JSONObject asset = new JSONObject();
		if(JIRA_ISSUE_KEY!=null){
			if(JIRA_ISSUE_KEY.length()>=2) {
				asset.accumulate("type", "issue");
				asset.accumulate("key", JIRA_ISSUE_KEY);
			}
		}
		else {
			System.out.println("Please Provide Valid JIRA Issue Key");
		}


		return asset;
	}
	//JSONObject - To add TestFolder ID 
	private JSONObject testFolderAsset() {

		JSONObject asset = new JSONObject();
		if(TESTFOLDERS_ID!=null){
			if(TESTFOLDERS_ID.length()>=2) {
				asset.accumulate("type", "folder");
				asset.accumulate("identifier", TESTFOLDERS_ID);
			}
		}
		else {
			System.out.println("Please Provide Valid TestFolder ID");
		}


		return asset;
	}

	//JSONObject - To addTestLog
	private JSONObject addTestLogProp() {

		JSONObject testRun = new JSONObject();
		testRun.accumulate("identifier", TEST_RUN_IDENTIFIER);

		JSONObject stepNumber = new JSONObject();
		stepNumber.accumulate("number", STEP_ORDER);

		JSONObject testResult = new JSONObject();
		testResult.accumulate("id", RESULT_KEY);

		JSONObject testLogProp = new JSONObject();

		testLogProp.accumulate("run", testRun);

		testLogProp.accumulate("step", stepNumber);

		testLogProp.accumulate("result", testResult);

		testLogProp.accumulate("actualResult", COMMENT);


		return testLogProp;
	}
	//JSONObject - To Add Add Attachments to a Test Log
	private JSONObject addAttachment(String FILE) {

		JSONObject attachmentsInfo = new JSONObject();
		attachmentsInfo.accumulate("name", FileName());
		attachmentsInfo.accumulate("extension", "png");
		//System.out.println(attachmentsInfo);
		attachmentsInfo.accumulate("file", FILE);

		return attachmentsInfo;

	}
	//To create FileName of the Screenshot with Date TimeStamp
	private String FileName() {

		String fileName = "";
		long millis = System.currentTimeMillis();
		String datetime = LocalDateTime.now().toString();

		datetime = datetime.replace(" ", "");
		datetime = datetime.replace(":", "");
		String rndchars = RandomStringUtils.randomAlphanumeric(16);
		fileName = rndchars + "_" + datetime + "_" + millis;


		return fileName;
	}
	
}

