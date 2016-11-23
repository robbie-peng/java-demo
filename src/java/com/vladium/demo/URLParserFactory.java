package com.vladium.demo;

import java.net.URL;

import com.vladium.utils.IJREVersion;

// ----------------------------------------------------------------------------
/**
 * A Factory for {@link IURLParser}. This class hides the details of Java version-
 * dependent dynamic class loading from the client code. See inside for further
 * details. 
 * 
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public abstract class URLParserFactory implements IJREVersion
{
    // public: ................................................................
    
    /**
     * Creates and returns a new instance of {@link IURLParser} implementation.
     */
    public static IURLParser newURLParser ()
    {
        // it feels extra "safe" to use Class.forName() here but in reality
        // I don't think it is necessary, as long as you are careful in insulating
        // all version-specific code inside the appropriate classes and instantiating
        // them correctly for the current JRE version:
         
        if (JRE_1_3_PLUS)
            return new JRE13URLParser ();
        else
            return new JRE11URLParser ();
    }    
    
    // protected: .............................................................

    // package: ...............................................................
    
    // private: ...............................................................
    
    /*
     * The pre-Java 1.3 implementation of IURLParser. 
     */
    private static class JRE11URLParser implements IURLParser
    {
        public String [] splitURL (final URL url)
        {
            if (url == null) throw new IllegalArgumentException ("null input: url");
            
            final String path, reference, query;
            reference = url.getRef ();
            
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
            
            return new String [] {path, reference, query};
        }
        
    } // end of nested class
    
    /*
     * The Java 1.3+ implementation of IURLParser. 
     */
    private static class JRE13URLParser implements IURLParser
    {
        public String [] splitURL (final URL url)
        {
            if (url == null) throw new IllegalArgumentException ("null input: url");
            
            return new String [] {url.getPath (), url.getRef (), url.getQuery ()};
        }
        
    } // end of nested class  
    
    
    private URLParserFactory () {} // prevent subclassing

} // end of class
// ----------------------------------------------------------------------------