/*************************************************************************
 * Copyright 2008 Regents of the University of California
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
package com.eucalyptus.reporting.art.renderer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import com.eucalyptus.reporting.art.entity.AccountArtEntity;
import com.eucalyptus.reporting.art.entity.AvailabilityZoneArtEntity;
import com.eucalyptus.reporting.art.entity.InstanceArtEntity;
import com.eucalyptus.reporting.art.entity.InstanceUsageArtEntity;
import com.eucalyptus.reporting.art.entity.ReportArtEntity;
import com.eucalyptus.reporting.art.entity.UserArtEntity;
import com.eucalyptus.reporting.art.renderer.document.Document;
import com.eucalyptus.reporting.units.SizeUnit;
import com.eucalyptus.reporting.units.TimeUnit;
import com.eucalyptus.reporting.units.UnitUtil;
import com.eucalyptus.reporting.units.Units;

class InstanceRenderer
	implements Renderer
{
	private final Document doc;

	public InstanceRenderer( final Document doc )
	{
		this.doc = doc;
	}

	@Override
	public void render( final ReportArtEntity report, final OutputStream os,
						final Units units ) throws IOException
	{
		doc.setWriter(new OutputStreamWriter(os));

		doc.open();
		doc.textLine("Instance Report", 1);
		doc.textLine("Begin:" + new Date(report.getBeginMs()).toString(), 4);
		doc.textLine("End:" + new Date(report.getEndMs()).toString(), 4);
		doc.textLine("Resource Usage Section", 3);
		doc.tableOpen();
		doc.newRow().addEmptyValCols(5)
				.addValCol("Net Total " + units.getSizeUnit(), 2, "center")
				.addValCol("Net External " + units.getSizeUnit(), 2, "center")
				.addValCol("Disk " + units.getSizeUnit(), 2, "center")
				.addValCol("Disk IOPS (M)", 2, "center")
				.addValCol("Disk Time (" + TimeUnit.values()[units.getTimeUnit().ordinal()-1] + ")", 2, "center");
		doc.newRow().addValCol("InstanceId")
				.addValCol("Type").addValCol("#").addValCol(units.getTimeUnit().toString()).addValCol("CpuUsage%")
				.addValCol("In").addValCol("Out")
				.addValCol("In").addValCol("Out")
				.addValCol("Read").addValCol("Write")
				.addValCol("Read").addValCol("Write")
				.addValCol("Read").addValCol("Write");
		for( final String zoneName : report.getZones().keySet() ) {
			final AvailabilityZoneArtEntity zone = report.getZones().get(zoneName);
			doc.newRow().addLabelCol(0, "Zone: " + zoneName)
					.addValCol("cumul.")
					.addValCol("cumul.");
			addUsageCols(doc, zone.getUsageTotals().getInstanceTotals(), units);
			for (String accountName: zone.getAccounts().keySet()) {
				AccountArtEntity account = zone.getAccounts().get(accountName);
				doc.newRow().addLabelCol(1, "Account: " + accountName)
						.addValCol("cumul.")
						.addValCol("cumul.");
				addUsageCols(doc, account.getUsageTotals().getInstanceTotals(),units);
				for (String userName: account.getUsers().keySet()) {
					UserArtEntity user = account.getUsers().get(userName);
					doc.newRow().addLabelCol(2, "User: " + userName)
							.addValCol("cumul.")
							.addValCol("cumul.");
					addUsageCols(doc, user.getUsageTotals().getInstanceTotals(),units);
					for (String instanceUuid: user.getInstances().keySet()) {
						InstanceArtEntity instance = user.getInstances().get(instanceUuid);
						doc.newRow().addValCol(instance.getInstanceId())
								.addValCol(instance.getInstanceType());
						addUsageCols(doc, instance.getUsage(), units);
					}
				}
			}
		}
		doc.tableClose();
		doc.close();
	}

	public static Document addUsageCols( final Document doc,
				final InstanceUsageArtEntity entity,
				final Units units )
			throws IOException
	{
		doc.addValCol((long)entity.getInstanceCnt());
		doc.addValCol(UnitUtil.convertTime(entity.getDurationMs(), TimeUnit.MS, units.getTimeUnit()));
		if (entity.getDurationMs()>0) {
			doc.addValCol(entity.getCpuUtilizationMs()==null?0.0d:(100.0d * (double)entity.getCpuUtilizationMs()/(double)entity.getDurationMs()));	// Percentage, so multiply ratio by 100		
		} else {
			doc.addValCol(0d); //Doesn't work if you divide by zero
		}
		doc.addValCol(UnitUtil.convertSize(entity.getNetTotalInBytes(), SizeUnit.B, units.getSizeUnit()));
		doc.addValCol(UnitUtil.convertSize(entity.getNetTotalOutBytes(), SizeUnit.B, units.getSizeUnit()));
		doc.addValCol(UnitUtil.convertSize(entity.getNetExternalInBytes(), SizeUnit.B, units.getSizeUnit()));
		doc.addValCol(UnitUtil.convertSize(entity.getNetExternalOutBytes(), SizeUnit.B, units.getSizeUnit()));
		doc.addValCol(UnitUtil.convertSize(entity.getDiskReadBytes(), SizeUnit.B, units.getSizeUnit()));
		doc.addValCol(UnitUtil.convertSize(entity.getDiskWriteBytes(), SizeUnit.B, units.getSizeUnit()));
		doc.addValCol(entity.getDiskReadOps()==null?null:((double)entity.getDiskReadOps()/1000000d)); //TODO: do something about this
		doc.addValCol(entity.getDiskWriteOps()==null?null:((double)entity.getDiskWriteOps()/1000000d));
		doc.addValCol(UnitUtil.convertTime(entity.getDiskReadTime(), TimeUnit.MS, TimeUnit.values()[units.getTimeUnit().ordinal()-1]));
		doc.addValCol(UnitUtil.convertTime(entity.getDiskWriteTime(), TimeUnit.MS, TimeUnit.values()[units.getTimeUnit().ordinal()-1]));

		return doc;
	}
}
