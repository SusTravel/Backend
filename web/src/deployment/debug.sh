#!/usr/bin/env bash
java  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005  -jar web-0.0.1-SNAPSHOT.jar &