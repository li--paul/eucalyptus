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

package com.eucalyptus.ws.util;

import java.util.concurrent.ThreadFactory;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import com.eucalyptus.system.Threads;

public class ChannelUtil {
  private static Logger LOG = Logger.getLogger( ChannelUtil.class );
  
  static class SystemThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread( final Runnable r ) {
      return Threads.newThread( r, "channels" );
    }
  }
  
  public static ChannelFutureListener DISPATCH( Object o ) {
    return new DeferedWriter( o, ChannelFutureListener.CLOSE );
  }
  
  public static ChannelFutureListener WRITE_AND_CALLBACK( Object o, ChannelFutureListener callback ) {
    return new DeferedWriter( o, callback );
  }
  
  public static ChannelFutureListener WRITE( Object o ) {
    return new DeferedWriter( o, new ChannelFutureListener( ) {
      public void operationComplete( ChannelFuture future ) throws Exception {}
    } );
  }
  
  private static class DeferedWriter implements ChannelFutureListener {
    private Object                request;
    private ChannelFutureListener callback;
    
    DeferedWriter( final Object request, final ChannelFutureListener callback ) {
      this.callback = callback;
      this.request = request;
    }
    
    @Override
    public void operationComplete( ChannelFuture channelFuture ) {
      if ( channelFuture.isSuccess( ) ) {
        channelFuture.getChannel( ).write( request ).addListener( callback );
      } else {
        LOG.debug( channelFuture.getCause( ), channelFuture.getCause( ) );
        try {
          callback.operationComplete( channelFuture );
        } catch ( Exception e ) {
          LOG.debug( e, e );
        }
      }
    }
    
  }
  
}
