/root/bin/jdk-11.0.10+9/bin/java\
  -classpath '/root/codeBase/div/devon/frinika-fork2/dist/frinika.jar:/root/tmp/projector-server-1.0-SNAPSHOT/lib/*'\
  -Dorg.jetbrains.projector.server.classToLaunch=com.frinika.FrinikaMain \
  -Djava.awt.headless=false \
   -Dtestfx.robot=glass \
  -Dglass.platform=Monocle \
  -Dmonocle.platform=Headless \
  -Dprism.order=sw \
   -Dorg.jetbrains.projector.server.port=8887 \
   org.jetbrains.projector.server.ProjectorLauncher
