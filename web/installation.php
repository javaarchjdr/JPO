<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>JPO Homepage</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body>
    <table CELLSPACING=0 CELLPADDING=10>
        <tr>
            <th colspan="2" height="60" bgcolor="#97a4da"><h1>JPO  Java Picture Organizer</h1></th>
        </tr>
        <tr>
            <td width="150" bgcolor="#97a4da" valign="top">
                        <?php include("nav.html"); ?>
            </td>
            <td>

                <h2 id="Requirements">Requirements</h2>

                <p>JPO is a pure Java application so it runs on any computer with a display that
			runs Java. (Note: I'm not sure what happens on a cell phone or pda but it hardly makes 
			sense in such an environment.)</p>

                <p>You need <strong>a recent Java runtime installation</strong>. You must have Java 1.4.0 or later because
			Sun introduced cool fast graphics routines in this release which are being used.
			Use the command <code>java -version</code> to find out what version you are running
			if you aren't sure.</p>


                <p>You can download the Java Runtime Environment (JRE) or the Java Software Development Kit (SDK)
			here: <a href="http://www.java.com/en/download/index.jsp">http://www.java.com/en/download/index.jsp</a>
                        Get the JRE is you just want to run java programs. Get the SDK if you want to develop code.</p>

                <p>Dealing with bitmaps inside the JVM consumes huge amounts of memory. On low specified
			machines this can become frustrating when the operating system decides to spend minutes 
			swapping memory around. You should be OK if you have more than 512MB and are 
			dealing with 6 Megapixel images. You can tweak things by changing the amount of 
			pictures JPO is allowed to cache in the settings. You can also tweak the amount of
			memory allocated to the JVM but you will need to make a local installation described
			below.</p>



                <hr>
                <h2 id="Options">Installation Options</h2>

                <p>You can run JPO with Java Web Start technology (which I recommend) or you
			can install the programs locally on your machine. If you want a local installation
			you can either use the packaged Microsoft Windows version (out of date at the time
			of writing) or download the jar files from this web site. You can also download the
			source code and compile your own version.</p>

                <p>The Java Web Start option is probably the simplest choice because you can run JPO directly
			from your web browser by clicking on the "Start now!" link at the top left of this page.
			Your browser then fire up the Java Web Start program which downloads all the pieces 
			from the Web and launches JPO. Each time you launch the application Java Web Start
			checks if I have released a new version and keeps your installation up to date 
			automatically.</p>

                <p>The Microsoft Windows package is a good choice for people who want to run JPO
			on this operating system. Anyone familiar with installing software on this OS
			will feel at home with the Installer interface. Unfortunately I have not released an
			updated package for quite a while. Due to the publicity which such a release attracts
			I feel a need to fix some more bugs and perform a very thorough test before
			the next release.</p>

                <p>Downloading the jars gives you full control about memory allocations and
			any tweaks you would like to make on your JVM.</p>

                <p>Downloading the source and compiling your own version gives you the ability to
			change the source code and do whatever you like (under the lenient restrictions 
			of the GPL, of course).</p>




                <hr>
                <h2 id="javawebstart">Using Java Web Start</h2>

                <p>If everything is configured correctly all you do is click this <strong>
                        <a href="jpo-stable.jnlp">link</a></strong> and off you go.</p>
                <p>You can also start other versions of the program:<br>
                    <a href="jpo-stable.jnlp">JPO Stable</a><br>
                    <a href="jpo-devel.jnlp">JPO Development</a><br>


                <p><strong>Note:</strong> You will see the following warning screen:</p>

                <p><img src="jpo_scr_4.jpg" width="493" height="240" border="0"></p>

                <p>You get this warning message because I haven't bought myself
			a certificate with which to sign the packaged programs. JPO does need 
			access to your hard disk so you have to accept this "sandbox" warning or it 
			will not run.</p>


                <p><strong>Note:</strong> Please also note that on some XP machines and Mac OS machines
			The Java Web Start version is not working. A general exception is thrown with
			no helpful information. I am currently at a loss what is wrong as it used to work
			and I haven't changed this part of the code. It continues to work on all machines
			I have ready access to. If you have an idea what is wrong and how to fix it, please
			let me know.</p>


                <p><strong>Potential problem:</strong> Your browser needs to know that it should
			start Java Web Start when it downloads a <code>.jnlp</code> extension file (Mimetype 
                    <code>application/x-java-jnlp-file</code>). I have seen browsers that didn't know this.
			You can teach the browser the association or run it from hand.</p>
                <p>You can run Java Webstart Programs from outside the browser by starting the program and
			passing in the location of the jnlp file: <br><font color="darkRed">
                        <code>javaws http://j-po.sourceforge.net/jpo-stable.jnlp</code></font></p>

                <p>To do this you do need to know where the javaws program was installed. On
			Windows do a search for javaws.exe. On Linux you can do <br>
                    <code><font color="darkRed">
			find / -name javaws -print</font></code></p>


                <hr>
                <h2 id="windows">Windows Installer</h2>
                <p><b><font color="red"></font></b> For Windows users there is an outdated packaged Windows
			version available. Download it from the regular SourceForge download area:</p>
                <p><a href="http://sourceforge.net/project/showfiles.php?group_id=71359&package_id=70920">Windows Executable</a></p>



                <hr>
                <h2 id="local">Local Installation</h2>
                <p>First download the jar files and save them in your program directory:<br>
                    <a href="http://j-po.sourceforge.net/jpo-0.9.jar">jpo-0.9.jar</a><br>
                    <a href="http://j-po.sourceforge.net/metadata-extractor-2.3.0.jar">metadata-extractor-2.3.0.jar</a><br>
                    <a href="http://j-po.sourceforge.net/jnlp.jar">jnlp.jar</a><br>
                    <a href="http://j-po.sourceforge.net/activation.jar">activation.jar</a><br>
                    <a href="http://j-po.sourceforge.net/miglayout-3.7.1.jar">miglayout-3.7.1.jar</a><br>
                    <a href="http://j-po.sourceforge.net/mail.jar">mail.jar</a>.</p>

                <p><strong>Note:</strong> Make sure they retain the <code>.jar</code> file extension. I've seen
			one XP box save the files as a <code>.zip</code>. With such a wrong extension it will not work!


                <p> Then you need to create a script or batch file to run everything. On Linux the script would look like this:</p>
                <p><font color="darkRed"><code>/PATH/TO/YOUR/JAVA/bin/java -Xms80M -Xmx800M
			-classpath /PATH/TO/YOUR/JPO/JARS/jnlp.jar:
			/PATH/TO/YOUR/JPO/JARS/metadata-extractor-2.3.0.jar:
			/PATH/TO/YOUR/JPO/JARS/mail.jar:
			/PATH/TO/YOUR/JPO/JARS/activation.jar:
                        /PATH/TO/YOUR/JPO/JARS/miglayout-3.7.1.jar:
			/PATH/TO/YOUR/JPO/JARS/jpo-0.9.jar Main</code></font></p>

                <p><strong>Note:</strong> Put everything on one long line. The space characters do
			matter; don't put spaces between the jars separated by colons(:) in the classpath! In the example 
			this was done to make the web page work.</p>

                <p><strong>Note:</strong> On <font color="red">Windows</font> machines the classpath must be
			separated by semicolons (;) on Linux machines by colons (:)</p>


                <p>On a particular Windows XP machine I installed Jpo into c:\Program Files\Jpo. The resulting Batch
			file looks like this: (you can download it here: <a href="Jpo.bat">Jpo.bat</a></p>

                <p><code>
			c:\windows\system32\java -Xms80M -Xmx800M -classpath
			"c:\Program Files\Jpo\jnlp.jar";
			"c:\Program Files\Jpo\metadata-extractor-2.3.0.jar";
			"c:\Program Files\Jpo\mail.jar";
			"c:\Program Files\Jpo\activation.jar";
			"c:\Program Files\Jpo\miglayout-3.7.1.jar";
			"c:\Program Files\Jpo\jpo-0.9.jar" Main
                    </code></p>


                <p>Actually I think I have managed to improve on this on 5.2.2007: I learned that I can
			set a classpath in the manifest of the main jar. If all the jars are in the same directory
			all that is needed to run the program is then a &quot;simple&quot; command like this:</p>

                <p><font color="darkRed"><code>/PATH/TO/YOUR/JAVA/bin/java -Xms80M -Xmx800M
			-jar /PATH/TO/YOUR/JPO/JARS/jpo-0.9.jar </code></font></p>


                <hr>
                <h2 id="source">Installing the source:</h2>

                <p>Naturally there are some prerequisites:
			a Java Software Development Kit (SDK), Apache Ant, a CVS client and JUnit.</p>
                <p>Pitfalls to be aware of: Ant can be very fussy and annoying with cryptic,
			incomprehensible error messages. On my SuSE 11.2 machine things have gotten
			much better. SuSE have done all sorts of magic with the Java installation and 
			the 6 release seems to be found by the command line without too much hassle.
			Ant also does some magic so that as long as the ant command itself is found 
			ant is perfectly happy to build. But, to be honest, I have also spent many
			frustrating hours fiddling with things I didn't understand. I can say that 
			there are two very important environment things: the location of the JVM and
			the location of ANT. The location of the JVM can be specified on a Linux (bash) 
			system by:</p>
                <code>export JVM=/usr/java/jdk1.5.0_07</code>
                <p>Test it with this command:</p>
                <code>$JVM/bin/java -version</code>
                <p>If you get something like the following it's ok, otherwise sort out your Java SDK installation first!</p>
                <code>Java(TM) 2 Runtime Environment, Standard Edition (build 1.5.0_07-b03)<br>
			Java HotSpot(TM) Client VM (build 1.5.0_07-b03, mixed mode, sharing)</code>

                <p>Not sure, but JAVA_HOME can be used for the same thing and on my system is set to the
			same directory. I have both variables set to the same thing and it works. Good enough for me.</p>

                <p>Check you have a working java compiler with:</p>
                <code>javac -version<br><br>
			javac 1.5.0_10<br>
			javac: no source files<br>
			Usage: ....</code>

                <p>Now check that your ant works properly (and this is where the
			ANT_HOME environment variable can be important):</p>
                <code>ant -version<br><br>
			Apache Ant version 1.6.5 compiled on May 3 2006</code>

                <p>Use the following command to checkout the latest cvs source from sourceforge:</p>
                <code>cvs -z3 -d:pserver:anonymous@j-po.cvs.sourceforge.net:/cvsroot/j-po checkout -P Jpo</code>

                <p>You would then cd to the Jpo directory:</p>
                <code>cd /wherever/you/put/the/code/.../Jpo</code>

                <p>In order to run the unit tests (without which you can't build) you need
			a working JUnit installation. Check this out with the following command:</p>
                <code>ant JUNIT</code>

                <p>A lot of stuff will scroll over the screen but if the last lines say something
			like the following you are good:</p>
                <code>JUNIT:<br><br>BUILD SUCCESSFUL<br>Total time: 0 seconds</code>


                <p>If the above doesn't work fix it! The command <code>ant -diagnostics</code> might
			be helpful. I wish someone had told be about this years ago. Also <code>ant -debug target</code>.
			Well actually, read the <a href = "http://www.oreilly.com/catalog/anttdg/">O'Reilly Ant book</a>.<br>
			Next you compile the code with the command</p>
                <code>ant compile</code>
                <p>And then run it with</p>
                <code>ant go</code>

                <p>In order to build and deply the jar files you need to generate a key with
			which you can sign the jar files</p>
                <code>keytool -genkey -keystore myKeystore -alias myself</code>
                <p>See the Java Web Start developers guide for details:
                    <a href="http://java.sun.com/products/javawebstart/docs/developersguide.html">http://java.sun.com/products/javawebstart/docs/developersguide.html</a></p>


                <p>For convenience I have set up a shell script that sets my environment. I run it with the
                    <code>source jpoenv.sh</code> command. It does the following:</p>
                <pre>
#!/bin/sh  
JAVA_HOME=/opt/IBMJava2-14
JAVA_DEV_ROOT=/path_to_my_sources/Jpo 
IS_UNIX=true 
JAVA_KEY_STORE=/path_to_my_keystore/javaKeyStore
JAVA_KEY_STORE_KEY=my_secret_key

export JAVA_HOME 
export JAVA_DEV_ROOT 
export IS_UNIX
export JAVA_KEY_STORE
export JAVA_KEY_STORE_KEY

cd /path_to_my_sources/Jpo
                </pre>



                <p>The following targets are supported by the ant buildfile:</p>
                <code>ant compile     - compiles everything requiring a compilation. The classes are put in<br>
				  	  build/classes   by their package (which is jpo)<br>
			ant run		- runs the application from build/classes<br>
			ant clean	- deletes the compiled classes<br>
			ant rebuild	- run clean and compile<br>
			ant javadoc	- creates the javadoc in the directory<br>
					  build/docs/index.html<br>
			ant go          - compiles and runs the application<br>
			ant buildjar    - builds the jar files<br>
			ant signjar	- signs the jar files (if yout have the keys...)</code>



                <p>Sourcefore also have some notes about downloading with cvs: <a href="http://sourceforge.net/cvs/?group_id=71359">http://sourceforge.net/cvs/?group_id=71359</a></p>

                <p>Sourceforge has a neat tool to browse the source code which also highlights
			differences between version very nicely: <a href="http://j-po.cvs.sourceforge.net/j-po/">http://j-po.cvs.sourceforge.net/j-po/</a></p>


                <hr>
                <h2 id="problems">Potential problems:</h2>

                <h2>Memory settings:</h2>
                <p>If you get <font color="red">out of Memory</font> errors then you are probably best off
	 	        using the local jar way of starting this application as you can then set the initial memory heap with 
			the -Xms and the maximum memory heap with the -Xmx parameters. Of course this can also be done on 
			the web start version but you would have to set Jpo up on your own webserver so that you 
			could specify the memory settings in the .jnlp configuration file.</p>

                <h2>Chinese Font</h2>
                <p>Franklin He has been kind enough to translate the User Interface to Traditional and Simplified
			Chinese. This looks really cool but poses some installation hassles if your Java system 
			is not correctly configured.
			On my SuSE 10 system I had to create a /usr/java/jdk1.5.0_07/jre/lib/fontconfig.SuSE.properties 
			file. Click
                    <a href="fontconfig">here</a> to download my version.</p>


                <h2>Helpful Links</h2>
                <p>Sun's Web start FAQ: <a href="http://java.sun.com/products/javawebstart/faq.html">http://java.sun.com/products/javawebstart/faq.html</a><br>
		        Sun's Java Web start download page: <a href="http://java.sun.com/products/javawebstart/download.html">http://java.sun.com/products/javawebstart/download.html</a><br>
		        Unofficial Java Web Start/JNLP FAQ: <a href="http://www.vamphq.com/jwsfaq.html">http://www.vamphq.com/jwsfaq.html</a></p>

                <p>Please let me know of specific installation issues you have so that I may extend this section for the benefit of other users who might have the same issue.</p>



                <hr>
                <p>Last update to this page: 6.6.2010<br>
			Copyright 2003-2010 by Richard Eigenmann, Z&uuml;rich, Switzerland</p>
            </td>
        </tr>
    </table>
</body>
</html>