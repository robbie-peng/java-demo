
package com.vladium.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

// ----------------------------------------------------------------------------
/**
 * A simple tool to dump class version stamps from a given set of classes. The
 * input classes could be enumerated as standalone .class files or as contained
 * in class libraries. This class recurses into all directories it encounters.<P>
 * 
 * Usage: DumpClassVersions &lt;file|dir|archive&gt; &lt;file|dir|archive&gt; ...<P>
 * 
 * This class is Java 1.1-compatible. It also serves as another demo of how to
 * use {@link IJREVersion}.
 * 
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public class DumpClassVersions implements IJREVersion
{
    // public: ................................................................
    
    public static final String USAGE = "usage: DumpClassVersions <list of dirs, class files, or .zip/.jar archives>";
    
    public static void main (final String [] args)
        throws IOException
    {
        if (args.length == 0)
            throw new IllegalArgumentException (USAGE);
        
        final PrintWriter stdout = new PrintWriter (System.out, true);
        
        s_minMajor = Short.MAX_VALUE;
        
        for (int a = 0; a < args.length; ++ a)
        {
            dumpClassVersion (new File (args [a]), stdout);
        }
        
        if (s_classCount > 0)
        {
            stdout.println ("-----------------------------------");
            stdout.println (s_classCount + " class(es) examined:");
            stdout.println ("min version: " + s_minMajor + "." + s_minMinor + "\t" + classVersionToTarget (s_minMajor, s_minMinor));
            stdout.println ("max version: " + s_maxMajor + "." + s_maxMinor + "\t" + classVersionToTarget (s_maxMajor, s_maxMinor));
        }
        else
        {
            stdout.println ("no classes found");
        }
    }
    
    /**
     * Examines all class definitions that could be found in 'file' [which could
     * be a directory or zip/jar archive] and dumps their version stamps to 'out'.
     * 
     * @throws IOException on any I/O error
     */
    public static void dumpClassVersion (final File file, final PrintWriter out)
        throws IOException
    {
        if (file == null)
            throw new IllegalArgumentException ("null input: file");  
        
        if (out == null)
            throw new IllegalArgumentException ("null input: out");

        if (! file.exists ())
            throw new IllegalArgumentException ("does not exist: [" + file + "]");
            
        
        final String lcName = file.getName ().toLowerCase ();
        
        if (file.isDirectory ())
        {
            dumpDirectory (file, out); // recurse
        }
        else if (lcName.endsWith (".zip") || lcName.endsWith (".jar"))
        {
            dumpArchive (file, out);
        }
        else if (lcName.endsWith (".class"))
        {
            dumpClassFile (file, out);
        }
    }
    
    // protected: .............................................................

    // package: ...............................................................
    
    // private: ...............................................................


    private static void dumpClassFile (final File file, final PrintWriter out)
        throws IOException
    {
        InputStream in = null;
        try
        {
            in = new BufferedInputStream (new FileInputStream (file), 8);
            dumpClassFile (file.getName (),
                JRE_1_2_PLUS
                ?
                    file.getParentFile ().toURL ().toExternalForm ()
                :
                    file.getParent (),
                in , out);
        }
        finally
        {
            if (in != null) try { in.close (); } catch (Exception ignore) {}
        }
    }
    
    private static void dumpClassFile (final String className, final String location,
                                         final InputStream in, final PrintWriter out)
        throws IOException
    {
        in.skip (4); // skip magic number
        
        final int minor = ((0xFF & in.read ()) << 8) | (0xFF & in.read ());
        final int major = ((0xFF & in.read ()) << 8) | (0xFF & in.read ());
        
        updateStats (major, minor);
        out.println (major + "." + minor + "\t" + classVersionToTarget (major, minor) + "\t" + className + "\t" + location);  
    }
    
    private static void dumpDirectory (final File dir, final PrintWriter out)
        throws IOException
    {
        if (JRE_1_2_PLUS)
        {
            final File [] files = dir.listFiles ();
            
            for (int f = 0; f < files.length; ++ f)
            {
                dumpClassVersion (files [f], out);
            }
        }
        else
        {
            final String [] files = dir.list ();
            
            for (int f = 0; f < files.length; ++ f)
            {
                dumpClassVersion (new File (dir, files [f]), out);
            }
        }
    }
    
    private static void dumpArchive (final File zip, final PrintWriter out)
        throws IOException
    {
        ZipInputStream in = null;
        try
        {
            in = new ZipInputStream (new BufferedInputStream (new FileInputStream (zip), 32 * 1024));
            for (ZipEntry entry; (entry = in.getNextEntry ()) != null; )
            {
                final String lcName = entry.getName ().toLowerCase ();
                
                if (! entry.isDirectory () && lcName.endsWith (".class"))
                {
                    dumpClassFile (entry.getName (), zip.toString (), in, out);
                }
            }
        }
        finally
        {
            if (in != null) try { in.close (); } catch (Exception ignore) {}
        }
    }
    
    /*
     * Converts a major/minor version pair to supported target. 
     */
    private static String classVersionToTarget (final int major, final int minor)
    {
        final long version = (major << 16) | minor;
        
        if (version >= 0x00300000)
            return "1.4";
        else if (version >= 0x002F0000)
            return "1.3";
        else if (version >= 0x002E0000)
            return "1.2";
        else if (version >= 0x002D0003)
            return "1.1";
        else if (version >= 0x002D0000)
            return "1.0";
        else
            return "unknown";
    }
    
    /*
     * Updates some simple stats (min/max version seen so far).
     */
    private static void updateStats (final int major, final int minor)
    {
        final long version = (major << 16) | minor;
        
        final long maxVersion = (s_maxMajor << 16) | s_maxMinor;
        final long minVersion = (s_minMajor << 16) | s_minMinor;
        
        if (version > maxVersion)
        {
            s_maxMajor = major;
            s_maxMinor = minor;
        }
        
        if (version < minVersion)
        {
            s_minMajor = major;
            s_minMinor = minor;
        }
        
        ++ s_classCount;
    }
    
    
    private static int s_classCount; 
    private static int s_minMajor, s_minMinor;
    private static int s_maxMajor, s_maxMinor;

} // end of class
// ----------------------------------------------------------------------------