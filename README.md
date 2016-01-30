hi guys,
the section on interviews follows the notes Boulos took during our first meeting.
I have moved everything that was in the google document I shared earlier today in this file here.

Laurent

FIRST MEETING
-        By Friday January 29, 2016
 
-        List of Questions
-        List of Interviewees
-        Check out GitHub and learn how to use it
 
-        First Deliverable: February 22nd.
§  Initial Set of Persona and use cases
§  Implementation of Use Cases. (show the implementation of the calculator)
§  Testing the implementation vs the use cases.
§  We should have the source code for the calculator.
§  Include all basic functions (will have to see on that later on) + transcendental ones.
 
n  Check what type of accuracy each person needs.
 
 
-        Think of people to interview.
 
o   Decide on whether the clients want it to flip/be usable on phones/tablets.
o   https://www.youtube.com/watch?v=RRXe1omEGWQ&list=PLD4EF3E3AD055F3C7
 
-        Need to have an idea of the users.
-        Choose people we know and other team members will interview them.
-        When designing the GUI make sure to cover as much as possible.
 
Means of communication/Sharing Work
-        Communication: Slack
-        Sharing work: GitHub
 
-        Think of questions to ask
 
 
User Cases:
Primary Actor: Someone that interacts directly with the uses of the software/hardware. They will benefit from the system. It is the potential user of the system.
Secondary Actor: Someone/Something that provides assistance to the system so that it can help the primary actor to achieve its goal. 
****END FIRST MEETING NOTES******

****INTERVIEWS****

POTENTIAL QUESTIONS:

NOTE : certain questions might seem to lead into the design of a vaster app than just a calculator, but I included them nonetheless. They can yield useful information and maybe we can include some ideas in our presentation, though not implement them.

-        Would an online calculator be appealing to you?
o   If yes, what function would be useful if provided? (saving results, formulas, etc. for sharing across platforms/applications)
-        Would you like your calculator to be configurable to a certain extent?
o   Modify number of decimals?
o   Rearrange buttons to your liking?
o   …
-        What precision do you require of your calculator?
o   Number of decimals
-        In what setting do you typically use a calculator?
o   Open question
o   Sample answers : when working on a computer, on the go on my phone, while working on a tablet, etc.
o   Expand : describe the steps of calculator use
§  Ex. : Scroll page, find icon, tap icon, calculate, switch app, plug in number, come back to calculator, do another calculation, switch app again.
-        What do you use your calculator for?
o   E.g. Simple operations or complex calculations
o   Lots of quick operations or long sequences
§  If long sequences : how would you qualify the tracking on your calculator (i.e. display of calculation so far, backtracking options like CTRL-Z, etc.)
-        How satisfied are you with the current calculator you use
o   Provide scale from 1 to 5
o   Maybe separate this question into 2 : Ease of use and Available functions
-        What’s missing from your calculator that you would like to see?
o   Open question
-        Which calculator do you use more often?
o   Type (physical or app, Name of app)
-        Do you still use a physical calculator?
o   If so, what advantages do you think it has over a calculator program or app?
-        Would unit tracking be a useful function?
o   i.e. instead of multiplying 2 x 2 and knowing it’s centimeters, which yields square centimeters, a unit could be attached to each term, i.e. 2 cm x 2 cm, and the calculator would return 4 square centimeters.  Simple example, but can
-        Would a unit converter be a useful addition to your calculator?
o   E.g. convert square meters in square kilometers, or PSI in mmHg of Bar


POTENTIAL INTERVIEWEES:

Name : Stéphane Munger
Age : 37
Occupation : Mechanical Engineer
Uses calculators on a variety of platforms, including PC, laptop, phone, and “hand-attached” iPad.
Contact : Laurent, friend of mine
Availability : February 1, 3, 5 (Mo, We, Fri) after work (I assume...)
Notes : already approached and interested in participating; lives near me (Laurent), I can easily drop by and do the interview if we decide to cut corners.

-----------------------Following section added by Federico, Jan 29th---------------------------------------
 Candidate interviewees - note, I know (mostly closely
	Robert-Eric Gaskell	Electronics engineer - audio
	Nicolas	Cool		Secondary student 		- Note, he lives far in the suburbs...
	Charles-Eric Jean	Civil Engineer
	-whatshisname-		Acoustician
	Juan Regueiro		Engineer, Student		- Note, these would be skype interviews
	Eugenia O'Reilly	Mathematician			- Note, these would be skype interviews
	
	Post on ENCS boards@Hall, boards@Trottier or McDonald - compensation? lunch?

Possible questions
	Info to draw uppon:
	---persona-related---
		Activity
		Region
		Age group
		Lifestyle
			Workload
			Work environment
			Busy personal lifestyle?
		Possible scenarios for using product ... starts getting into:
	---domain-related----
	Does interviewee use any particular type of calc (sci/graph/tirg/stat)?
	If so, is there any reason this type satisfies the user's needs best?
	If graphs, what particular features are attractive/unatractive to the user?
	Does the user prefer to use a physical device or a software-only implementation?
	Why?
	Most satisfying memory of a calculator usage up-to-date
	Most frustrating memory of a "		"	"
	Anything that interviewee would like to have in an ideal calculator?
	Anything that an interviewee would like to abolish from the typical calculator?
	Does the interviewee use the calculator's memory?
	If so, how many memory slots are useful to the interviewee?
	Any gripes with typical calculator memory implementations?
	--readability--
	number of decimals? *
	Does the interviewee use different bases - bin,octal,hex...
	If graphs, what's more important, graph size or button size?
	-fractions-
		(do we actually have a way to implement this for transcendental funs?)
	-results in terms of transcendental numbers ** probably not... but that's already biased
	--architecture-related--
	Does the user generally count with an internet connection during a typical usage scenario?
	Would a typical scenario happen while user has access, to a laptop or desktop?
	If so, would a typical scenario entail the user already working on said device?
	If not, would the user have access to a mobile device? Would said device be part of the usage?
	If so, would the user prefer switching tasks on a single device or actually having a dedicated device?
	Would the user consider installing a calculator app on a mobile device?
	Would the user consider opening a calculator webpage on said device?
	Would the user have any constraints for input or output during typical usage?
	--layout and design--
	-this next bit is important particularly if a small mobile device is chosen-
	Does the user consider the available screen size on the mobile device to be sufficient for typical app usage?
	Are there any particular apps or pages that the user finds challenging to use given screen-size?
	Same as prior but pleasing?
	Does the interviewee consider button real-estate or display real estate more important?
	Is the interviewee comfortable with multi-view apps? (find a suitable example)
	Does the user have a particular calculator (physical or app) that (s)he already finds particularly appealing?
	--implementation concerns--these are addressed in prior questions
	native app/hybrid app/html-css-js app/webpage (but needing connectivity and having a toolbar suck IMHO)
	precission - user controlled or set, how precise *
	use of transcendental numbers in display? does this even make sense?**
	
Here’s some links regarding tech choices:
https://www.upwork.com/blog/2014/02/choosing-best-mobile-app-development-technology/
http://www.comentum.com/phonegap-vs-native-app-development.html
http://www.androidauthority.com/want-develop-android-apps-languages-learn-391008/

not so relevant...
http://svsg.co/how-to-choose-your-tech-stack/
I didn’t check chrome apps, did you find anything Devang?
A note on tech choices (reflected on some of my questions) is that a web version requires network connection and this might be fairly clunky, depending on the context. Personnaly - but I’m not being interviewed :) - I prefer self-contained apps that don’t depend on the network (which in some cases can be spotty or non-existent) . Ha ha - we should include a question asking if they would use the calculator on a plane - self derission is good…
------------------------------------------------------------------------end text submitted by Fede Jan 29------
1. How would a calculator including these 5 transcendental functions help you with your everyday 

work? Can you give examples of where you would typically use these functions? How do you 

currently go about computing these functions?

2. Will such a calculator reduce the time that it takes to arrive to the solutions of a larger problem 

that you would have to solve?

3. Are there existing calculators that are capable of fulfilling your needs? If not, how difficult is it to 

fulfill these tasks?

4. What would you expect from such a calculator?

5. What kind of problems would this calculator help you solve?

6. What do you like about existing calculators that you think we should incorporate into our 

calculator?

7. What is lacking with existing calculators that you would want to see in our calculator? How 

would these features help with your work?

8. What would be the easiest/most accessible platform/device for you for using such a calculator? 

Do you think that your colleagues and others in your field would also benefit from a calculator 

on similar platform?

-----------------------Following section added by Yuanwen, Jan 29th---------------------------------------

***Potential Interviewee***

Name: Yongai Xu

Occupation: financial analyst

Available time: after work, around 6pm.

Working location: down town, near Mcgill campus

Working in the field of finance, she's dealing with numbers and calculations and uses calculator frequently every day. So she has a lot of experience with using calculator and has some own opinion on it.

***Potential Interview questions***

1)	Does your career need you to use calculator frequently? If yes, how often do you use it?

2)	What functions on a calculator do you use most?

3)	What kind of calculation do you use your calculator for? Simple operation or complex equation?

4)	Would you prefer to use a calculator that display an answer every time you enter a calculation step or a calculator that takes 	the whole equation and delivers the final answer directly?

5)	How many digits to the right of the decimal point do you need normally in your work?

6)	What's the range of order of magnitude that you regularly deal with in your work?

7)	What features on a calculator that you are not happy with during your daily usage?

8)	Do you tend to use a physical calculator or a calculator app on a cell phone or computer?

9)	When you're working on your computer, if you need to use calculator, which option will you chose? a) calculator software 		installed on your computer b) online calculator c) calculator app on your cell phone

10)	Do you need a remember function on a calculator in your work? If yes, normally how many numbers do you need a calculator to 		remember when you do your calculation?

11)	When you chose a calculator, does the style of the interface (color, font, shape of the icon etc.) influence you?

12)	Is a calculator with self-customizable user-interface appealing to you? / To what extend would you like to customize your 		calculator

13)	When you're off work do you still need to use the complex functions that you use in your work?

14)	Would you prefer a calculator that is capable of switching between simple and complex mode? If yes, do you prefer to conduct 		the switch through clicking an existing icon on the calculator or through other gestures (eg. place the cell phone 			horizontally)

Potential Interviewee :
-	Name: Youngeun Kim.
-	Profession: Chemical engineer that works for a manufacturer of application software for real-time data infrastructure solutions. 
-	She works downtown 2 minutes away from McGill Metro
-	Available: Mostly in the evening after 7 PM. 


Questions:

1)	Around how many times have you used a calculator in this past week? 

2)	Was the usage mainly as an app or on a physical calculator? 

3)	What is the most important function in a calculator in relation to your usage/work?

4)	When you go to purchase a calculator, what do you base your purchase on? Looks or functionality? If the answer is looks then 	what do they look for in it? and if it is functionality then what type of functions are mostly important for them

5)	Do you prefer having bigger numbers and smaller operators or the other way around? Or would making everything equally big be 	your preferred calculator style?

6)	How many significant digits would you say are required from your calculator?

7)	Do you think the equal sign should be bigger that the rest of the buttons?

8)	Do you think any of the buttons should be colored? If yes which ones? and how would that help you in general ? 

9)	What platform would you like your calculator to run on? 

10)	Would saving the values for multiple usages be a benefit in the line of work that you do? 

11)	If you had to remove 1 button that is present on most calculators today what would it be? 
