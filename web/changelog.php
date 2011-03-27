<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>JPO Homepage</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta name="generator" content="Bluefish 1.0.7">
</head>
<body>
<table CELLSPACING=0 CELLPADDING=10>
	<tr>
		<th colspan="2" height="60" bgcolor="#97a4da"><h1>JPO  Java Picture Organizer</h1></th>
	</tr>
	<tr>
		<td width="150" bgcolor="#97a4da" valign="top">
                        <?php include("nav.html"); ?>		</td>
		<td>
			<h2 id="Changelog">Changelog</h2>

			<table>
				<tr><td colspan=2><b>Development Version 0.9</b></td></tr>
				<tr><td valign="top">27.3.2011</td><td> Added parsing of GPS data from image</td></tr>
				<tr><td valign="top">27.3.2011</td><td> When removing a group in the tree the thumbnail panel didn't jump to the last visible group</td></tr>
				<tr><td valign="top">27.3.2011</td><td> Fixed File Copy for multiple files</td></tr>
				<tr><td valign="top">22.2.2011</td><td> Added Thumbnail to Tooltips</td></tr>
				<tr><td valign="top">21.2.2011</td><td> Generated Webpage now also shows a little Google map</td></tr>
				<tr><td valign="top">12.1.2011</td><td> Settings Dialog now hides email Password</td></tr>
				<tr><td valign="top">2.1.2011</td><td> Changed Cursor color in Picture Viewer to make it visible</td></tr>
				<tr><td valign="top">2.1.2011</td><td> Consolidate no longer refuses to work when not consolidation lowres too</td></tr>
				<tr><td valign="top">28.12.2010</td><td> Fixed the bug whereby selections were not moved to a move target</td></tr>
				<tr><td valign="top">1.10.2010</td><td> Geotagging of images</td></tr>
				<tr><td valign="top">5.6.2010</td><td> Improved Reconcile Function</td></tr>
				<tr><td valign="top">5.6.2010</td><td> Added Hover to JTree</td></tr>
				<tr><td valign="top">4.6.2010</td><td> Fixed Export Collection bug</td></tr>
				<tr><td valign="top">18.5.2010</td><td> Added a Find Duplicates function</td></tr>
				<tr><td valign="top">18.5.2010</td><td> Fixed bug with Navigate-to</td></tr>
				<tr><td valign="top">17.5.2010</td><td> Added support for mailing via Hotmail(TM) and Gmail(TM)</td></tr>
				<tr><td valign="top">17.5.2010</td><td> Date parsing when importing picked the first date instead of the date of capture</td></tr>
				<tr><td valign="top">17.5.2010</td><td> Privacy Thumbnail Deletion - Added a Progress Bar</td></tr>
				<tr><td valign="top">14.4.2010</td><td> Added a title to the PicturePopUp menu</td></tr>
				<tr><td valign="top">13.4.2010</td><td> Made consolidate a SwingWorker</td></tr>
				<tr><td valign="top">27.1.2010</td><td> When you doubleclicked a thumbnail to open to picture window the clicked node would stay selected and a delete would try to delete that node instead of the node which is being browsed</td></tr>
				<tr><td valign="top">10.1.2010</td><td> Fixed EDT issues when adding pictures</td></tr>
				<tr><td valign="top">15.9.2009</td><td> Fixed highlighting of selected groups.</td></tr>
				<tr><td valign="top">8.9.2009</td><td> Fixing Swing Event Display Thread problems.</td></tr>
				<tr><td valign="top">8.9.2009</td><td> Fixed issue with internal image type which made gif and png images fail.</td></tr>
				<tr><td valign="top">26.2.2009</td><td> When moving a picture to a different group the PictureViewer will advance to the next picture.</td></tr>
				<tr><td valign="top">13.2.2009</td><td> When adding a directory that contained know images that are not added we can end up with empty directories. No longer.</td></tr>
				<tr><td valign="top">3.2.2009</td><td> The link to the zipfile download was not linking to the correct name.</td></tr>
				<tr><td valign="top">10.1.2009</td><td> Added ability to import a previous collection.</td></tr>
				<tr><td valign="top">10.1.2009</td><td> A bug in Consolidate Group fixed.</td></tr>
				<tr><td valign="top">10.1.2009</td><td> Group thumbnails are now updated after pictures are added to the groups.</td></tr>
				<tr><td valign="top">9.12.2008</td><td> Fixed Issues with original filenames retaining blanks on webpage generation</td></tr>
				<tr><td valign="top">4.12.2008</td><td> Converted the ugli Gui to a wizard for WebPage Generation</td></tr>
				<tr><td valign="top">1.12.2008</td><td> Improved "UP" navigation on generated webpages of large collections</td></tr>
				<tr><td valign="top">1.12.2008</td><td> Cleaner JavaScript</td></tr>
				<tr><td valign="top">1.12.2008</td><td> Option to generate robots.txt</td></tr>
				<tr><td valign="top">30.11.2008</td><td> Redesign of Generate Webpage, cleanup of generated HTML, facility to start at a specified number</td></tr>
				<tr><td valign="top">28.11.2008</td><td> Made scaling thumbnails visually much more appealing</td></tr>
				<tr><td valign="top">12.4.2008</td><td> Fixed a bug with the HTML export. It was writing the Path instead of just the filename</td></tr>
				<tr><td valign="top">6.3.2008</td><td> Implemented Sequential Numbering option in output for Jay.</td></tr>
				<tr><td valign="top">2.3.2008</td><td> Implemented Autoscroll in the JTree.</td></tr>
				<tr><td valign="top">18.11.2007</td><td> Added some error checking for the Camera Download Wizard and mkdirs.</td></tr>
				<tr><td valign="top">18.11.2007</td><td> Camera Download Wizard now select the root node by default if it is the only node in the collection.</td></tr>
				<tr><td valign="top">18.11.2007</td><td> Save Dialog now recognises the autostart collection correctly</td></tr>
				<tr><td valign="top">15.11.2007</td><td> Fixed Tooltips & Keyboard Navigation in Picture Browser</td></tr>
				<tr><td valign="top">11.11.2007</td><td> Improved Camera Editor</td></tr>
				<tr><td valign="top">11.11.2007</td><td> Camera Watch Deamon and Wizard</td></tr>
				<tr><td valign="top">6.11.2007</td><td> Added ability to edit the nodes in the JTree</td></tr>
				<tr><td valign="top">4.11.2007</td><td> Cameras are now saves in the preferences too. Unfortunately all older Camera settings will be lost and the users must set up their cameras again. This is not as bad as it sounds becaus saving the cameras didn't work proerply anyway!</td></tr>
				<tr><td colspan=2><b>Version 0.8.5</b></td></tr>
				<tr><td valign="top">25.8.2007</td><td> After saving the user now has the option to make the selection an Autostart</td></tr>
				<tr><td valign="top">23.8.2007</td><td> Tried to fix the stupid bug with the full screen PictureViewer that never accounted for the taskbar</td></tr>
				<tr><td valign="top">14.7.2007</td><td> Add from Camera now can go direct to Edit Camera and Edit Camera close window saves the changes</td></tr>
				<tr><td valign="top">10.7.2007</td><td> Changed speed of ToolTipManager to make the ToolTips snappier</td></tr>
				<tr><td valign="top">23.6.2007</td><td> Drag to Windows Explorer and Web File upload applets now seems to work</td></tr>
				<tr><td valign="top">2.6.2007</td><td> Fixed a bug with the display of Categories that had been pointed out by Jon Allen</td></tr>
				<tr><td valign="top">2.6.2007</td><td> PictureInfoEditor parses the creation date when the cursor moves in or out of the date field</td></tr>
				<tr><td valign="top">20.5.2007</td><td> Added a progress indicator for loading the files</td></tr>
				<tr><td valign="top">30.1.2007</td><td> Fixed a	bug in HTML Export where the wrong image width for a thumbnail could be written.</td></tr>
				<tr><td valign="top">30.1.2007</td><td> Navigation from Thumbnail added so that you can jump to the groups that refer to this picture.</td></tr>
				<tr><td valign="top">21.1.2007</td><td> You can now select all Thumbnails with Ctrl-A. Also you can add to your selection with the SHIFT and CTRL modifiers.</td></tr>
				<tr><td valign="top">20.1.2007</td><td> Loads of Code Refactoring</td></tr>
				<tr><td valign="top">20.1.2007</td><td> Started to add JUnit test cases</td></tr>
				<tr><td valign="top">18.1.2007</td><td> Added random multi viewer function to menu</td></tr>
				<tr><td valign="top">29.12.2006</td><td> Fixed stupid bug with Directory Chooser on virgin properties.</td></tr>
				<tr><td valign="top">19.12.2006</td><td> Fixed an annoying bug whereby it was not reading the cameras data file.</td></tr>
				<tr><td valign="top">13.11.2006</td><td> Fixed sizing issues.</td></tr>
				<tr><td valign="top">12.11.2006</td><td> Code refactoring to avoid blocking the Even threat when starting up.</td></tr>
				<tr><td valign="top">11.11.2006</td><td> Fixed bug with Consolidate that did not like blanks in directory names.</td></tr>
				<tr><td valign="top">11.11.2006</td><td> Replaced the ini file with a Preferences implementation. The data in the ini file will be parsed before the ini file is removed.</td></tr>
				<tr><td valign="top">4.11.2006</td><td> Window can now be maximised upon start. This solves the wierd Windows OS window sizing behaviour</td></tr>
				<tr><td valign="top">14.10.2006</td><td> Sort double triggered the structure changed event which lead to thumbnails no longer showing.</td></tr>
				<tr><td valign="top">7.10.2006</td><td> Sort didn't set the unsaved changes flag.</td></tr>
				<tr><td valign="top">26.8.2006</td><td> Remove Node now also recognises a selection of multiple nodes.</td></tr>
				<tr><td valign="top">26.7.2006</td><td> Changed the structure of the ThumbnailCreationQueue access and hopefully fixed the deadlock scenario.</td></tr>
				<tr><td valign="top">13.7.2006</td><td> Fixed an annoying bug in the Picture Pane: the mouse release was not being trapped after a drag which lead to counterintuitive behaviour.</td></tr>
				<tr><td valign="top">28.6.2006</td><td> Changed notification texts for better UI handling in Picture Viewer.</td></tr>
				<tr><td valign="top">28.6.2006</td><td> Added ability to change the description in the Picture Viewer</td></tr>
				<tr><td valign="top">28.6.2006</td><td> Improved internationalization support by removing hard coded texts and removing hard coded font instructions.</td></tr>
				<tr><td valign="top">22.6.2006</td><td> Added support for Simplified and Traditional chinese Menus and dialogs. Thanks to Franklin He for the translations.</td></tr>
				<tr><td valign="top">21.6.2006</td><td> Fixed a nasty bug in the GroupBrowser which caused a memory leak and unnecessary relayouting.</td></tr>
				<tr><td valign="top">20.6.2006</td><td> Improved Synchronisation of Thumbnail creation. Should avoid some thread crashes and artefacts.</td></tr>
				<tr><td valign="top">11.6.2006</td><td> Opening without a preload collection didn't call create new collection</td></tr>
				<tr><td valign="top">03.4.2006</td><td> Fixed Bug where German Windows language would prevent JPO switching to English Gui. </td></tr>
				<tr><td valign="top">26.3.2006</td><td> Fixed Bug where a sort of a group with a subgroup might fail. </td></tr>
				<tr><td valign="top">26.3.2006</td><td> Search by Category added </td></tr>
				<tr><td valign="top">28.2.2006</td><td> Introduction of new PictureCollection class that reduces the complexity of the SortableDefaultMutableTreeNode. </td></tr>
				<tr><td valign="top">27.2.2006</td><td> Thumbnails of the pictures being mailed are shown on the Mailing panel. </td></tr>
				<tr><td valign="top">26.2.2006</td><td> Categories can be selected when adding pictures now. </td></tr>
				<tr><td valign="top">26.2.2006</td><td> Improved Search Data structures and navigation </td></tr>
				<tr><td valign="top">25.2.2006</td><td> HTML export now offers zipfile creation with Highres images </td></tr>
				<tr><td valign="top">21.2.2006</td><td> The picture popup menu now allows you to unselect all selected pictures from mailing. </td></tr>
				<tr><td valign="top">21.2.2006</td><td> If you remove a group while it is being displayed in the Thumbnail pane the thumbnail pane now reacts to this and jumps to the parent. </td></tr>
				<tr><td valign="top">21.2.2006</td><td> Emailing now happens in a thread and is more informative about what is going on </td></tr>
				<tr><td valign="top">19.2.2006</td><td> Categories can be assigned when importing from Camera now </td></tr>
				<tr><td valign="top">19.2.2006</td><td> Delete on a selection of Thumbnails now iterates through the thumbnails </td></tr>
				<tr><td valign="top">11.2.2006</td><td> Fixed a bug that could crash the thumbnail scaler processes</td></tr>
				<tr><td valign="top">21.9.2005</td><td> Improved visualisation of selection and ability to select by rectangle. </td></tr>
				<tr><td valign="top">19.9.2005</td><td> Improved user interface to tag pictures with categories. </td></tr>
				<tr><td valign="top">11.9.2005</td><td> After JPO adds pictures it jumps to the new pictures and shows them. </td></tr>
				<tr><td valign="top">4.9.2005</td><td> Quality of Lowres can be independently adjusted from midres quality in HTML export. </td></tr>
				<tr><td valign="top">4.9.2005</td><td> Folder icon not being rendered properly for HTML page fixed </td></tr>
				<tr><td valign="top">1.9.2005</td><td> Proper escaping of special characters in HTML </td></tr>
				<tr><td valign="top">10.6.2005</td><td> Fixed some messed up positioning in the Picture Viewer </td></tr>
				<tr><td valign="top">10.6.2005</td><td> If you had viewed a picture and then closed the viewer then deleted the image the picture viewer would open up and show the next image. Undesirable. Removed</td></tr>
				<tr><td valign="top">10.6.2005</td><td> Fixed annoying behaviour in Picture Pane where an end of drag would zoom in.</td></tr>
				<tr><td valign="top">22.5.2005</td><td> Fixed a bug with consolidate that did not like blanks in a filename.</td></tr>
				<tr><td valign="top">24.4.2005</td><td> Added ability to show Thumbnails in different sizes.</td></tr>
				<tr><td valign="top">18.3.2005</td><td> Added option to not retain directory structures when adding pictures.</td></tr>
				<tr><td valign="top">15.2.2005</td><td> Added rotate buttons to PictureViewer and updated some icons.</td></tr>
				<tr><td valign="top">23.1.2005</td><td> Descriptions in email are encoded in iso-8859-1</td></tr>
				<tr><td valign="top">23.1.2005</td><td> Filenames are preserved on the email.</td></tr>
				<tr><td valign="top">9.1.2005</td><td> Basic Email functionality added</td></tr>
				<tr><td valign="top">1.1.2005</td><td> Fixed a silly bug that wrote collections that could not be read back in</td></tr>
				<tr><td valign="top">07.12.2004</td><td> Added a keyboard shortcut (1) that jumps to full image resolution</td></tr>
				<tr><td valign="top">08.11.2004</td><td> The introduction of multi selection killed the validation of not dropping onto a node's children. This has been reintroduced.</td></tr>
				<tr><td valign="top">06.10.2004</td><td> Selections in one Group were not cleared when switching to another group which caused odd drag and drop behaviour</td></tr>
				<tr><td valign="top">06.10.2004</td><td> Rotation set the angle instead of rotating by the angle. Fixed.</td></tr>
				<tr><td valign="top">06.10.2004</td><td> Fixed a bug in the Pictureviewer: Image would not rotate if user chose a rotate function</td></tr>
				<tr><td valign="top">06.10.2004</td><td> Added a display of how many thumbnails are on the queue</td></tr>
				<tr><td valign="top">28.09.2004</td><td> Fixed the logic of what happens when you are showing an image and then remove it's node or it's parent node</td></tr>
				<tr><td colspan=2><b>Version 0.8.4</b></td></tr>
				<tr><td valign="top">20.08.2004</td><td> Consolidate now also consolidates the group thumbnails</td></tr>
				<tr><td valign="top">20.08.2004</td><td> Bugfix: im thumbnail size was not exactly specified thumbnail size or original image was smaller than thumbnail then thumbnail creation could loop</td></tr>
				<tr><td valign="top">20.08.2004</td><td> Tried to improve code regarding synchronisation which caused GUI freeze</td></tr>
				<tr><td valign="top">05.08.2004</td><td> Mark: Windows build for CT</td></tr>
				<tr><td valign="top">05.08.2004</td><td> After a drop of multiple nodes the nodes remained selected which included them in the next drag and drop which is very confusing to users.</td></tr>
				<tr><td valign="top">02.08.2004</td><td> Bugfix Count of Nodes was giving too high a number</td></tr>
				<tr><td valign="top">02.08.2004</td><td> Bugfix Consolidation counted nodes wrongly when not consolidating sub groups.</td></tr>
				<tr><td valign="top">02.08.2004</td><td> Bugfix switching to Window without Frame would make the picture verz small.</td></tr>
				<tr><td valign="top">02.08.2004</td><td> Dialog Errors on Add from Camera fixed</td></tr>
				<tr><td valign="top">02.08.2004</td><td> Images that do not have the highres online are indicated with a CD icon. This can happen if the highres is on CD.</td></tr>
				<tr><td valign="top">02.08.2004</td><td> Renaming of the Root Node no longer gives an error.</td></tr>
				<tr><td valign="top">02.08.2004</td><td> Group Folder icons now use highres pictures if Thumbnails are not available</td></tr>
				<tr><td valign="top">30.07.2004</td><td> Picture browser would not start up because of a Classloader statement that prevented the icon being loaded</td></tr>
				<tr><td valign="top">30.07.2004</td><td> Fixed a problem with the build file that didn't copy the license and therefore it could not be displayed</td></tr>
				<tr><td valign="top">28.07.2004</td><td> Groups now show miniicons in folder.</td></tr>
				<tr><td valign="top">24.07.2004</td><td> Improved selection highlighting (borders) of Thumbnails.</td></tr>
				<tr><td valign="top">24.07.2004</td><td> Tracked down the setUndavedChanges call to the add routine and stopped it there. This prevents JPO from telling you that there are unsaved changes after you opened a file.</td></tr>
				<tr><td valign="top">06.07.2004</td><td> Fixed the problem with the Umlauts in the German Texts: SuSE9.1 defaults to UTF-8 which is basically a good thing but nedit can't handle this and messes up the characters in the source file. The resources file must only be edited with a UTF-8 aware editor.</td></tr>
				<tr><td valign="top">04.07.2004</td><td> Stopped automatic call of the integrity checker whenever a xml was loaded. This drains resources and is annoying.</td></tr>
				<tr><td valign="top">04.07.2004</td><td> Thumbnails can now be unselected by single clicking on a selected thumbnail</td></tr>
				<tr><td valign="top">08.06.2004</td><td> Fixed a bug where changeing the Number of Thumbnails could cause an ArrayOutOfBounds exception</td></tr>
				<tr><td valign="top">06.06.2004</td><td> Added jar file building and deployment to sourceforge options into the Ant build.xml file</td></tr>
				<tr><td valign="top">06.06.2004</td><td> When a thumbnail was freshly created the thumbnail was loaded twice because of the notification</td></tr>
				<tr><td valign="top">05.06.2004</td><td> When the user selected no Thumbnails on Disk this would cause a loop because of the notification that the thumbnail had changed</td></tr>
				<tr><td valign="top">05.06.2004</td><td> Fixed a missing label on the delete all thumbnails button</td></tr>
				<tr><td valign="top">31.05.2004</td><td> Added German resource bundle and ability to change user language</td></tr>
				<tr><td valign="top">20.05.2004</td><td> Added ability attach categories to pictures and edit categories</td></tr>
				<tr><td valign="top">13.05.2004</td><td> The collection datamodel now allows categories and the XmlReader and XmlWriter support this</td></tr>
				<tr><td valign="top">11.05.2004</td><td> Indent and Outdent functionality added.</td></tr>
				<tr><td valign="top">11.05.2004</td><td> When a picture is moved to a recent move target in the Picture Viewer the Picture Viewer advances to the next picture in the old group. This allows for easier sorting of collection in full screen mode.</td></tr>
				<tr><td valign="top">11.05.2004</td><td> When a node is removed that a Picture Viewer is showing it tries to move to the next picture if there is one. If not it closes the window.</td></tr>
				<tr><td valign="top">11.05.2004</td><td> When a node is removed that was next to a picture the PictureInfoEditor was showing this would wrongly close.</td></tr>
				<tr><td valign="top">11.05.2004</td><td> When updating a description the unsaved attribute is now set on a collection</td></tr>
				<tr><td valign="top">10.05.2004</td><td> Added Multiple select drag and drop</td></tr>
				<tr><td valign="top">10.05.2004</td><td> Added Page display as per Joe Azure's suggestion</td></tr>
				<tr><td valign="top">06.05.2004</td><td> Add from Camera now remembers the target directory properly. Even when cancel is pressed. This improves user friendlyness. </td></tr>
				<tr><td valign="top">04.05.2004</td><td> Introduced <b>Apache-Ant</b>-way of building the application. Make now no longer works. Thanks to Csaba Nagy for gettimg me started.</td></tr>
				<tr><td valign="top">22.03.2004</td><td> Find Dialog improved visually </td></tr>
				<tr><td valign="top">22.03.2004</td><td> The user can now specify the background and text color when generating a web page. </td></tr>
				<tr><td valign="top">22.03.2004</td><td> The info panel now shows the Thumbnail when an image is selected </td></tr>
				<tr><td valign="top">22.03.2004</td><td> Added a check that verifies the checksums of your pictures to the Integrity Checker </td></tr>
				<tr><td valign="top">18.03.2004</td><td> Added a Thumbnail preview to the Add File File Chooser </td></tr>
				<tr><td valign="top">12.03.2004</td><td> Restructured the way the ThumbnailJScrollpane works and Thumbnails. </td></tr>
				<tr><td valign="top">29.02.2004</td><td> Restructured the Find code and added date range searching </td></tr>
				<tr><td valign="top">28.02.2004</td><td> Added an Info Panel underneath the JTree as recommended by Jochen Schaeuble </td></tr>
				<tr><td valign="top">28.02.2004</td><td> Dropping a Group on another Group now brings a popup Menu </td></tr>
				<tr><td valign="top">28.02.2004</td><td> Consolidate can now consolidate the Lowres directories too </td></tr>
				<tr><td valign="top">28.02.2004</td><td> Adding pictures from the camera can now rely on the filename to determine if the picture is known </td></tr>
				<tr><td valign="top">28.02.2004</td><td> Thumbnail queue requests are taken off queue when a new collection is initialised. </td></tr>
				<tr><td valign="top">27.02.2004</td><td> Collection Properties now shows disk space used </td></tr>
				<tr><td valign="top">27.02.2004</td><td> Redesigned the ThumbnailJScrollpane. Only maxThumbnailComponents on the panel. They can load and unload the images.</td></tr>
				<tr><td valign="top">23.02.2004</td><td> Progress monitor for Consolidation</td></tr>
				<tr><td valign="top">23.02.2004</td><td> Fixed a bug where a find with save would crash</td></tr>
				<tr><td valign="top">23.02.2004</td><td> Move targets are now removed when the target node is removed, the collection is initialised or a new collection is loaded</td></tr>
				<tr><td valign="top">20.02.2004</td><td> Picture Browser now has the standard picture popup menu</td></tr>
				<tr><td valign="top">20.02.2004</td><td> Changed data model of main frame dimensions and picutre viewer dimensions to use a rectangle and different way of storing this in the ini file. Reading these values will not work from a previous version to the current one.</td></tr>
				<tr><td valign="top">20.02.2004</td><td> IntegrityChecker window framework added</td></tr>
				<tr><td valign="top">18.02.2004</td><td> The HTML generator now generates HTML with a Cascading Style Sheet</td></tr>
				<tr><td valign="top">05.02.2004</td><td> The divider spot of the main window is properly saved and restored.</td></tr>
				<tr><td valign="top">05.02.2004</td><td> Add from Camera introduced.</td></tr>
				<tr><td valign="top">Dec. 2003</td><td> Reworked major parts of the code to separate model from view.</td></tr>
				<tr><td valign="top">19.11.2003</td><td> If the filename contained an ampersand the XML file of the collection could not be read. Now introduced proper XML escaping on filenames and all other data fields too.</td></tr>
				<tr><td valign="top">17.11.2003</td><td> The title of the Collection was not XML Special Character escaped which cause bad xml and hence load failures. Bug reported by Eugen</td></tr>
				<tr><td valign="top">17.11.2003</td><td> Option write Thumbnails to disk was not saved.</td></tr>
				<tr><td valign="top">16.10.2003</td><td> Drop of a node on itself should simply cancel the drop not present error. Enhancement for Eugen Keller.</td></tr>
				<tr><td colspan=2><b>Version 0.8.3</b></td></tr>
				<tr><td valign="top">29.08.2003</td><td> Re-Release of 0.8.3 with source</td></tr>
				<tr><td valign="top">24.08.2003</td><td> Added the ability to call up user defined programs</td></tr>
				<tr><td valign="top">24.08.2003</td><td> Fixed a bug in the Collection Properties where groups were double counted</td></tr>
				<tr><td valign="top">24.08.2003</td><td> Fixed a bug in the drag and drop of the JTree which would prevent groups and pictures dragged from the Thumbnail pane to work</td></tr>
				<tr><td valign="top">19.08.2003</td><td> Fixed a bug where clicking forward on a small image in a slide show would not advance to the new image</td></tr>
				<tr><td valign="top">19.08.2003</td><td> Fixed some bugs when adding pictures whilst having read protected directories in the selection</td></tr>
				<tr><td valign="top">19.08.2003</td><td> Fixed a bug where Find could add the found results again</td></tr>
				<tr><td valign="top">17.08.2003</td><td> Release of 0.8.3</td></tr>
				<tr><td valign="top">01.08.2003</td><td> Added DHTML effects on HTML page generation</td></tr>
				<tr><td valign="top">29.07.2003</td><td> Fixed an issue where rotate would fail; also fixed rotation logic. </td></tr>
				<tr><td valign="top">29.07.2003</td><td> Fixed an issue where refresh would not refresh the tumbnail </td></tr>
				<tr><td valign="top">30.06.2003</td><td> Redesigned Thread logic on Thumbnail Creation. Now queue based with independent Threads servicing the creation requests.</td></tr>
				<tr><td valign="top">29.06.2003</td><td> Corrected filename retention on File|New, File|Save As</td></tr>
				<tr><td valign="top">03.06.2003</td><td> Rotation of Thumbnail no longer looses color; now using TYPE_USHORT_565_RGB images</td></tr>
				<tr><td valign="top">30.05.2003</td><td> UnsavedChanges are now tracked on the root node and not the JTree level</td></tr>
				<tr><td valign="top">30.05.2003</td><td> Paste in table now handles wrap around when paste area is larger than source area</td></tr>
				<tr><td valign="top">26.05.2003</td><td> Improved micro scroll amount for mousewheel in thumbnail pane</td></tr>
				<tr><td valign="top">26.05.2003</td><td> Improved HTML export so supply width and height of images</td></tr>
				<tr><td valign="top">23.05.2003</td><td> Introduced ability to sort groups</td></tr>
				<tr><td valign="top">10.05.2003</td><td> Internal notification architecture introduced</td></tr>
				<tr><td valign="top">10.05.2003</td><td> Faster rotation of thumbnails</td></tr>

				<tr><td colspan=2><b>Version 0.8.2</b></td></tr>
				<tr><td valign="top">22.03.2003</td><td> Added ability to change descriptions in the Thumbnail Pane</td></tr>
				<tr><td valign="top">22.03.2003</td><td> Responsiveness of Thumbnail pane improved by major redesign</td></tr>
				<tr><td valign="top">20.03.2003</td><td> Added an option to the JFileChooser that adds only new pictures</td></tr>
				<tr><td valign="top">20.03.2003</td><td> Modified the JFileChooser to have an extra panel for Options</td></tr>
				<tr><td valign="top">20.03.2003</td><td> Fixed a bug whereby nested subdirectories were not added if the first directory didn't contrain any pictures</td></tr>
				<tr><td valign="top">10.03.2003</td><td> Added menu entries to Picture Popup Menu for rotation. Added rotation in Background.</td></tr>
				<tr><td valign="top">10.03.2003</td><td> Sorted the issue with the undesirable image format after rotation</td></tr>
				<tr><td valign="top">05.03.2003</td><td> Added keyboard shortcuts to menu functions</td></tr>
				<tr><td valign="top">02.03.2003</td><td> Added ability to rotate image upon load. (However, the color model changes and HTML exported images can't be read by browsers.)</td></tr>
				<tr><td valign="top">02.03.2003</td><td> Moved the EXIF information display to the Picture Properties</td></tr>
				<tr><td valign="top">22.02.2003</td><td> Fixed a bug where adding a single picture could be added to the ThumbnailJScrollpane several times. This was because a Tree Structure Change event and node selection event were coming in at the same time each firing off a layoutThumbnails thread. The selection event now checks to see if the selection changed before laying out anything and the killThread waits a little longer. </td></tr>
				<tr><td valign="top">18.02.2003</td><td> Fixed a bug when laying out thumbnails where files were being opened but not closed which could stop all IO in the JVM. </td></tr>
				<tr><td valign="top">18.02.2003</td><td> Fixed a bug when adding files where files were being opened but not closed which could stop all IO in the JVM. </td></tr>
				<tr><td valign="top">18.02.2003</td><td> Added a progress GUI when adding files </td></tr>
				<tr><td valign="top">12.02.2003</td><td> Selecting a group didn't bring up thumbnails in Web Start environment. Fixed. </td></tr>
				<tr><td valign="top">19.01.2003</td><td> Sorted the odd behaviour when doing drag over on the Thumbnails </td></tr>
				<tr><td valign="top">18.01.2003</td><td> Changes in the Tree were not detected by the Thumbnail pane. They now are the Thumbnails are refreshed. (Bobby) </td></tr>
				<tr><td valign="top">18.01.2003</td><td> The Picture Info Editor fired a change event before any change was applied. </td></tr>
				<tr><td valign="top">18.01.2003</td><td> Consolidation wasn't monitoring for failures </td></tr>
				<tr><td valign="top">18.01.2003</td><td> If an image has a faulty URL the HtmlDistiller now uses the broken thumbnail icon </td></tr>

				<tr><td colspan=2><b>Version 0.8.1</b></td></tr>
				<tr><td valign="top">15.01.2003</td><td> The JFileChooser in Copy to Target Directory was not behaving as specified </td></tr>
				<tr><td valign="top">15.01.2003</td><td> The copy picture could overwrite a picture file in the target directory. Now a new name is given. </td></tr>
				<tr><td valign="top">15.01.2003</td><td> Open Recent was't always getting files that were opened or saved fixed </td></tr>
				<tr><td valign="top">15.01.2003</td><td> Recently used directories was not being updated correctly in all places </td></tr>
				<tr><td valign="top">15.01.2003</td><td> Added a popup after save to confirm successful saving </td></tr>
				<tr><td colspan=2><b>Version 0.8</b></td></tr>
				<tr><td valign="top">14.01.2003</td><td> Edit | Collection Properties | Protect collection from Edits is now saved with the collecton. Required DTD change. </td></tr>
				<tr><td valign="top">14.01.2003</td><td> Upgraded to Drew Noakes' 2.1 version of the EXIF and IPTC extractor library </td></tr>
				<tr><td valign="top">14.01.2003</td><td> Consolidate screen has Export instead of Consolidate in the button. Also fixed some comments. </td></tr>
				<tr><td valign="top">14.01.2003</td><td> Inserts and deletes of nodes do not automatically move the selected node </td></tr>
				<tr><td valign="top">11.01.2003</td><td> Group popup and Picture Popup Menus now respect the allow edits property </td></tr>
				<tr><td valign="top">11.01.2003</td><td> Disabled Jar Extraction; It's not thought through and running the generated jar is too difficult</td></tr>
				<tr><td valign="top">09.01.2003</td><td> Fixed a bug in the color validation on the Autoloader selection field.</td></tr>
				<tr><td valign="top">09.01.2003</td><td> Added an error dialog when a file delete failed.</td></tr>
				<tr><td valign="top">09.01.2003</td><td> Fixed a bug where the html export was generating a tag without right bracket</td></tr>
				<tr><td valign="top">09.01.2003</td><td> Fixed a bug where the wrong folder icon was displayed in the tree</td></tr>
				<tr><td valign="top">01.01.2003</td><td> Added Collection properties dialog </td></tr>
				<tr><td valign="top">29.12.2002</td><td> Added Cut & Paste to Table editing </td></tr>
				<tr><td valign="top">29.12.2002</td><td> Delete all Thumbnails Button added to Settings Dialog </td></tr>
				<tr><td valign="top">28.12.2002</td><td> If nodes are removed for which a PictureEditor is open this is notified and closed.</td></tr>
				<tr><td valign="top">28.12.2002</td><td> Thumbnails and Treenodes now have same popup menu</td></tr>
				<tr><td valign="top">28.12.2002</td><td> Fixed logic issues with cache and concurrency</td></tr>
				<tr><td valign="top">26.12.2002</td><td> Added File Delete and File Rename options</td></tr>
				<tr><td valign="top">15.12.2002</td><td> Show on GroupPopup menu renamed to Slideshow and will now recursively search for the first picture</td></tr>
				<tr><td valign="top">15.12.2002</td><td> Popup menu on groups in browser panel now takes effect on correct node </td></tr>
				<tr><td valign="top">15.12.2002</td><td> Bug in search fixed: the correct node is mentioned </td></tr>
				<tr><td valign="top">06.12.2002</td><td> Open Recent added </td></tr>
				<tr><td valign="top">27.11.2002</td><td> Doubleclick on a picture node in the tree now opens the picture </td></tr>
				<tr><td valign="top">26.11.2002</td><td> Fixed some problems with drag and drop on the root node</td></tr>
				<tr><td valign="top">25.11.2002</td><td> Introduced a progress tracker on the Image Loading</td></tr>
				<tr><td valign="top">24.11.2002</td><td> If Present the date in the Image (EXIF) is extracted and used when adding pictures</td></tr>
				<tr><td valign="top">24.11.2002</td><td> Implemented Drew Noakes' 1.2 version of EXIF extractor </td></tr>
				<tr><td valign="top">20.11.2002</td><td> Started transition to Java Web Start</td></tr>
				<tr><td valign="top">19.11.2002</td><td> Reworked Add command. Multi select is possible and add single / add directory done away with</td></tr>
				<tr><td valign="top">10.11.2002</td><td> Save As now prompts for file overwrite if target already exists </td></tr>
				<tr><td valign="top">22.10.2002</td><td> Add single file, Add directory and Add file from flat file now only put the root of the filename into the description field </td></tr>
				<tr><td valign="top">22.10.2002</td><td> Fixed Bug in WritableDirectoryChooser where a null parent was being checked for writability </td></tr>
				<tr><td valign="top">20.10.2002</td><td> Added group as table editing ability </td></tr>
				<tr><td valign="top">26.09.2002</td><td> Jpg writing quality can now be specified by HTML renderer </td></tr>
				<tr><td valign="top">26.09.2002</td><td> Added Midres Format to HTML output </td></tr>
				<tr><td valign="top">24.09.2002</td><td> Added Progress Indicator to Thumbnail Panel </td></tr>
				<tr><td valign="top">23.09.2002</td><td> Added popup menu to thumbnails </td></tr>
				<tr><td valign="top">23.09.2002</td><td> Added doubleclick on thumbnail opens viewer </td></tr>
				<tr><td valign="top">17.09.2002</td><td> Converted most number fields to an extended class that validates for digits only </td></tr>
				<tr><td valign="top">17.09.2002</td><td> Fixed bugs with the cache function and html export routines </td></tr>
				<tr><td valign="top">15.09.2002</td><td> Fixed validation color setting bug in WritableDirectoryChooser and added component to SettingsDialog and HtmlDistillerJFrame classes </td></tr>
				<tr><td valign="top">12.09.2002</td><td> Built WritableDirectoryChooser component </td></tr>
				<tr><td valign="top">12.09.2002</td><td> Cleaned up SettingsDialog Box, added max picture size parameter for editing and picture window Size parameters </td></tr>
				<tr><td valign="top">10.09.2002</td><td> Picture Viewer now has more sensible default size and reads location from ini </td></tr>
				<tr><td valign="top">10.09.2002</td><td> Fixed concurrent access of cached images that have not yet been loaded </td></tr>
				<tr><td valign="top">08.09.2002</td><td> Added validation for JVM version >= 1.4.0 </td></tr>
				<tr><td valign="top">08.09.2002</td><td> Introduced self-restarting if started without parameters </td></tr>
				<tr><td valign="top">07.09.2002</td><td> Introduced ResourceBundle for translatable texts </td></tr>
				<tr><td valign="top">06.09.2002</td><td> Fixed HTML generation which was doing silly things with filenames</td></tr>
				<tr><td valign="top">05.09.2002</td><td> Fixed code for dealing with filenames with blanks in them</td></tr>
				<tr><td valign="top">03.09.2002</td><td> Added color validation to SettingsDialog for logfile setup</td></tr>
				<tr><td valign="top">03.09.2002</td><td> Added improved first use logic for logfile</td></tr>
				<tr><td valign="top">01.09.2002</td><td> Added ability to drag thumbnails</td></tr>
				<tr><td valign="top">01.09.2002</td><td> Re-Introduced Caching</td></tr>

				<tr><td colspan=2><b>Release 0.7</b></td></tr>
				<tr><td valign="top">24.08.2002</td><td> Added reconcile function to Tools Menu</td></tr>
				<tr><td valign="top">10.08.2002</td><td> Sorted out Drag and Drop on the JTree</td></tr>
				<tr><td valign="top">01.08.2002</td><td> Reworked the logic behind maximising the fullscreen window (fullscreen, left only, right only etc.)</td></tr>
				<tr><td valign="top">31.07.2002</td><td> Add single picture didn't format the URL correctly</td></tr>
				<tr><td valign="top">31.07.2002</td><td> Added ability to consolidate all pictures of a group structure into the same directory</td></tr>
				<tr><td valign="top">31.07.2002</td><td> Improved display handling of the node updates when adding a directory of pictures</td></tr>

				<tr><td colspan=2><b>Release 0.6</b></td></tr>
				<tr><td valign="top">16.06.2002</td><td> Added ability to choose whether Window position and size should be saved on exit.</td></tr>
				<tr><td valign="top">14.06.2002</td><td> Added Add Flatfile function.</td></tr>
				<tr><td valign="top">21.04.2002</td><td> Added tickbox to Settings screen to allow thumbnail storing to be turned off.</td></tr>
				<tr><td valign="top">21.04.2002</td><td> Improved initialisation of app in virgin environments (no ini file).</td></tr>
				<tr><td valign="top">20.04.2002</td><td> Changed the clock icon to indicate if the Autoadvance is on of off.</td></tr>
				<tr><td valign="top">20.04.2002</td><td> Added log file ability to app & fields on Settings dialog to set up.</td></tr>
				<tr><td valign="top">13.04.2002</td><td> Added ini File validation for thumbnail directory.</td></tr>
				<tr><td valign="top">12.04.2002</td><td> Changed logic in thumbnail creation to validate URLs before attempting to read and write to them, avoiding ugly and unnecessary errors.</td></tr>
				<tr><td valign="top">05.04.2002</td><td> Changed "Edit Group Description" popup menu item to "Rename"</td></tr>

				<tr><td colspan=2><b>Release 0.5</b></td></tr>
				<tr><td valign="top">15.3.2002</td><td> Fixed bug in add collection in that empty group was created if filechooser was closed</td></tr>
				<tr><td valign="top">14.3.2002</td><td> Added logic to identify where the same filename was being used in JAR export and rename it</td></tr>
				<tr><td valign="top">14.3.2002</td><td> Changed several dialogs to anchor themselves off the main application frame.</td></tr>
				<tr><td valign="top">12.3.2002</td><td> Export to Jar received a progress monitor and the user can interrupt the export.</td></tr>
				<tr><td valign="top">11.3.2002</td><td> Image Copy has improved validation, file extensions are preserved, filename of source is picked up correctly.</td></tr>
				<tr><td valign="top">11.3.2002</td><td> Push directory names validation works properly.</td></tr>

				<tr><td colspan=2><b>Release 0.4</b><br>
				<tr><td valign="top">8.3.2002</td><td> Thumbnails now consider where they are stored and will create new filenames in the thumbnail directory if they are not from local filesystem</td></tr>
				<tr><td valign="top">8.3.2002</td><td> Added jar export and jar autostart feature</td></tr>
				<tr><td valign="top">5.3.2002</td><td> Fixed bug in thumbnail name assignment</td></tr>
				<tr><td valign="top">3.3.2002</td><td> Added button to Picture Edit dialog to choose file</td></tr>
				<tr><td valign="top">3.3.2002</td><td> Added button to Settings Dialog to choose directory of thumbnails</td></tr>
				<tr><td valign="top">3.3.2002</td><td> Fixed bug where full screen image would not close</td></tr>
				<tr><td valign="top">3.3.2002</td><td> Added export to flat file</td></tr>

				<tr><td colspan=2><b>Release 0.3</b></td></tr>
				<tr><td valign="top">2.3.2002</td><td> Added Auto Timer functionality</td></tr>
				<tr><td valign="top">28.2.2002</td><td> Added license dialog and started adding copyright tags to source</td></tr>
				<tr><td valign="top">28.2.2002</td><td> Added functionality that when an image doesn't exist a thumbnail of a broken image is displayed</td></tr>
				<tr><td valign="top">28.2.2002</td><td> The PictureInfoEditor shows the path of the image in red if the image is not found</td></tr>
				<tr><td valign="top">28.2.2002</td><td> Removed the debugging info from the thumbnail creation class</td></tr>
				<tr><td valign="top">28.2.2002</td><td> Thumbnail Path added to Settings & Settings dialog box</td></tr>
				<tr><td valign="top">28.2.2002</td><td> Add single picture functionality coded</td></tr>
				<tr><td valign="top">27.2.2002</td><td> Add from collection coded for group popup menu</td></tr>
				<tr><td valign="top">27.2.2002</td><td> Add new group function coded for group popup menu</td></tr>
				<tr><td valign="top">26.2.2002</td><td> File | Open menu item was linked to wrong command</td></tr>
				<tr><td valign="top">26.2.2002</td><td> Out of Memory Problem tracked down to the way the -Xms and -Xmx commands were submitted</td></tr>

				<tr><td colspan=2><b>Release 0.2</b></td></tr>
				<tr><td valign="top">23.2.2002</td><td> Picture Popup menu is now a separate group</td></tr>
				<tr><td valign="top">23.2.2002</td><td> Main Menu is a separate group</td></tr>
				<tr><td valign="top">23.2.2002</td><td> Move of functions from Fotoalbum class to Clever JTree class, encapsulating the methods and functions.</td></tr>
				<tr><td valign="top">23.2.2002</td><td> The Picture pane no longer automatically opens when the row selection in the JTree moves to a picture.</td></tr>

				<tr><td colspan=2><b>Release 0.1</b></td></tr>
				<tr><td valign="top">20.2.2002</td><td> First Release with core functionality</td></tr>
			</table>


			<hr>
			<h2 id="Bugs">Bugs</h2>

			<table>
				<tr><td valign="top">1</td><td>Drop in the Thumbnail pane is not working correctly - Fixed 19.1.2003</td></tr>
				<tr><td valign="top">2</td><td>Clicking on the three dot icon in the Info Window and not selecting a file can throw null pointer errors and stuff</td></tr>
			</table>



			<hr>
			<h2 id="PotentialFeatures">Potential Future Features</h2>

			<table>
				<tr><td valign="top">1</td><td>Collection Extraction to Zip file and ability to load images from Zip file</td></tr>
				<tr><td valign="top">2</td><td>Thumbnails for Groups could show mini-icons of some pictures in the group</td></tr>
				<tr><td valign="top">3</td><td>Some sort of integration with email to send pictures directly</td></tr>
			</table>

			<hr>
			<p>Last update to this page: 27 Mar 2011<br>
			Copyright 2003-2011 by Richard Eigenmann, Z&uuml;rich, Switzerland</p>
		</td>
	</tr>
</table>
</body>
</html>