# How to run the tests
- Clone the repo, then run `mvn clean install -U`
- Once all dependencies have been installed, run any simulation using this command: `mvn gatling:test -Dgatling.simulationClass=<path to the simulation here>`
  
Example: `mvn gatling:test -Dgatling.simulationClass=com.tset.nft.simulations.petstore.SmokeSimulation`
