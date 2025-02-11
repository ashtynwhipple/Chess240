# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

[Link to project diagram for phase 2](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUYAblMOkhwAA1phGcSWbkCmCVOV+VBGTiRCoVfkjpcYGBUt5kjA0ChzQoEDKOJZ6nL0ABRKDeOHYw1QfXZczlFj1epVGAAFicAGYpq73eUbXawo7Zeg+mZOKZFcz1CdQbj1RafAgEDqqHjThmSapSiAZZlNbSOoyGdolVnTsZSgoOBw+Q3tMXSwTm5nK9WUHIUAofCbYcAp6lGwqhxW2e3O93JybtWrDPqzkCjQjoci1AWsICLiCc9QjdcOndJuVVk9Zyb6hAk2gH1N9sXKNnCr6GDlAATE4TjdLeQximMMCPo8Uwvqkb4fl+qwHOgHCmFhXi+P4ATQOw5IwAAMhA0RJAEaQZFkyDmGyXrlNUdRNK0BjqAkaAQQMd7QeM35fC8bwfPxBxemy57Alc3FQaMfFPnM+ivEsck-hJf5XiWeYIGRPKwqR5GouisSermO6DkSw5khSdaiqMTYWcubYcjA3K8pqgrCjAtliJg0pjvK5bKqqpnlGgBZFtu-5FNeZQwD0UyjOowDknJ0bQOUPjBHO0BIAAXigywwKJxTqacgFgOUYYAIwQQlLLJfcUZuulXlZSaOX5YVewwKmmGLg5LJRduGq9vIpjbsFuooOUJRIAAZpYAD6glLLCspoBAyRoMtilCbM2DeCWHAFTtCxLNivkyvKkW7gxcV1UlKWPmlsWZYhHUFZMxUxVF5WVU4NW9A9qgNalzWvW1qQfV1PUYeNplsoFpIwKO44bqkM5zguSOsk5sZdjA6Nbgju5qbF+k8pEqinpgZNDSVsXxdJty8RMXyIchyZs0Vv6XgBtFATAoHgUDzP3mzz5zpzn7c+haZYaYOF+IEXgoOgJFkb4zCUekmSYOV9EM4x0jOsRzr1M6zQtGxqgcd0HPvugvPieckn3X0DsoXBX20676mFMNMDafYWuY6+jtoCZU2I0uLJWWA6Nh0hEf2UyjmFO2Lk8uuWPaEKYSe07OP01Nsa52Nl3+f1adBQaIX5oW-YqCXJRXMDoPPeDGWQ9DPtibuf2hgDtV9IlINPU1Mate9UB5Z9PNw03ZkyLHyPHdwmSJ4XkfVy2uMZ85kTDBANCE+XwBSldS8tze7cT30L3lCAvgIN2G3MCA3B4GfJpaPIfcM1+gLCqMAACsYER5jw7pPFqz8CxvwgB-L+sB0Z-2AD7ReN1Thk3KPpLWVMaZ01ukbOK31jgD2ASBCBvQDi9QVorbwysAiQm7MRaEMAADi0FWQ62ovrYBhsYqMQ4ebK29hoL2ylhHZ22C-aMwQlIr2-EeZEIDvXZAsQuGJSTtLKOmll443jlvRR6BU57xXM5VyOdNx508tvS+Vccb6kDmFRuWC1FCPdlA++j9p7ZVnp1ABP0KE5BAdVSB9UfFdz8e1AJ885Z9WLsQ0uP95x9mvmWVelYYDkjAFotQsIzHDgseUSIFhUCnzYbEGAEA5qcO4bvYpyT9HlHyaoKpWB3F7gvG3Po4jEp8QqP0fpKAACS0g+JVWAuGEMTwqK1h4rJCW3Qpg6AQKAWUmpxZ9C+CMgAcqzHZexmgAjkTfeRfTuGDOGdBcZkzpmzKmPMlAWzDkzBWX0NZGzXlLJ2U8fZbzjk837mVShQtqFMxGaoa5lzRh3PKFMmZczdYvMWfcP5HyvkgE2WilYuzoIHN+X0IFtDF4KyVnhbAPgoDYG4PAGshh8kpBRfw0JgjW6VFqA0MREjIbSy4gColZD-bdLdkzbeKl8WjEJein2qjootJRgy-JsI4DKugoZDEeiBwrwGsjXJxjw4fiKendk5QrGpI8gXExaAHFykaRWZx9dXERRJhpW+o9ImNQftEt6-i55dRkaC0J-1AZM28d63xfrYkBowfLJJGlwSWvSTdcyNdkao0yCqkZ2MsklJgKMtAHRmB7KtPU0Ydrrpuo8Ry8NXqwZTz0N2SEaIMRBPIcGv0Q8RZ1sepG6JTaYAtqMmAONiS83NKTZC25EyEUPOBdW0VHqRnwpgIikMKiznNOXTO+5MyF3BM7YLYWI8V2zrXfO0l8tyWMLwpYFAhZNowAAFIQB5OWwwAQsXygNu62K1RKQsRaCMyRRr0BcVpcAe9UA4AQG0lAFYAB1FgoyLYtAAELEQUHAAA0msbosKxnnvXcKkEhQcHuwld7J4kHoOwfg+MD5yHUPOgw1h3DUqiN7o3ac-cZGFVJoAFZvrQCq19PJ8mauMhktNe8jFzh0SnB1g08ZZ15ETWx1qwO2srvapxk1FUuoyX+3pEaG0tWjVDOJgaQX8xDUPMNd9+1T0s73Be8aJ2JrzBpiuqbdXpuybk7NM6TUqYPua7OH7845JnZW5TrYvPTQbq66O26Llmc7o24AzaUCttiO20qdmu1hh7U58zsVB3Drbe58dercaJdadBYmqXZOWRgIpJA9g0BVHWdBt8wW4XSH2r1yg9HoCzG8qF1s4W+TYD-oy6CMBkgZFSDAHrUG-wKHYl10YGA4tdLur28ezmWrAFtGOe0xonSfgPR2orgswzhgiX28r5QzvxiuyhGr8NUuyL4+UcTomNUngQGeLdJnSFBvuyAk9NCFZkuwrelWUAoPwGQYTbAtLCDxESMyvhv6a1GgqCbM2FsrbGBkeRuRVxeMXnZUmz+Y4tTaD0AYQpP39Exzq0-ZBPngDs6STNo+J9DB8-24uw7ZXMuwJfggpBTPUloIK3zNH9nwGlc9S96XsU4GvwtIglG6PUHaDHRznVhjDdM-yYyAXE6hcbBFx+jQumq2-cJ+l+t2un6y-1-L7+1uTe3cK6rrt6vnvHdeyjH379Lf+6a4HhJZvm6ZO57HqA7ltC27q-m4Xp9NTO78vag7JCjvQJ9VPXXcu089koEroPKvB5h9FhlmBOvo8G8Z9-TUdfE-xfqzWpNZAfDDmM1T-7aOmeMlZygAhoPfZ8fOTTqHIfj0QtoVhReQA)
