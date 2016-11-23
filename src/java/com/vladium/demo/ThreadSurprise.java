package com.vladium.demo;


// ----------------------------------------------------------------------------
/**
 * What do you think this should do at runtime? Try compiling and running
 * (it is important to always recompile) this class in different J2SDKs: you
 * will see very inconsistent behavior. I am convinced that Sun's compilers from
 * J2SDKs v1.2 and v1.4 produce correct bytecode and v1.1 or v1.3 do not. 
 * 
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public class ThreadSurprise
{
    // public: ................................................................
    
    public static void main (String [] args)
        throws Exception
    {
        Thread [] threads = new Thread [0];
        threads [-1].sleep (1); // should this throw?
    }
    
} // end of class
// ----------------------------------------------------------------------------