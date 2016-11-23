package com.vladium.demo;


import java.net.MalformedURLException;
import java.net.URL;

import com.vladium.utils.IJREVersion;

// ----------------------------------------------------------------------------
/**
 * A demo of how you would write Java version-dependent code without doing
 * dynamic class loading. This class accomplishes the same goal of extracting
 * path, query, and reference components of a URL in different ways in different
 * runtimes by switching on static constants in {@link com.vladium.utils.IJREVersion}.<P>
 * 
 * An alternative strategy is shown in {@link URLTest2}.<P>
 * 
 * Usage: java URLTest1 "some URL string"
 * 
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public class URLTest1 implements IJREVersion
{
    // public: ................................................................

    public static void main (final String [] args)
        throws MalformedURLException
    {
        URL url = new URL (args [0]);
        
        final String path, reference, query;
        
        // getRef() has been available since Java 1.1:
        reference = url.getRef ();
        
        if (JRE_1_3_PLUS)
        {
            // in Java 1.3+ everything is easy:
            query = url.getQuery ();
            // [note: J2SDK javadocs fail to mention that getPath() was added
            // in version 1.3]
            path = url.getPath ();
        }
        else
        {
            // prior to Java 1.3 I have to do extra work:
            final String file = url.getFile ();
            
            final int qindex = file.indexOf ('?');
            final int pindex = file.indexOf ('#');
            
            if (qindex >= 0)
            {
                path = file.substring (0, qindex);
                
                if (pindex >= 0)
                    query = file.substring (qindex + 1, pindex);
                else
                {
                    if (qindex < file.length () - 1)
                        query = file.substring (qindex + 1);
                    else
                        query = "";
                }
            }
            else
            {
                query = null;
                
                if (pindex >= 0)
                    path = file.substring (pindex);
                else
                    path = file;
            } 
        }
        
        System.out.println ("path: " + path);
        System.out.println ("ref: " + reference);
        System.out.println ("query: " + query);
        System.out.println ();
    }

} // end of class
// ----------------------------------------------------------------------------