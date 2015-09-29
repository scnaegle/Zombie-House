# Zombie-House
Cs 351 project 1
Creators: Sean Naegle, Tyler Shelton, Ally Goodman-Janow

Zombie house is a top-down, single player game where you, as the player, must make it out of a zombie infested house without being bitten or getting blown up. There are as many as 5 levels, and if you can reach the endpoints without dying then you have successfully made it out of the house and won the game. In order to find those endpoints, you must search, in the dark, through the house and stay clear of any zombies. As a means of defense, there are fire traps randomly located throughout the house for the player's use and protection. Be aware, running over a trap (pressing 'r' while moving) can set it off so remember to walk when around them. Any zombie that sets foot on a trap is immediately blown up and removed from the game, so one less zombie to worry about. The player can pick these traps up and put them down where ever they seem fit by simply pressing the 'p' key while on top of a trap. Also be aware that any object that intersects the explosion also dies, so stand back. 

Attributes: Sight, Speed, Smell, Stamina, Regeneration Rate, Hearing, Decision Rate, Spawn Rate

The player has a sight, a speed, hearing, stamina and regeneration rate which all allow the player to move/see what is currently happening. The player can hear zombies if they are within their hearing rate, and can only see so far out in front of them. Stamina enables them to run for a certain amount of time but quickly diminishes and slowly regenerates. 

Zombies are blind and deaf, they can only smell the player. They have a decision rate which allows them to update their movement every specified seconds. They're spawn rate calculates how likely it is that a zombie will be spawned on a tile. They higher the number, the more zombies. 

Any of these attributes can be changed at the beginning of the level when the 'settings' window appears. 

Movement:
The player is moved by pressing any of the arrow keys and ASWD, or any combination of them. 
The player runs by pressing 'r' or 'shift' while any of the movement keys are held down.
The player can walk or stand still for as long as they want, but running requires stamina which decreases the more you run. 
The player cannot move through walls.

There are two types of zombies: random walk and line walk.
Random walk zombies randomly pick directions and move until they hit a wall, then they line walk.
Line walk zombies move in straight lines.
All zombies have a smell attribute which senses the player, so if they smell you, they bee-line it to your location. 

Features:
- Every object plays it's own sound. Sounds were downloaded from FreeSound.com.
- All sprite and object images were taken from google.
- Pause/Start when player taps the space bar. 
- Labels on screen that allow user to see what attributes currently are.
- Window is re-sizeable.
- User may change settings at the beginning of each game. 
- There are creepy shadows!
- Each level spawns more zombies and gets gradually harder. 


Enjoy the game!

