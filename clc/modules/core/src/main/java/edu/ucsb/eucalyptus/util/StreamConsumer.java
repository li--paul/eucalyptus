/*************************************************************************
 * Copyright 2008 Regents of the University of California
 * Copyright 2009-2012 Ent. Services Development Corporation LP
 *
 * Redistribution and use of this software in source and binary forms,
 * with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *   Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer
 *   in the documentation and/or other materials provided with the
 *   distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. USERS OF THIS SOFTWARE ACKNOWLEDGE
 * THE POSSIBLE PRESENCE OF OTHER OPEN SOURCE LICENSED MATERIAL,
 * COPYRIGHTED MATERIAL OR PATENTED MATERIAL IN THIS SOFTWARE,
 * AND IF ANY SUCH MATERIAL IS DISCOVERED THE PARTY DISCOVERING
 * IT MAY INFORM DR. RICH WOLSKI AT THE UNIVERSITY OF CALIFORNIA,
 * SANTA BARBARA WHO WILL THEN ASCERTAIN THE MOST APPROPRIATE REMEDY,
 * WHICH IN THE REGENTS' DISCRETION MAY INCLUDE, WITHOUT LIMITATION,
 * REPLACEMENT OF THE CODE SO IDENTIFIED, LICENSING OF THE CODE SO
 * IDENTIFIED, OR WITHDRAWAL OF THE CODE CAPABILITY TO THE EXTENT
 * NEEDED TO COMPLY WITH ANY SUCH LICENSES OR RIGHTS.
 ************************************************************************/

package edu.ucsb.eucalyptus.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.eucalyptus.system.Threads;

public class StreamConsumer extends Thread {
    private InputStream is;
    private File file;
    private String returnValue;
    private int chunkSize;
    
    public StreamConsumer(InputStream is) {
        super( Threads.threadUniqueName( "eucalyptus-stream-consumer" ) );
        this.is = is;
        returnValue = "";
        this.chunkSize = SystemUtil.IO_CHUNK_SIZE;
    }
    
    public StreamConsumer(InputStream is, int ioChunkSize) {
        super( Threads.threadUniqueName( "eucalyptus-stream-consumer" ) );
        this.is = is;
        returnValue = "";
        this.chunkSize = ioChunkSize;
    }
    
    public StreamConsumer(InputStream is, File file, int ioChunkSize) {
        this(is, ioChunkSize);
        this.file = file;
    }
    
    public StreamConsumer(InputStream is, File file) {
        this(is);
        this.file = file;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void run() {
        BufferedOutputStream outStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            BufferedInputStream inStream = new BufferedInputStream(is);
            if (file != null) {
                fileOutputStream = new FileOutputStream(file);
				outStream = new BufferedOutputStream(fileOutputStream);
            }
            byte[] bytes = new byte[this.chunkSize];
            int bytesRead;
            while ((bytesRead = inStream.read(bytes)) > 0) {
                returnValue += new String(bytes, 0, bytesRead);
                if (outStream != null) {
                    outStream.write(bytes, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
            		is.close();
					outStream.close();
	                fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
    }
}
