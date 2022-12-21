<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                       | License                                                                                                      |
| -------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| virtual-schema-common-document   |                                                                                                              |
| [Parquet for Java][0]            | [MIT License][1]                                                                                             |
| [error-reporting-java][2]        | [MIT License][3]                                                                                             |
| [Jakarta JSON Processing API][4] | [Eclipse Public License 2.0][5]; [GNU General Public License, version 2 with the GNU Classpath Exception][6] |
| [FastCSV][7]                     | [MIT License][8]                                                                                             |

## Test Dependencies

| Dependency                                | License                           |
| ----------------------------------------- | --------------------------------- |
| [Hamcrest][9]                             | [BSD License 3][10]               |
| [JUnit Jupiter Engine][11]                | [Eclipse Public License v2.0][12] |
| [JUnit Jupiter Params][11]                | [Eclipse Public License v2.0][12] |
| [EqualsVerifier | release normal jar][13] | [Apache License, Version 2.0][14] |
| [mockito-core][15]                        | [The MIT License][16]             |
| [mockito-junit-jupiter][15]               | [The MIT License][16]             |
| virtual-schema-common-document            |                                   |
| [Matcher for SQL Result Sets][17]         | [MIT License][18]                 |
| [udf-debugging-java][19]                  | [MIT][8]                          |
| [Performance Test Recorder Java][20]      | [MIT License][21]                 |
| [Apache Commons Text][22]                 | [Apache License, Version 2.0][14] |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][4] | [Eclipse Public License 2.0][5]; [GNU General Public License, version 2 with the GNU Classpath Exception][6] |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][23]                       | [GNU LGPL 3][24]                               |
| [Apache Maven Compiler Plugin][25]                      | [Apache License, Version 2.0][14]              |
| [Apache Maven Enforcer Plugin][26]                      | [Apache License, Version 2.0][14]              |
| [Maven Flatten Plugin][27]                              | [Apache Software Licenese][14]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][28] | [ASL2][29]                                     |
| [Maven Surefire Plugin][30]                             | [Apache License, Version 2.0][14]              |
| [Versions Maven Plugin][31]                             | [Apache License, Version 2.0][14]              |
| [Apache Maven Deploy Plugin][32]                        | [Apache License, Version 2.0][14]              |
| [Apache Maven GPG Plugin][33]                           | [Apache License, Version 2.0][14]              |
| [Apache Maven Source Plugin][34]                        | [Apache License, Version 2.0][14]              |
| [Apache Maven Javadoc Plugin][35]                       | [Apache License, Version 2.0][14]              |
| [Nexus Staging Maven Plugin][36]                        | [Eclipse Public License][37]                   |
| [JaCoCo :: Maven Plugin][38]                            | [Eclipse Public License 2.0][39]               |
| [error-code-crawler-maven-plugin][40]                   | [MIT License][41]                              |
| [Reproducible Build Maven Plugin][42]                   | [Apache 2.0][29]                               |
| [Project keeper maven plugin][43]                       | [The MIT License][44]                          |
| [Apache Maven JAR Plugin][45]                           | [Apache License, Version 2.0][14]              |
| [Maven Clean Plugin][46]                                | [The Apache Software License, Version 2.0][29] |
| [Maven Resources Plugin][47]                            | [The Apache Software License, Version 2.0][29] |
| [Maven Install Plugin][48]                              | [The Apache Software License, Version 2.0][29] |
| [Maven Site Plugin 3][49]                               | [The Apache Software License, Version 2.0][29] |

[0]: https://github.com/exasol/parquet-io-java/
[1]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[2]: https://github.com/exasol/error-reporting-java/
[3]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[4]: https://github.com/eclipse-ee4j/jsonp
[5]: https://projects.eclipse.org/license/epl-2.0
[6]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[7]: https://github.com/osiegmar/FastCSV
[8]: https://opensource.org/licenses/MIT
[9]: http://hamcrest.org/JavaHamcrest/
[10]: http://opensource.org/licenses/BSD-3-Clause
[11]: https://junit.org/junit5/
[12]: https://www.eclipse.org/legal/epl-v20.html
[13]: https://www.jqno.nl/equalsverifier
[14]: https://www.apache.org/licenses/LICENSE-2.0.txt
[15]: https://github.com/mockito/mockito
[16]: https://github.com/mockito/mockito/blob/main/LICENSE
[17]: https://github.com/exasol/hamcrest-resultset-matcher/
[18]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[19]: https://github.com/exasol/udf-debugging-java/
[20]: https://github.com/exasol/performance-test-recorder-java/
[21]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[22]: https://commons.apache.org/proper/commons-text
[23]: http://sonarsource.github.io/sonar-scanner-maven/
[24]: http://www.gnu.org/licenses/lgpl.txt
[25]: https://maven.apache.org/plugins/maven-compiler-plugin/
[26]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[27]: https://www.mojohaus.org/flatten-maven-plugin/
[28]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[29]: http://www.apache.org/licenses/LICENSE-2.0.txt
[30]: https://maven.apache.org/surefire/maven-surefire-plugin/
[31]: https://www.mojohaus.org/versions-maven-plugin/
[32]: https://maven.apache.org/plugins/maven-deploy-plugin/
[33]: https://maven.apache.org/plugins/maven-gpg-plugin/
[34]: https://maven.apache.org/plugins/maven-source-plugin/
[35]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[36]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[37]: http://www.eclipse.org/legal/epl-v10.html
[38]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[39]: https://www.eclipse.org/legal/epl-2.0/
[40]: https://github.com/exasol/error-code-crawler-maven-plugin/
[41]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[42]: http://zlika.github.io/reproducible-build-maven-plugin
[43]: https://github.com/exasol/project-keeper/
[44]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[45]: https://maven.apache.org/plugins/maven-jar-plugin/
[46]: http://maven.apache.org/plugins/maven-clean-plugin/
[47]: http://maven.apache.org/plugins/maven-resources-plugin/
[48]: http://maven.apache.org/plugins/maven-install-plugin/
[49]: http://maven.apache.org/plugins/maven-site-plugin/
