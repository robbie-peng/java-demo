package com.vladium.demo;


import java.net.URL;

// ----------------------------------------------------------------------------
/**
 * A simple interface to extract the path, query, and anchor parts from a URL.
 * Implementing it using java.net.URL calls for functionality not uniformly
 * supported across different Java versions. {@link URLParserFactory} acts as
 * the Factory for this API.
 * 
 * @see URLTest2
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public interface IURLParser
{
    // public: ................................................................
    
    /**
     * Given a non-null URL this method return an array of path, query, and
     * reference components of the URL, in that order.
     */
    String [] splitURL (URL url); 

} // end of interface
// ----------------------------------------------------------------------------