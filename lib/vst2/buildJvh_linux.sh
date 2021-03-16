lstdc# check envt
echo checking environment
if [ -z "$JAVA_HOME" ]
then
  echo "Environment variable JAVA_HOME is not set. It should be set to the root directory of your Java SDK install."
  exit
fi
com/synthbot/audioplugin/vst/vst2/vst2/public.sdk/source/vst2.x
# compile
echo compiling the library
gcc -o libjvsthost.so -fPIC -shared -Wl,-soname,libjvsthost.so  -lc -lstdc++ \
-Ivst2 \
-I$JAVA_HOME/include \
-I$JAVA_HOME/include/linux \
-I/usr/include \
vst2/audioeffect.cpp \
JVstHost.cpp

# copy somewhere useful
echo copying library to /usr/local/lib/
cp libjvsthost.so /usr/local/lib/
