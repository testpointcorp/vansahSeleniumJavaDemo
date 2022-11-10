## Vansah Automation Demo

This Selenium TestNG Automation framework is just a demo and quick start to Automated Testing with Vansah - Test Management for Jira.

## Introduction

Vansah's API can be used to integrate Vansah with various tools, automation and development frameworks including third-party applications. The main usage for the API is to allow our customers to integrate their automated tests and submit test results to Vansah Test Management for Jira. For a full detail of what other tasks you can perform using the API the list can be obtained from this documentation. For more information on API token required for connecting to Jira see: https://community.vansah.com/posts/how-to-generate-a-vansah-api-token-from-jira


## Tools
<ul>
  <li>Maven - Build Tool</li>
  <li>TestNG</li>
  <li>Selenium 3</li>
  <li>ChromeDriver v107.x.x.x</li>
</ul>

## How to Run

### Step 1
  
    Clone or Download the Repo.
  
### Step 2
      
    Open Eclipse and Import the Project as Maven Project.
    
### Step 3

    Add VansahConnectToken in VansahNode.java File
    Token should be obtained from your Jira Workspace
    
<a href="https://community.vansah.com/posts/how-to-generate-a-vansah-api-token-from-jira">Click to Know How to obtain your Vansah API Connect Token</a>

### Step 4

    Replace All the Project Info in Tests.java file
    
### Step 5

    Click on TestNg.xml and Run as TestNG suite
    


    

