#!/bin/bash
mvn install -DskipTests -Dmaven.javadoc.skip=true -Dproject-keeper.skip=true -Derror-code-crawler.skip=true
