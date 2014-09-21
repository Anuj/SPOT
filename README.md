SPOT
====

SPOT is an interactive QA agent for preschoolers. 

Here is how the project is organized:


Puppybox - Contains a flash builder 4.6 project that contains most of the animated content for the interactive agent. The PuppyBox/test folder contains all the assets that SPOT uses. Most of this content should be reusable. This piece of code runs an AIR websocket server on port 5000, and waits for hypothesis from the speech rec code explained below.

SpeechRecCode - Contains a basic javascript code that talks to Google's speech services, listens to client audio and retrives a speech rec hypothesis. This hypothesis is then sent over a websocket port 5000 to the flash builder code.

SPOT-Brain - Contains the natural language piece of the code. The TCP server connects with the flash builder project on port 8000. This port can also be used to control spot via the command line for wizard-of-oz testing. Currently the way commands progress is:
game - reveals two objects
left - explains object on the left
right - explains object on the right
start - shuffles and hides objects
reveal "<object name>" - reveals the object that was hidden in last step
User can keep going through this loop till they exhaust syllabus items. Syllabus items are in the puppybox code in syllabus.xml

SPOT-Brain also contains java classes to analyze, decompose and resolve incoming questions from the puppybox code. Details on this is included in my thesis (chapter 7).
http://www.eecs.berkeley.edu/Pubs/TechRpts/2013/EECS-2013-67.pdf
