
# JAVALIN BOILERPLATE

This is a Javalin starter POC template, aiming to provide just enough modern front-end tooling to develop interactive web-applications in modern Java. Inspirations are taken from Apache Wicket, JSF and Vaadin, yet keeping in line with minimalistic approach of Javalin. [AngularTS](https://github.com/Angular-Wave/angular.ts) is used for front-end routing and form processing. 

## Software Requirements

To run this project, ensure you have the following software installed:

- **GoLang**: Version 1.20 or later
- **Java**: Version 21 or later
- **Node.js**: Version 18 or later
- **Docker**: Version 27 or later

### Getting started

Set up `goose` and `npm` via:

```bash
 make setup
```

Start `db` container:

```bash
 docker compose up -d
``` 

Run migration:
```bash
 make db-up
``` 

All set!

---

### Development mode

For interactive live-reload, you will need to run `browsersync` in a separate terminal:
```bash
 make dev
```
and then run `Debug Java` > `src/main/java/Main.java` in VSCode or Eclipse for automatic class reloads. If you don't need live-reload:
```bash
 make run
```

### Starter points

`CalculatorController.java` - sample of reactive UI 
`ProductController.java` - sample CRUD

