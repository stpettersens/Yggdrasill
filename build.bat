packager.py -p io.stpettersen.yggdrasill.client -m YggdrasillClient -cp libs -r Client
javac -cp . packager/io/stpettersen/yggdrasill/client/*.java
jar -c YggdrasillClient.jar packager/io/stpettersen/yggdrasill/client/Manifest.mf -C packager/io/stpettersen/yggdrasill/client
