package com.vladium.demo;


import java.net.MalformedURLException;
import java.net.URL;

// ----------------------------------------------------------------------------
/**
 * A demo of how you would write Java version-dependent code by doing
 * dynamic class loading. This class accomplishes the same goal of extracting
 * path, query, and reference components of a URL in different ways in different
 * runtimes by relying on {@link URLParserFactory} to load the appropriate Java
 * version-dependent implementation of {@link IURLParser}.<P>
 * 
 * This strategy is very general and will work in most cases. An alternative,
 * simpler but less general, strategy is shown in {@link URLTest1}.<P>
 * 
 * Usage: java URLTest2 "some URL string"
 * 
 * @see URLParserFactory
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public class URLTest2
{
    // public: ................................................................

    public static void main (final String [] args)
        throws MalformedURLException
    {
        URL url = new URL (args [0]);
        
        // this is where the version check takes place: 
        final IURLParser parser = URLParserFactory.newURLParser ();
        System.out.println ("got IURLParser implementation: " + parser);
        
        final String [] split = parser.splitURL (url);
        
        System.out.println ("path: " + split [0]);
        System.out.println ("ref: " + split [1]);
        System.out.println ("query: " + split [2]);
        System.out.println ();
    }

} // end of class
// ----------------------------------------------------------------------------