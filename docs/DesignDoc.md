---
geometry: margin=1in
---
# PROJECT Design Documentation

> _The following template provides the headings for your Design
> Documentation.  As you edit each section make sure you remove these
> commentary 'blockquotes'; the lines that start with a > character
> and appear in the generated PDF in italics but do so only **after** all team members agree that the requirements for that section and current Sprint have been met. **Do not** delete future Sprint expectations._

## Team Information
* Team name: Dangerously dy/dx
* Team members
  * Amber Goldhagen
  * Marissa Gomes
  * Kat Delaney
  * Kaleb Stanzak
  * Trevor Card
  * Brandon Troost

## Executive Summary

We are Team Dangerously dy/dx and we are developing an app called Shelter Saviors. The mission of Shelter Saviors is to connect animal shelters in-need with donations and volunteers who want to support these non-profit organizations financially, materially, and physically.

### Purpose
>  _**[Sprint 2 & 4]** Provide a very brief statement about the project and the most
> important user group and user goals._
>

The main purpose of the project is to give shelters a platform to receive donations.
The most important user groups are U-fund/shelter admins/managers and general users/donators.

### Glossary and Acronyms
> _**[Sprint 2 & 4]** Provide a table of terms and acronyms._

| Term | Definition |
|------|------------|
| SPA | Single Page |
| MVP | Minimum Viable Product |


## Requirements

This section describes the features of the application.

> _In this section you do not need to be exhaustive and list every
> story.  Focus on top-level features from the Vision document and
> maybe Epics and critical Stories._

### Definition of MVP
> _**[Sprint 2 & 4]** Provide a simple description of the Minimum Viable Product._

The MVP is a website that lets admins/managers add, edit and remove needs to their shelter's cupboard and then allows supporters to add these needs to their basket and pay for those needs.

### MVP Features
>  _**[Sprint 4]** Provide a list of top-level Epics and/or Stories of the MVP._

### Enhancements
> _**[Sprint 4]** Describe what enhancements you have implemented for the project._


## Application Domain

This section describes the application domain.

![Domain Model](Domain%20Analysis%20Swen%20261%20(Team).png)

> _**[Sprint 2 & 4]** Provide a high-level overview of the domain for this application. You
> can discuss the more important domain entities and their relationship
> to each other._


## Architecture and Design

This section describes the application architecture.

### Summary

The following Tiers/Layers model shows a high-level view of the webapp's architecture. 
**NOTE**: detailed diagrams are required in later sections of this document.
> _**[Sprint 1]** (Augment this diagram with your **own** rendition and representations of sample system classes, placing them into the appropriate M/V/VM (orange rectangle) tier section. Focus on what is currently required to support **Sprint 1 - Demo requirements**. Make sure to describe your design choices in the corresponding _**Tier Section**_ and also in the _**OO Design Principles**_ section below.)_

![The Tiers & Layers of the Architecture](Software%20Architecture.png)

The web application, is built using the Model–View–ViewModel (MVVM) architecture pattern. 

The Model stores the application data objects including any functionality to provide persistance. 

The View is the client-side SPA built with Angular utilizing HTML, CSS and TypeScript. The ViewModel provides RESTful APIs to the client (View) as well as any logic required to manipulate the data objects from the Model.

Both the ViewModel and Model are built using Java and Spring Framework. Details of the components within these tiers are supplied below.


### Overview of User Interface

This section describes the web interface flow; this is how the user views and interacts with the web application.

> _Provide a summary of the application's user interface.  Describe, from the user's perspective, the flow of the pages in the web application._


### View Tier
> _**[Sprint 4]** Provide a summary of the View Tier UI of your architecture.
> Describe the types of components in the tier and describe their
> responsibilities.  This should be a narrative description, i.e. it has
> a flow or "story line" that the reader can follow._

> _**[Sprint 4]** You must  provide at least **2 sequence diagrams** as is relevant to a particular aspects 
> of the design that you are describing.  (**For example**, in a shopping experience application you might create a 
> sequence diagram of a customer searching for an item and adding to their cart.)
> As these can span multiple tiers, be sure to include an relevant HTTP requests from the client-side to the server-side 
> to help illustrate the end-to-end flow._

![Sequence Diagram 1](Sequence%20diagram%201%20need%20to%20basket.png)

![Sequence Diagram 2](Sequence%20diagram%202%20manager%20create%20need.png)
> _**[Sprint 4]** To adequately show your system, you will need to present the **class diagrams** where relevant in your design. Some additional tips:_
 >* _Class diagrams only apply to the **ViewModel** and **Model** Tier_
>* _A single class diagram of the entire system will not be effective. You may start with one, but will be need to break it down into smaller sections to account for requirements of each of the Tier static models below._
 >* _Correct labeling of relationships with proper notation for the relationship type, multiplicities, and navigation information will be important._
 >* _Include other details such as attributes and method signatures that you think are needed to support the level of detail in your discussion._

### ViewModel Tier
> _**[Sprint 1]** List the classes supporting this tier and provide a description of there purpose._

> _**[Sprint 4]** Provide a summary of this tier of your architecture. This
> section will follow the same instructions that are given for the View
> Tier above._

> _At appropriate places as part of this narrative provide **one** or more updated and **properly labeled**
> static models (UML class diagrams) with some details such as associations (connections) between classes, and critical attributes and methods. (**Be sure** to revisit the Static **UML Review Sheet** to ensure your class diagrams are using correct format and syntax.)_
> 
![Replace with your ViewModel Tier class diagram 1, etc.](uml%20basket%20controller.png)
![Replace with your ViewModel Tier class diagram 1, etc.](uml%20need%20controller.png)
![Replace with your ViewModel Tier class diagram 1, etc.](uml%20user%20controller.png)

### Model Tier
> _**[Sprint 1]** List the classes supporting this tier and provide a description of there purpose._

> _**[Sprint 2, 3 & 4]** Provide a summary of this tier of your architecture. This
> section will follow the same instructions that are given for the View
> Tier above._.


> _At appropriate places as part of this narrative provide **one** or more updated and **properly labeled**
> static models (UML class diagrams) with some details such as associations (connections) between classes, and critical attributes and methods. (**Be sure** to revisit the Static **UML Review Sheet** to ensure your class diagrams are using correct format and syntax.)_
> 
![Replace with your Model Tier class diagram 1, etc.](uml%20users.png)
![Replace with your Model Tier class diagram 1, etc.](uml%20basket%20persistence.png)
![Replace with your Model Tier class diagram 1, etc.](uml%20basket.png)
![Replace with your Model Tier class diagram 1, etc.](uml%20needfileDAO.png)
![Replace with your Model Tier class diagram 1, etc.](uml%20needDAO.png)
![Replace with your Model Tier class diagram 1, etc.](uml%20need.png)

## OO Design Principles

> _**[Sprint 1]** Name and describe the initial OO Principles that your team has considered in support of your design (and implementation) for this first Sprint._

<div style="display: flex; align-items: center;">
  <img src="uml%20need%20controller.png" alt="Object Oriented Design Principles" style="float: left; margin-right: 10px; max-width: 200px;">
<p>The Object Oriented design principles we considered in Sprint 1 are Single Responsibility and Open/Closed. Single Responsibility is demonstrated by our NeedController class which has one job only: to handle REST API/http commands !
and responses accordingly. Our Need Controller class is also an example of Open/Closed; this class's exisitng methodsmay be extended to fit more use cases or data types but they will not be modified to perform other actions entirely.</p>

> _**[Sprint 2, 3 & 4]** Will eventually address upto **4 key OO Principles** in your final design. Follow guidance in augmenting those completed in previous Sprints as indicated to you by instructor. Be sure to include any diagrams (or clearly refer to ones elsewhere in your Tier sections above) to support your claims._

<div style="display: flex; align-items: center;">
  <img src="information%20expert%20example.png" alt="Object Oriented Design Principles" style="float: left; margin-right: 10px; max-width: 200px;">
<p>The Information Expert principle suggests that the responsiblity of managing and processing informaiton should lie with the class that has the necessary information. This principle helps in organizing the code logically, ensuring that methods are placed in the classes that own the ate. In our system, the Helper class has its own basket. The emptyBasket() method, which replaces a Helper's basket with an empty array, is rightly placed within the Helper class. This adheres to the Information Expert principe as the Helper class has the necessary information (the basket) to perform the operation.</p> 

<div style="display: flex; align-items: center;">
  <img src="uml%20user%20controller.png" alt="Object Oriented Design Principles" style="float: left; margin-right: 10px; max-width: 200px;">
<p>The Dependency Inversion principle states that high-level modules should not depend on low-level modules, instead depending on abstractions. Details should also depend on abstractions. This principle helps in reducing the dependency between classes. Making the system more flexible. Our project has a UserController managing user actions and interacting with a User model, so the controller relies on abstraction rather than a concrete implementation of the User class. This ensures that changes in the User class do not directly affect the UserController.</p> 

<div style="display: flex; align-items: center;">
  <img src="low%20coupling%20example.png" alt="Object Oriented Design Principles" style="float: left; margin-right: 10px; max-width: 200px;">
<p>The Low Coupling principle outlines that classes in a system should have minimal dependencies on each other, which makes the system easier to maintain. In our system, each class interacts with others through interfaces. For example, the User class interacts with the Admin/U-fund Manager and Helper classes for specific tasks like logging in and managing needs. This helps limit the dependencies between classes, ensuring changes in one class have minimal impact on others. </p>

<div style="display: flex; align-items: center;">
  <img src="law%20of%20demeter%20example.png" alt="Object Oriented Design Principles" style="float: left; margin-right: 10px; max-width: 200px;">
<p>The Law of Demeter principle suggests that an object should only interact with its direct neighbors (objects that it directly references). In our system, the User class interacts with the Admin/U-fund Manager and Helper classes, but not with the Need or Funding Basket classes. Similarly, the Funding Basket class interacts with the Need and Checkout classes, adhering to the principle.</p> 

<div style="display: flex; align-items: center;">
  <img src="controller%20example.png" alt="Object Oriented Design Principles" style="float: left; margin-right: 10px; max-width: 200px;">
<p>The Controller principle handles system events and coordinates the actions of other classes or objects. In our system the NeedController class handles all of the process for finding, removing, or updating needs. 
> _**[Sprint 3 & 4]** OO Design Principles should span across **all tiers.**_

## Static Code Analysis/Future Design Improvements
> _**[Sprint 4]** With the results from the Static Code Analysis exercise, 
> **Identify 3-4** areas within your code that have been flagged by the Static Code 
> Analysis Tool (SonarQube) and provide your analysis and recommendations.  
> Include any relevant screenshot(s) with each area._

**(2024/12/4): Sprint 4**
![Replace with sprint 4 static code analysis, etc.](sprint%204%20static%20code%20analysis.png)

The ufund-api project has 94.8% coverage, indicating that most of the code is covered by tests, reducing the chances of untested functionality.
However, the ufund-ui project currently lacks test coverage and is at 0%, but this is the JavaScript/HTML/CSS part of the code, so this can be passed off.
The ufund-api reliability score is C with 4 identified issues, marking reliability as a primary concern for our project. This is because of logic issues in areas and error-prone code in Users, which is critical to our project. Likewise, the ufund-ui reliability score is C with 1 identified issue, again making reliability a concern for similar reasons. 
On the bright side, both our frontend and backend have an A for maintainability. This means we have well-structured code. 
In terms of hotspots, there have been some flagged in ufund-api due to a lack of proper error handling and areas that are prone to bugs. Ufund-ui does not have any flagged hotspots, however, due to the lack of coverage, there may be potential issues that remain hidden. 
Ufund-api has 0% code duplication meaning we have no redundant code, which is good. But, ufund-ui has 2% code duplication, which is a slight issue. Having redundant code increases the chance of inconsistencies and requires more maintenance effort.

> _**[Sprint 4]** Discuss **future** refactoring and other design improvements your team would explore if the team had additional time._

## Testing
> _This section will provide information about the testing performed
> and the results of the testing._

### Acceptance Testing
> _**[Sprint 2 & 4]** Report on the number of user stories that have passed all their
> acceptance criteria tests, the number that have some acceptance
> criteria tests failing, and the number of user stories that
> have not had any testing yet. Highlight the issues found during
> acceptance testing and if there are any concerns._

### Unit Testing and Code Coverage
> _**[Sprint 4]** Discuss your unit testing strategy. Report on the code coverage
> achieved from unit testing of the code base. Discuss the team's
> coverage targets, why you selected those values, and how well your
> code coverage met your targets._

>_**[Sprint 2, 3 & 4]** **Include images of your code coverage report.** If there are any anomalies, discuss
> those._
**(2024/10/31): Sprint 2**
![Replace with sprint 2 code coverage, etc.](code%20coverage%20sprint%202.png)

**(2024/11/18): Sprint 3**
![Replace with sprint 3 code coverage api, etc.](sprint%203%20code%20coverage%20api.png)
![Replace with sprint 3 code coverage ufundapi, etc.](sprint%203%20code%20coverage%20ufundapi.png)
![Replace with sprint 3 code coverage users, etc.](sprint%203%20code%20coverage%20users.png)

**(2024/12/4): Sprint 4**
![Replace with sprint 4 code coverage user, etc.](sprint%204%20code%20coverage%20user.png)
![Replace with sprint 4 code coverage usercontroller, etc.](sprint%204%20code%20coverage%20usercontroller.png)
![Replace with sprint 4 code coverage users, etc.](sprint%204%20code%20coverage%20users.png)
![Replace with sprint 4 code coverage usersapi, etc.](sprint%204%20code%20coverage%20usersapi.png)
![Replace with sprint 4 code coverage basket, etc.](sprint%204%20code%20coverage%20basket.png)
![Replace with sprint 4 code coverage basketcontroller, etc.](sprint%204%20code%20coverage%20basketcontroller.png)
![Replace with sprint 4 code coverage needcontroller, etc.](sprint%204%20code%20coverage%20needcontroller.png)
![Replace with sprint 4 code coverage needfiledao, etc.](sprint%204%20code%20coverage%20needfiledao.png)
![Replace with sprint 4 code coverage need, etc.](sprint%204%20code%20coverage%20need.png)
![Replace with sprint 4 code coverage subscriptioncontroller, etc.](sprint%204%20code%20coverage%20subscription%20controller.png)
![Replace with sprint 4 code coverage basketpersistence, etc.](sprint%204%20code%20coverage%20basket%20persistence.png)
![Replace with sprint 4 code coverage ufundapi, etc.](sprint%204%20code%20coverage%20users.png)
![Replace with sprint 4 code coverage api, etc.](sprint%204%20code%20coverage%20api.png)



## Ongoing Rationale
>_**[Sprint 1, 2, 3 & 4]** Throughout the project, provide a time stamp **(yyyy/mm/dd): Sprint # and description** of any _**mayor**_ team decisions or design milestones/changes and corresponding justification._

**(2024/10/01): Sprint 1** 

Based on the project criteria given in the One Stop Shop, our team was under the impression that needs could not be tracked using IDs. Due to this confusion our sprint 1 architecture lacked data persistence and did not use JSON files for individual needs. We have realized our mistake and will be incorporating IDs and JSON into all future iterations of Shelter Saviors. The rest of Sprint 1 was relatively straight forward as we used what we learned from the REST API tutorial to implement the basic cURL commands for adding, editing, removing, and finding needs.

**(2024/10/16): Sprint 2**

Building off of Sprint 1 Rationale, incorporated IDs into all of our REST API methods, and created a data persistence structure for Needs similar to that of the Heroes tutorial.
