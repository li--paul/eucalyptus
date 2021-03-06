/*************************************************************************
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
 * POSSIBILITY OF SUCH DAMAGE.
 ************************************************************************/
@GroovyAddClassUUID
package com.eucalyptus.reporting.service

import edu.ucsb.eucalyptus.msgs.BaseMessage
import edu.ucsb.eucalyptus.msgs.EucalyptusData
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import com.eucalyptus.component.annotation.ComponentMessage
import com.eucalyptus.component.id.Reporting
import com.eucalyptus.reporting.export.ReportingExport
import com.eucalyptus.binding.HttpParameterMapping

import edu.ucsb.eucalyptus.msgs.GroovyAddClassUUID

@ComponentMessage(Reporting.class)
class ReportingMessage extends BaseMessage {
}

class ExportReportDataType extends ReportingMessage {
  @HttpParameterMapping (parameter = "Start")
  String startDate
  @HttpParameterMapping (parameter = "End")
  String endDate
  boolean dependencies
}

class ExportReportDataResponseType extends ReportingMessage  {
  ExportDataResultType result
  ReportingResponseMetadataType responseMetadata = new ReportingResponseMetadataType( );
}

class ReportingResponseMetadataType extends EucalyptusData {
  String requestId
}

class ReportingErrorType extends EucalyptusData {
  String type
  String code
  String message
  ReportingErrorDetailType detail = new ReportingErrorDetailType( );
}

class ReportingErrorDetailType extends EucalyptusData {
}

class ReportingErrorResponseType extends ReportingMessage {
  String requestId
  HttpResponseStatus httpStatus;
  ArrayList<ReportingErrorType> errors = new ArrayList<ReportingErrorType>( )

  ReportingErrorResponseType( ) {
    set_return( false )
  }

  @Override
  String toSimpleString( ) {
    "${errors?.getAt(0)?.type} error (${errors?.getAt(0)?.code}): ${errors?.getAt(0)?.message}"
  }
}

class ExportDataResultType extends EucalyptusData {
  ExportDataResultType(){}
  ExportDataResultType( ReportingExport data ){ this.data = data }
  ReportingExport data
}

class DeleteReportDataType extends ReportingMessage {
  @HttpParameterMapping (parameter = "End")
  String endDate
}

class DeleteReportDataResponseType extends ReportingMessage  {
  DeleteDataResultType result
  ReportingResponseMetadataType responseMetadata = new ReportingResponseMetadataType( );
}

class DeleteDataResultType extends EucalyptusData {
  DeleteDataResultType(){}
  DeleteDataResultType( int count ){ this.count = count }
  int count
}

class GenerateReportType extends ReportingMessage {
  @HttpParameterMapping (parameter = "Start")
  String startDate
  @HttpParameterMapping (parameter = "End")
  String endDate
  String type
  String format
  String timeUnit
  String sizeUnit
  String sizeTimeTimeUnit
  String sizeTimeSizeUnit
}

class GenerateReportResponseType extends ReportingMessage  {
  GenerateReportResultType result
  ReportingResponseMetadataType responseMetadata = new ReportingResponseMetadataType( );
}

class GenerateReportResultType extends EucalyptusData {
  GenerateReportResultType(){}
  GenerateReportResultType( data ){ this.data = data }
  String data
}



