/*******************************************************************************
 * Copyright (c) 2009  Eucalyptus Systems, Inc.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, only version 3 of the License.
 * 
 * 
 *  This file is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 * 
 *  You should have received a copy of the GNU General Public License along
 *  with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  Please contact Eucalyptus Systems, Inc., 130 Castilian
 *  Dr., Goleta, CA 93101 USA or visit <http://www.eucalyptus.com/licenses/>
 *  if you need additional information or have any questions.
 * 
 *  This file may incorporate work covered under the following copyright and
 *  permission notice:
 * 
 *    Software License Agreement (BSD License)
 * 
 *    Copyright (c) 2008, Regents of the University of California
 *    All rights reserved.
 * 
 *    Redistribution and use of this software in source and binary forms, with
 *    or without modification, are permitted provided that the following
 *    conditions are met:
 * 
 *      Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 * 
 *      Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 * 
 *    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 *    IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *    TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *    PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 *    OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *    EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *    PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *    PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. USERS OF
 *    THIS SOFTWARE ACKNOWLEDGE THE POSSIBLE PRESENCE OF OTHER OPEN SOURCE
 *    LICENSED MATERIAL, COPYRIGHTED MATERIAL OR PATENTED MATERIAL IN THIS
 *    SOFTWARE, AND IF ANY SUCH MATERIAL IS DISCOVERED THE PARTY DISCOVERING
 *    IT MAY INFORM DR. RICH WOLSKI AT THE UNIVERSITY OF CALIFORNIA, SANTA
 *    BARBARA WHO WILL THEN ASCERTAIN THE MOST APPROPRIATE REMEDY, WHICH IN
 *    THE REGENTS' DISCRETION MAY INCLUDE, WITHOUT LIMITATION, REPLACEMENT
 *    OF THE CODE SO IDENTIFIED, LICENSING OF THE CODE SO IDENTIFIED, OR
 *    WITHDRAWAL OF THE CODE CAPABILITY TO THE EXTENT NEEDED TO COMPLY WITH
 *    ANY SUCH LICENSES OR RIGHTS.
 *******************************************************************************
 * @author chris grzegorczyk <grze@eucalyptus.com>
 */

package com.eucalyptus.ws;

import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Entity;
import com.eucalyptus.bootstrap.Databases;
import com.eucalyptus.configurable.ConfigurableClass;
import com.eucalyptus.configurable.ConfigurableField;
import com.eucalyptus.configurable.ConfigurableProperty;
import com.eucalyptus.configurable.ConfigurablePropertyException;
import com.eucalyptus.configurable.PropertyChangeListener;
import com.eucalyptus.entities.AbstractPersistent;
import edu.ucsb.eucalyptus.cloud.entities.SystemConfiguration;

@Entity
@javax.persistence.Entity
@PersistenceContext( name = "eucalyptus_cloud" )
@Table( name = "cloud_image_configuration" )
@Cache( usage = CacheConcurrencyStrategy.TRANSACTIONAL )
@ConfigurableClass( root = "bootstrap.webservices", description = "Parameters controlling the web services endpoint." )
public class StackConfiguration extends AbstractPersistent {
  
  @ConfigurableField( initial = "500", description = "Channel connect timeout (ms)." )
  public static Integer       CHANNEL_CONNECT_TIMEOUT           = 500;
  @ConfigurableField( initial = "3", changeListener = TimeChangeListener.class,
      description = "Time interval duration (in seconds) during which duplicate signatures will be accepted to accomodate collisions for legitimate requests inherent in Query/REST signing protocol." )
  public static Integer       REPLAY_SKEW_WINDOW_SEC            = 3;
  @ConfigurableField( initial = "300",
      description = "A max clock skew value (in seconds) between client and server accepted when validating timestamps in Query/REST protocol.",
      changeListener = TimeChangeListener.class )
  public static Integer       CLOCK_SKEW_SEC                    = 20;
  public static final Boolean SERVER_CHANNEL_REUSE_ADDRESS      = true;
  public static final Boolean SERVER_CHANNEL_NODELAY            = true;
  public static final Boolean CHANNEL_REUSE_ADDRESS             = true;
  public static final Boolean CHANNEL_KEEP_ALIVE                = true;
  public static final Boolean CHANNEL_NODELAY                   = true;
  @ConfigurableField( initial = "128", description = "Server worker thread pool max." )
  public static Integer       SERVER_POOL_MAX_THREADS           = 128;
  @ConfigurableField( initial = "0", description = "Server max worker memory per connection." )
  public static Long          SERVER_POOL_MAX_MEM_PER_CONN      = 0L;
  @ConfigurableField( initial = "0", description = "Server max worker memory total." )
  public static Long          SERVER_POOL_TOTAL_MEM             = 0L;
  
  @ConfigurableField( initial = "500", description = "Service socket select timeout (ms)." )
  public static Long          SERVER_POOL_TIMEOUT_MILLIS        = 500L;
  
  @ConfigurableField( initial = "128", description = "Server selector thread pool max." )
  public static Integer       SERVER_BOSS_POOL_MAX_THREADS      = 128;
  
  @ConfigurableField( initial = "0", description = "Server max selector memory per connection." )
  public static Long          SERVER_BOSS_POOL_MAX_MEM_PER_CONN = 0L;
  
  @ConfigurableField( initial = "0", description = "Server worker thread pool max." )
  public static Long          SERVER_BOSS_POOL_TOTAL_MEM        = 0L;
  
  @ConfigurableField( initial = "500", description = "Service socket select timeout (ms)." )
  public static Long          SERVER_BOSS_POOL_TIMEOUT_MILLIS   = 500L;
  
  @ConfigurableField( description = "Web services port.", changeListener = WebServices.PortListener.class )
  public static Integer       PORT                              = 8773;
  public static final Integer INTERNAL_PORT                     = 8773;
  
  @ConfigurableField( initial = "240", description = "Client idle timeout (secs)." )
  public static Long          CLIENT_IDLE_TIMEOUT_SECS          = 240L;
  
  @ConfigurableField( initial = "240", description = "Cluster client idle timeout (secs)." )
  public static Long          CLUSTER_IDLE_TIMEOUT_SECS         = 240L;
  
  @ConfigurableField( initial = "2000", description = "Cluster connect timeout (ms)." )
  public static Long          CLUSTER_CONNECT_TIMEOUT_MILLIS    = 2000L;
  
  @ConfigurableField( initial = "20", description = "Server socket read time-out." )
  public static Long          PIPELINE_READ_TIMEOUT_SECONDS     = 20L;
  
  @ConfigurableField( initial = "20", description = "Server socket write time-out." )
  public static Long          PIPELINE_WRITE_TIMEOUT_SECONDS    = 20L;
  
  @ConfigurableField( initial = "1048576000", description = "Server http chunk max." )
  public static Integer       CLIENT_HTTP_CHUNK_BUFFER_MAX      = 1048576000;
  
  @ConfigurableField( initial = "40", description = "Server worker thread pool max." )
  public static Integer       CLIENT_POOL_MAX_THREADS           = 40;
  
  @ConfigurableField( initial = "0", description = "Server worker thread pool max." )
  public static Long          CLIENT_POOL_MAX_MEM_PER_CONN      = 0L;
  
  @ConfigurableField( initial = "0", description = "Server worker thread pool max." )
  public static Long          CLIENT_POOL_TOTAL_MEM             = 0L;
  
  @ConfigurableField( initial = "500", description = "Client socket select timeout (ms)." )
  public static Long          CLIENT_POOL_TIMEOUT_MILLIS        = 500L;
  
  @ConfigurableField( initial = "102400", description = "Maximum HTTP chunk size (bytes)." )
  public static Integer       HTTP_MAX_CHUNK_BYTES              = 10 * 10 * 1024;
  @ConfigurableField( initial = "4096", description = "Maximum HTTP initial line size (bytes)." )
  public static Integer       HTTP_MAX_INITIAL_LINE_BYTES       = 4 * 1024;
  @ConfigurableField( initial = "8192", description = "Maximum HTTP headers size (bytes)." )
  public static Integer       HTTP_MAX_HEADER_BYTES             = 8 * 1024;
  
  @ConfigurableField( description = "Default scheme prefix in eucarc.", changeListener = TemporarySchemeUpdater.class )
  public static Boolean       DEFAULT_HTTPS_ENABLED             = Boolean.FALSE;
  
  @ConfigurableField( initial = "http", description = "Default scheme for EC2_URL in eucarc.", changeListener = UriChangeListener.class )
  public static String        DEFAULT_EC2_URI_SCHEME            = "http";                                      //GRZE: there references to specific services are not in the right scope here. 
                                                                                                                
  @ConfigurableField( initial = "http", description = "Default scheme for S3_URL in eucarc.", changeListener = UriChangeListener.class )
  public static String        DEFAULT_S3_URI_SCHEME             = "http";                                      //GRZE: there references to specific services are not in the right scope here.
                                                                                                                
  @ConfigurableField( initial = "http", description = "Default scheme for AWS_SNS_URL in eucarc.", changeListener = UriChangeListener.class )
  public static String        DEFAULT_AWS_SNS_URI_SCHEME        = "http";                                      //GRZE: there references to specific services are not in the right scope here.
                                                                                                                
  @ConfigurableField( initial = "http", description = "Default scheme for EUARE_URL in eucarc.", changeListener = UriChangeListener.class )
  public static String        DEFAULT_EUARE_URI_SCHEME          = "http";                                      //GRZE: there references to specific services are not in the right scope here.
                                                                                                                
  @ConfigurableField( description = "Use DNS delegation for eucarc." )
  public static Boolean       USE_DNS_DELEGATION                = Boolean.FALSE;
  
  private static Logger       LOG                               = Logger.getLogger( StackConfiguration.class );
  
  public enum BasicTransport implements TransportDefinition {
    HTTP {
      @Override
      public String getScheme( ) {
        return "http";
      }
      
      @Override
      public String getSecureScheme( ) {
        return "https";
      }
    },
    JMX {
      @Override
      public String getSecureScheme( ) {
        return getScheme( );
      }
      
      @Override
      public String getScheme( ) {
        return "service:jmx:rmi:///jndi/rmi://";
      }
    },
    JDBC {
      
      @Override
      public String getSecureScheme( ) {
        return getScheme( );
      }
      
      @Override
      public String getScheme( ) {
        return Databases.getBootstrapper( ).getJdbcScheme( );
      }
      
    };
    @Override
    public abstract String getScheme( );
    
    @Override
    public abstract String getSecureScheme( );
  }
  
  public static String lookupDnsDomain( ) {
    return SystemConfiguration.getSystemConfiguration( ).getDnsDomain( );//TODO:GRZE: sigh. 
  }
  
  public static class TimeChangeListener implements PropertyChangeListener {
    /**
     * @see com.eucalyptus.configurable.PropertyChangeListener#fireChange(com.eucalyptus.configurable.ConfigurableProperty,
     *      java.lang.Object)
     * 
     *      Validates that the new value is >= 0
     */
    @Override
    public void fireChange( ConfigurableProperty t, Object newValue ) throws ConfigurablePropertyException {
      
      int time = -1;
      try {
        if ( newValue instanceof String ) {
          time = Integer.parseInt( ( String ) newValue );
        }
      } catch ( NumberFormatException e ) {
        LOG.debug( "Failed to parse int from " + newValue );
      }
      if ( time < 0 )
        throw new ConfigurablePropertyException( "An integer >= 0 is expected for " + t.getFieldName( ) );
      
    }
  }
  
  public static class TemporarySchemeUpdater implements PropertyChangeListener {
    @Override
    public void fireChange( ConfigurableProperty t, Object newValue ) throws ConfigurablePropertyException {
      String scheme = Boolean.TRUE.equals( "" + newValue )
        ? "https"
        : "http";
      DEFAULT_AWS_SNS_URI_SCHEME = DEFAULT_EC2_URI_SCHEME = DEFAULT_EUARE_URI_SCHEME = DEFAULT_S3_URI_SCHEME = scheme;
    }
  }
  
  public static class UriChangeListener implements PropertyChangeListener {
    /**
     * @see com.eucalyptus.configurable.PropertyChangeListener#fireChange(com.eucalyptus.configurable.ConfigurableProperty,
     *      java.lang.Object)
     * 
     *      Validates that the new value is >= 0
     */
    @Override
    public void fireChange( ConfigurableProperty t, Object newValue ) throws ConfigurablePropertyException {
      
      String prefix = null;
      
      if ( newValue instanceof String ) {
        prefix = ( String ) newValue;
        if ( "http".equals( prefix ) || "https".equals( prefix ) )
          return;
      }
      throw new ConfigurablePropertyException( "URL prefix for " + t.getFieldName( )
                                               + " has to be 'http' or 'https'" );
      
    }
  }
  
}
