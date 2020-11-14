# super-battle-cards
A recreation of the game Super Battle Cards that plays in the browser.

### Notes
This project was created using sbt as the build tool, Jquery & KnockoutJS for the frontend and Play Framework for the backend.

To run this: Open the Command Prompt at the root of this project and type "sbt run". Then you can play it on your brower with the url: "localhost:9000".

### Requirements
###### In order to run this app there are a couple of things that must be added.
 - You must supply your own assets (see public/images/README.md)
 - You must connect it to your own database at app/models/.database. Create classes that extend Database.java and DatabaseInterface.java.

### Version 1.0
###### Limitations
 - Only 1 game mode: Endless (missing Quick and Careful modes).
###### Additions
 - Game progress is saved.
 - Watch replay functionality.
###### Changes
 - Many changes for the spawn rates, item values, and monster health.
 - Added a small chance to spawn an Enraged Boss with double health.
 - Hero skills (i.e. Fireball, Poison, Arrows) are activated before time/counter updates.
