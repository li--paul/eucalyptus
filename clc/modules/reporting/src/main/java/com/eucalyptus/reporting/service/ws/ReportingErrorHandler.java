/*************************************************************************
 * Copyright 2009-2013 Ent. Services Development Corporation LP
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
 * POSSIBILITY OF SUCH DAMAGE.
 ************************************************************************/
package com.eucalyptus.reporting.service.ws;

import org.apache.log4j.Logger;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.messaging.MessagingException;
import com.eucalyptus.binding.BindingManager;
import com.eucalyptus.component.annotation.ComponentNamed;
import com.eucalyptus.context.Contexts;
import com.eucalyptus.records.EventRecord;
import com.eucalyptus.records.EventType;
import com.eucalyptus.reporting.service.ReportingErrorResponseType;
import com.eucalyptus.reporting.service.ReportingErrorType;
import com.eucalyptus.reporting.service.ReportingException;
import com.eucalyptus.util.EucalyptusCloudException;
import com.eucalyptus.util.Exceptions;
import com.eucalyptus.util.LogUtil;
import edu.ucsb.eucalyptus.msgs.BaseMessage;
import edu.ucsb.eucalyptus.msgs.EucalyptusErrorMessageType;

/**
 *
 */
@ComponentNamed
public class ReportingErrorHandler {
  private static final Logger log = Logger.getLogger( ReportingErrorHandler.class );
  private static final String INTERNAL_FAILURE = "InternalFailure";

  public void handle( final MessagingException exception ) {
    final Object payloadObject = exception.getFailedMessage( ).getPayload( );
    final EucalyptusCloudException cloudException = Exceptions.findCause( exception, EucalyptusCloudException.class );
    EventRecord.here(ReportingErrorHandler.class, EventType.MSG_REPLY, payloadObject.getClass().getSimpleName()).debug( );
    log.trace("Caught exception while servicing: " + payloadObject);
    if ( cloudException != null ) {
      try {
        final BaseMessage payload = parsePayload( payloadObject );
        final ReportingErrorResponseType errorResp = new ReportingErrorResponseType( );
        final HttpResponseStatus status;
        final String code;
        if ( cloudException instanceof ReportingException ) {
          final ReportingException reportingException = (ReportingException) cloudException;
          status = reportingException.getStatus();
          code = reportingException.getError();
        } else {
          status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
          code = INTERNAL_FAILURE;
        }
        errorResp.setHttpStatus( status );
        errorResp.setCorrelationId( payload.getCorrelationId( ) );
        errorResp.setRequestId( payload.getCorrelationId( ) );
        final ReportingErrorType error = new ReportingErrorType( );
        error.setType( "Receiver" );
        error.setCode( code );
        error.setMessage( cloudException.getMessage() );
        errorResp.getErrors().add( error );
        Contexts.response(errorResp);
      } catch ( final PayloadParseException e ) {
        log.error( "Failed to parse payload ", e.getCause() );
      }
    } else {
      log.error( "Unable to handle exception", exception );
    }
  }

  private static BaseMessage parsePayload( final Object payload ) throws PayloadParseException {
    if ( payload instanceof BaseMessage ) {
      return ( BaseMessage ) payload;
    } else if ( payload instanceof String ) {
      try {
        return ( BaseMessage ) BindingManager.getBinding( ReportingQueryBinding.REPORTING_DEFAULT_NAMESPACE ).fromOM( ( String ) payload );
      } catch ( Exception e ) {
        throw new PayloadParseException( e );
      }
    }
    return new EucalyptusErrorMessageType( "ReportingErrorHandler", LogUtil.dumpObject( payload ) );
  }

  private static final class PayloadParseException extends Exception {
    private static final long serialVersionUID = 1L;

    public PayloadParseException( final Throwable cause ) {
      super(cause);
    }
  }

}
