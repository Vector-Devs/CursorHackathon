# Multi-Target Application (MTA)

This repository includes [`mta.yaml`](mta.yaml), an SAP MTA 3.3 descriptor that lists every runnable module: **mockServices**, **enterpriseservice**, six **agents**, and the **reasoning-ui** static front end.

## Prerequisites

- [Cloud MTA Build Tool (MBT)](https://github.com/SAP/cloud-mta-build-tool) (`mbt` CLI)
- Java 21 + Maven (for Java modules)
- Node.js + npm (for `reasoning-ui`)

## Build the MTAR

From the repository root:

```bash
mbt build
```

The archive is written under `mta_archives/`, typically:

`mta_archives/cursorhackathon_1.0.0.mtar`

## Validate only

```bash
mbt validate
```

## Deploy

Use your platform’s MTA deploy (e.g. SAP BTP **Cloud Foundry** with `cf deploy`, or **SAP Cloud MTA** deployment in the BTP cockpit). Wire **service bindings / destinations** so agents see the correct **mock**, **enterprise**, and **reasoning** base URLs (see each module’s `application.properties`).

Local defaults are documented in [`agents/readme.md`](agents/readme.md) (port map: 8082 mock, 8085 enterprise, 8090–8094 agents, 5173 UI dev).

## Notes

- **Java** modules expect a Spring Boot **executable JAR** from `mvn package` (`target/*.jar`).
- **reasoning-ui** is built with **npm** (`dist/`). For pure Cloud Foundry static hosting you may replace the `html5` module with a `staticfile` or `nodejs` buildpack flow and an **approuter** for API routing.
- The MTA does **not** encode inter-app URLs; set env vars (or destinations) after deploy to match your landscape.
