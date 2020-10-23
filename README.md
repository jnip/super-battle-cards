# super-battle-cards
A recreation of the game Super Battle Cards that plays in the browser.

### Requirements
###### In order to run this app there are a couple of things that must be added.
 - You must supply your own assets (see public/images/README.md)
 - You must connect it to your own database (NoSQL may work out of the box) at models/.helper/WebService.java

### Version 0.9
###### Player & Access Limitations
- No external network connections allowed.
- No player authentication.
- UI does not work properly on mobile. Optimized for Kindle Paperwhite. Works on PC browser.

###### Game Limitations
- No landing page (direct access into game).
- Only 1 mode: Endless (missing Quick and Careful modes).

###### Gameplay Limitations
- Fog is displayed but implementation is not there yet. Acts as an empty space.
- Money is displayed but does not have a value associated with it. Money counter has not been implemented.
- Poison works properly but is hidden (no UI elements to show monster that are poisoned).
