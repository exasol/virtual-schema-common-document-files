<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                   | License                                                                                                      |
| -------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Common Virtual Schema for document data][0] | [MIT][1]                                                                                                     |
| [Parquet for Java][2]                        | [MIT License][3]                                                                                             |
| [error-reporting-java][4]                    | [MIT][1]                                                                                                     |
| [Jakarta JSON Processing API][6]             | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |
| [Performance Test Recorder Java][9]          | [MIT][1]                                                                                                     |
| [Project Lombok][11]                         | [The MIT License][12]                                                                                        |
| [FastCSV][13]                                | [MIT License][1]                                                                                             |

## Test Dependencies

| Dependency                                   | License                           |
| -------------------------------------------- | --------------------------------- |
| [Hamcrest][15]                               | [BSD License 3][16]               |
| [JUnit Jupiter Engine][17]                   | [Eclipse Public License v2.0][18] |
| [JUnit Jupiter Params][17]                   | [Eclipse Public License v2.0][18] |
| [mockito-core][21]                           | [The MIT License][22]             |
| [mockito-junit-jupiter][21]                  | [The MIT License][22]             |
| [Common Virtual Schema for document data][0] | [MIT][1]                          |
| [Matcher for SQL Result Sets][27]            | [MIT][1]                          |
| [udf-debugging-java][29]                     | [MIT][1]                          |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][6] | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][34]                       | [GNU LGPL 3][35]                               |
| [Apache Maven Compiler Plugin][36]                      | [Apache License, Version 2.0][37]              |
| [Apache Maven Enforcer Plugin][38]                      | [Apache License, Version 2.0][37]              |
| [Maven Flatten Plugin][40]                              | [Apache Software Licenese][41]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][42] | [ASL2][41]                                     |
| [Reproducible Build Maven Plugin][44]                   | [Apache 2.0][41]                               |
| [Maven Surefire Plugin][46]                             | [Apache License, Version 2.0][37]              |
| [Versions Maven Plugin][48]                             | [Apache License, Version 2.0][37]              |
| [Apache Maven Deploy Plugin][50]                        | [Apache License, Version 2.0][37]              |
| [Apache Maven GPG Plugin][52]                           | [Apache License, Version 2.0][37]              |
| [Apache Maven Source Plugin][54]                        | [Apache License, Version 2.0][37]              |
| [Project keeper maven plugin][56]                       | [The MIT License][57]                          |
| [Apache Maven JAR Plugin][58]                           | [Apache License, Version 2.0][37]              |
| [Apache Maven Javadoc Plugin][60]                       | [Apache License, Version 2.0][37]              |
| [Nexus Staging Maven Plugin][62]                        | [Eclipse Public License][63]                   |
| [Lombok Maven Plugin][64]                               | [The MIT License][1]                           |
| [JaCoCo :: Maven Plugin][66]                            | [Eclipse Public License 2.0][67]               |
| [error-code-crawler-maven-plugin][68]                   | [MIT][1]                                       |
| [Maven Clean Plugin][70]                                | [The Apache Software License, Version 2.0][41] |
| [Maven Resources Plugin][72]                            | [The Apache Software License, Version 2.0][41] |
| [Maven Install Plugin][74]                              | [The Apache Software License, Version 2.0][41] |
| [Maven Site Plugin 3][76]                               | [The Apache Software License, Version 2.0][41] |

[4]: https://github.com/exasol/error-reporting-java
[41]: http://www.apache.org/licenses/LICENSE-2.0.txt
[11]: https://projectlombok.org
[46]: https://maven.apache.org/surefire/maven-surefire-plugin/
[62]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[70]: http://maven.apache.org/plugins/maven-clean-plugin/
[1]: https://opensource.org/licenses/MIT
[21]: https://github.com/mockito/mockito
[9]: https://github.com/exasol/performance-test-recorder-java
[3]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[40]: https://www.mojohaus.org/flatten-maven-plugin/
[48]: http://www.mojohaus.org/versions-maven-plugin/
[56]: https://github.com/exasol/project-keeper/
[16]: http://opensource.org/licenses/BSD-3-Clause
[36]: https://maven.apache.org/plugins/maven-compiler-plugin/
[13]: https://github.com/osiegmar/FastCSV
[67]: https://www.eclipse.org/legal/epl-2.0/
[50]: https://maven.apache.org/plugins/maven-deploy-plugin/
[63]: http://www.eclipse.org/legal/epl-v10.html
[35]: http://www.gnu.org/licenses/lgpl.txt
[66]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[22]: https://github.com/mockito/mockito/blob/main/LICENSE
[12]: https://projectlombok.org/LICENSE
[27]: https://github.com/exasol/hamcrest-resultset-matcher
[44]: http://zlika.github.io/reproducible-build-maven-plugin
[57]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[7]: https://projects.eclipse.org/license/epl-2.0
[34]: http://sonarsource.github.io/sonar-scanner-maven/
[37]: https://www.apache.org/licenses/LICENSE-2.0.txt
[38]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[0]: https://github.com/exasol/virtual-schema-common-document/
[29]: https://github.com/exasol/udf-debugging-java/
[18]: https://www.eclipse.org/legal/epl-v20.html
[74]: http://maven.apache.org/plugins/maven-install-plugin/
[17]: https://junit.org/junit5/
[42]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[52]: https://maven.apache.org/plugins/maven-gpg-plugin/
[2]: https://github.com/exasol/parquet-io-java/
[6]: https://github.com/eclipse-ee4j/jsonp
[64]: https://anthonywhitford.com/lombok.maven/lombok-maven-plugin/
[54]: https://maven.apache.org/plugins/maven-source-plugin/
[8]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[15]: http://hamcrest.org/JavaHamcrest/
[76]: http://maven.apache.org/plugins/maven-site-plugin/
[72]: http://maven.apache.org/plugins/maven-resources-plugin/
[60]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[68]: https://github.com/exasol/error-code-crawler-maven-plugin
[58]: https://maven.apache.org/plugins/maven-jar-plugin/
