mvn install:install-file -DgroupId=${project.groupId} -DartifactId=${project.artifactId} -Dversion=${project.version} -Dpackaging=jar -Dfile=../target/final/${project.build.finalName}-jar-with-dependencies.jar -DlocalRepositoryPath=../../Maven-Repository

:: mvn install:install-file -DgroupId=org.parabot -DartifactId=client -Dversion=2.4.1.1 -Dpackaging=jar -Dfile=../target/Parabot-V2.4.1.1-jar-with-dependencies.jar -DlocalRepositoryPath=../../Maven-Repository
:: mvn install:install-file -DgroupId=org.parabot -DartifactId=client -Dversion=2.4.3 -Dpackaging=jar -Dfile=../target/Parabot-V2.4.3-jar-with-dependencies.jar -DlocalRepositoryPath=../../Maven-Repository
:: mvn install:install-file -DgroupId=org.parabot -DartifactId=client -Dversion=2.5 -Dpackaging=jar -Dfile=../target/final/Parabot-V2.5-jar-with-dependencies.jar -DlocalRepositoryPath=../../Maven-Repository
