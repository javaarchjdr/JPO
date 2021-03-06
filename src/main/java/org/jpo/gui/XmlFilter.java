package org.jpo.gui;

import java.io.File;


/*
 XmlFilter.java:  a filter to select only xml files

 Copyright (C) 2002,2009  Richard Eigenmann.
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or any later version. This program is distributed 
 in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 without even the implied warranty of MERCHANTABILITY or FITNESS 
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for 
 more details. You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 The license is in gpl.txt.
 See http://www.gnu.org/copyleft/gpl.html for the details.
 */
/**
 * This class overrides the abstract javax.swing.filechoose.FileFilter class
 * (not the java.io.FileFilter) to provide a true or false indicator to using
 * JFileChoosers whether the file is a directory or image ending in .xml. 
 *
 */
public class XmlFilter extends javax.swing.filechooser.FileFilter {

    /**
     * accepts directories and files ending in .xml
     *
     * @param file file
     * @return true if the files ends in .xml
     */
    @Override
    public boolean accept( File file ) {
        return file.isDirectory()
                || file.getAbsolutePath().toUpperCase().endsWith( "XML" );
    }

    /**
     * returns the description "XML Files"
     *
     * @return the description "XML Files"
     */
    @Override
    public String getDescription() {
        return "XML Files";
    }
}
