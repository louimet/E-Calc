##Meeting Minutes

###FIRST MEETING

   By Friday January 29, 2016
   List of Questions
   List of Interviewees
   Check out GitHub and learn how to use it
   First Deliverable: February 22nd.
§ Initial Set of Persona and use cases 
§ Implementation of Use Cases. (show the implementation of the calculator) 
§ Testing the implementation vs the use cases. 
§ We should have the source code for the calculator. 
§ Include all basic functions (will have to see on that later on) + transcendental ones.

n Check what type of accuracy each person needs.

Think of people to interview.
o Decide on whether the clients want it to flip/be usable on phones/tablets. o https://www.youtube.com/watch?v=RRXe1omEGWQ&list=PLD4EF3E3AD055F3C7

Need to have an idea of the users.
Choose people we know and other team members will interview them.
When designing the GUI make sure to cover as much as possible.
Means of communication/Sharing Work

Communication: Slack
   Sharing work: GitHub
   Think of questions to ask

Note from tutorial:
User Cases: Primary Actor: Someone that interacts directly with the uses of the software/hardware. They will benefit from the system. It is the potential user of the system. Secondary Actor: Someone/Something that provides assistance to the system so that it can help the primary actor to achieve its goal.

###MEETING - Friday, January 29th, 2016
Just a quick note to keep track of things...

Laurent will compile the question list

Federico will establish a structure for the repository

Meet on Monday, Feb 1st, to discuss the questionnaire

----End Jan 29th meeting----

###Spontaneous meeting - after-class Devang, Yuanwen and Federico - Tuesday, February 9th, 2016

* yuanwen feels uneasy that we haven't coded anything--Federico suggests we establish a very rough architecture like such:

      * -  GUI - gets input - calls

      *  - Parser - some error checking - calls:

      *  - functions - return value to GUI

   * We can then implement a simple window with one button and one input/output box in android studio (everything seems to point towards an app) with the following elements:

   * - have the button call a parser that reads the string that is input

   *  - have the parser call a silly function that prepends "Hello," and appends a "!"

   *  - Do this for Friday so we can exchange know-how if someone can't get it to work

   * Then, when that is working, we can hook up to our functions, make a nice little GUI, add haptic feedback, rotate screen, etc

* regarding implementing the function, Federico had a couple of suggestions

      * the function can be tested for performance in Java, timewise and error-wise (what sort of error? cumulative?)

      * Matlab, Octave or whatever you want can be used for graphing out different approximations which look good for the docummentation

* We all agree that interviews should happen ASAP to get everything else moving

* Federico proposes/asks that when someone writes something rather large or questions on slack we answer or just aknowledge in the following 24 hrs

* A couple of points were made regarding the implementation, do we initialize many values (certainly pi and e if appliccable but maybe even lookup tables) or just the basics?

   * there are trade-offs regarding user-experiene that can be inluded in the questionnaire:

   * initializing tables on start can lead to longer start-up and higher memory footprint but faster results once initialized and less cpu-usage (better for battery)

   * the converse is also true.

   * This raises another few possible questions: does the user care about battery-drain, bootup-time, memory footprint time for calculating every single operation? Probably nothing too big in terms of difference but might as well ask



###Meeting - Nuns' bldg Devang, Yuanwen and Federico - Friday, February 26th, 2016

Looking through missing requirements for deliverable 2:

* Collaboration patterns used (collaboration patterns, Köppe, et al, 2015, ieee): 

  1. (pattern ii - share expectations) Shared expectations regarding the deliverable.
  2. (pattern iv - fill knowledge gaps) - shared knowledge regarding the usage of github, markdown, Android Studio and some of the algorithms.
  3. (pattern v - centralize work product management) Slack for communication and Github as a repository for code and docummentation.
  4. (pattern vi - manage the project) Discussed project requirements, derived tasks, agreed on the allocation of tasks and elaborated table of responsibilities.
  5. (viii - keep motivated) Worked together and constantly towards the goal of the project.
  6. (ix - start immediately) Met as soon as we were all available and tried to establish as many elements as possible from the onset.
  7. (xi - spread tasks appropriately) Spread tasks fairly and according to individual skills and desires. 

* Outline of strategy:
  1. Limit the sampling frame for potential users (people who use a calculator on a daily basis and might require portability - eg. engineering or finance).
  2. Interviewing respondents, and generating appropriate personae.
  3. Creating an affinity diagram
  4. Finding requirements through interviewing potential users - platform, potential use scenarios, desired features, etc.
  5. Elaborating a use-case.
  6. Choosing functions.
  7. Elicitng the necessary tasks for meeting requirements and implementing a use-case.
  8. Allocating tasks according to individual skills and desires.
  9. Evaluating feasibility of the feature-set for the present iteration
  10. Meeting a first milestone (deliverable 1).
  11. Sharing knowledge pertaining to each individual's tasks so everyone can participate in any given area of development and present the achievements attained thus far (second milestone).
  12. Repeat from number 2 with pertinent adaptations for iteration II and any unanticipated changes.

* Algorithms and pseudocode - TODO - include in the slides remotely.
  
* Data Structures - Expresion evaluation uses a linked list for tokenization, and two stacks: one for operator and one for operands.

* Inclusions:
  1. basic arithmetic calculation
  2. chosen transcendental functions - e^x, x^0.5, Log10(x), 10^x, Sine(x)
  3. parenthesis
  4. input enforcement as a function of previous input
  5. expression evaluation for input
  6. basic user interface
  7. tactile feedback

* Exclusions:
  1. Memory
  2. Access to input history as an alternate view (including copy-paste functionality)
  2. Unit tracking
  3. plus/minus button

* Technical reasons for the above, not enough time to implement

* Code review results:
  1. original 10^x, was using ln(10), implementation separated into ln and 10^x so ln could be used by Log10
  2. Log10 function re-implemented with usage of ln
  
* Test results, add a **fresh** printout of the testing suite's output and octave plot goodies
  
* Data file - none
  
* Presentation slides: generally follow the order of requirements followed, elaborate once all materials have been gathered by Monday
   
* Run through app structure:
  * ~~Things to review, should we be able to input digits after result?~~
  * bug inputing operators after a digit without a dot
  * check if we can get rid ot isOperand with conditions from result
  * when more than 23 digits are input, display will not show latest input (scroll or use scientific notation? set a maximum?)
  * implement plus/minus
  * ~~refactor with functions in a library~~
  * ~~Icon?~~
  * discuss layouts for different devices/orientations - android studio has resources for this.
  
* review and split octave code:
  1. Federico will upload sample code (sine) for octave plotting - part of the work is in choosing data to display (relative error? over exponentially increasing steps? etc.)
  2. YuanWen will have a stab at 10^x, e^x
  3. Devang will try Log10
  4. Federico will have a stab at sqrt
  5. Laurent?
 
  
 ###Meeting - H961 Devang, Yuanwen, Laurent and Federico (Laurent is sick with a fever) - Monday, February 29th, 2016

Upload slide template to Google Drive

Things to include in the presentation:

* Strategy outline
  * outline itself with the following interleaved
  * method of elaborating questionnaire - each pitches in, find some form of categorization, pilot, review
  * Personas - primary and secondary division justification
  * include picture of 10,000 post-its

* Choices and justification - regarding UX, not function implementation
  * Use case - elaborate...
  * UI - familiar layout justification from interviews
  * Flow diagram for input
  * Data structures

* App Structure
  * Class relation - code structure
  * screen - cap

* Includes/Excludes
  * Section of includes
  * Section of excludes to be 
  
* Functions - Display plots for errors, time from testing and/or time complexity analysis

To sum up

Allot around 1.5 minutes per section above, except the last (functions) which gets 4 minutes.

do:

1. Presentation slides

2. Understand each other's function implemenattion and options

3. put together zip file - <Includes> sheet for strategy, sheet for pseudocode, sheet for collab pattrs, doc for test results, sheet for incl/excl

###Meeting - H961 Devang, Yuanwen, and Federico - Wednesday, March 16th, 2016

Come up with tasks...

* contact TA's for ASQ - contact as early as possible, schedule meetig as late as possible

* All - Take ASQ to
	1. Stephane
	2. Tristan
	3. Joey? substitute?

* Create design spec (CRC) for what we have with a modification: change button functions to inputDigit(int i), inputLpar, inputRpar, inputExp, inputPlus, etc and put it in a different class where that class + expressionevaluator are the controller, mainView (and the history-view) is the view and the boolean isZs, the expression, the expression history and the library are the model

* Implemet the revised CRC
 
* All - Read the [oracle code conventions](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html) and, unless the convention raises someone's objections, fit our existing code into the convention. 

* All - do a real cyclical asymmetric code review

* Revammp the UI after reading a bit of his resources... some ideas, borders of elements, color of buttons according to role, two lines for display, top(left-justified) expression, bottom(right-justified) result, change eval to =, etc

* function-member-specific, revamp functions if they're not satisfying

* History pop-up window
  1. open a dummy window
  2. caching expression data (String[]? or a <String> linked-list? queue?)
  3. putting the two together (some decissions to be made, display all expressions with one empty line between them, Use case: choose oe expression, open up in editing window, call standard android keybd, allow edits and verify correctness at end (as much as enforced by the input management) and then press button to inject into a cleared display window (where editing is rather clunky
  
* Memory, implement? 1-slot? include the reference to memory in the expression or the actual contents of memory? limit display precission? - think about this. How should this work? 1-button, only last output? one button for store, one for invoking? more things to think about...

* scrollable display (estimated task: .5 hrs?)
 
* ~~chuck +/- key (5 minutes)~~

* Revise the DOC

* define additions to the DOC and how they fit into the revised format

* Encapsulated prototype of unit tracking? If time permits 

Meet Friday after class? have ideas to discuss regarding the UI, CRC, Memory usage and consolidate what we bring to the meeting in a final usage of memory, a final UI design and do the CRC cards (perhas write crc for current, although we have the UML class diagram in the presentation slides)

###Meeting - H960.6 Devang, Yuanwen, Laurent, Federico and Laurent is sick with a fever) - Tuesday, March 22nd, 2016

* CRC View (History view and and Main view are views, Handle Input and expression evaluator are controller, Expression Buffer and Expression History and Library are model

* New and updated tasks: 
    1. put tasks in github
    2. display implemented as two contiguous windows, top with input allowed and scrollable (no keyboard or scrollbar) but add arrow keys; bottom for displaying results (view only)
    3. implement plus minus key on operand under cursor (only if operand under cursor) and toggle preceding hyphen - see hyphen [here](www.cs.tut.fi/~jkorpela/dashes.html) - __Laurent__
    4. make blocks for gui - __Devang__
    5. find nice gui pallette (almost coincides with blocks) - __Devang__
    6. Find a way to implement memory function and history - __Yuanwen__
    7. Redistribution of functions code - __Federico__
    8. Make changes to user manual __Yuanwen__
    9. Editorial pass on function section in the doc - __Federico__
    10. Revision of doc - __Laurent__
    
###Meeting - H961.1 Devang, Yuanwen, Laurent and Federico - Tuesday, March 29nd, 2016
Checked progress

* Devang merged UI changes
    * will add 'e' key, up down arrows (still need to think of labeling

* YuanWen will merge memory implementation
    * will look into two-line display

* Fede will then merge (after YuanWen) history up down functionality

* Laurent will have a go at plus/minus

* Laurent is finishing up doc
    * Devang will write up doc for gui
    * YuanWen will write doc for mem
    * Fede doc for Architecture and give an editorial pass to function doc (for homogeneity)

* ALL! - prepare ASQ for Friday if possible so we can share


  ###Meeting - H613 Devang, Yuanwen, Laurent and Federico - Friday, April 8, 2016

- Everyone send your ASQ and screenshots of debugging to Laurent.
- Documentation for square root function - Devang
- Documentation for sine, logs, pow - Federico
- Send Federico expressions/cases you tested which broke the app. Values that broke the functions.
- Appendix section for functions (plots, tables) - Yuanwen
- Testing section documentation - Federico
- Asymmetric code review.
- Everyone read the documentation when done.



  
 

  
