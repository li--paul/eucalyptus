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
package com.eucalyptus.reporting.art.entity;

import java.util.*;

import com.eucalyptus.reporting.art.ArtObject;
import com.eucalyptus.reporting.art.util.IndentingStringBuffer;

public class UserArtEntity
	implements ArtObject
{
	private final Map<String,InstanceArtEntity> instances;
	private final Map<String,VolumeArtEntity> volumes;
	private final Map<String,BucketUsageArtEntity> bucketsUsage;
	private final Map<String,ElasticIpArtEntity> elasticIps;
	private final UsageTotalsArtEntity totals;
	
	public UserArtEntity()
	{
		super();
		this.instances = new HashMap<String,InstanceArtEntity>();
		this.volumes = new HashMap<String,VolumeArtEntity>();
		this.bucketsUsage = new HashMap<String,BucketUsageArtEntity>();
		this.elasticIps = new HashMap<String,ElasticIpArtEntity>();
		this.totals = new UsageTotalsArtEntity();
	}

	public Map<String,InstanceArtEntity> getInstances()
	{
		return instances;
	}

	public Map<String,VolumeArtEntity> getVolumes()
	{
		return volumes;
	}

	public Map<String,BucketUsageArtEntity> getBucketUsage()
	{
		return bucketsUsage;
	}

	/**
	 * "127.0.0.1" -> ElasticIpUsageArtEntity 
	 */
	public Map<String,ElasticIpArtEntity> getElasticIps()
	{
		return elasticIps;
	}

	public UsageTotalsArtEntity getUsageTotals()
	{
		return totals;
	}

	public String prettyPrint(int numIndents)
	{
		IndentingStringBuffer sb = new IndentingStringBuffer();
		sb.appendIndentLine(numIndents, String.format("(totals:%s", totals));
		if (instances.keySet().size()>0) {
			sb.append(" instances:{");
			for (String uuid : instances.keySet()) {
				sb.appendIndentLine(numIndents+1, instances.get(uuid).toString());			
			}
			sb.appendIndentLine(numIndents, "})");
		} else if (volumes.keySet().size()>0) {
			sb.append(" volumes:{");
			for (String uuid : volumes.keySet()) {
				sb.appendIndentLine(numIndents+1, volumes.get(uuid).toString());			
			}
			sb.appendIndentLine(numIndents, "})");			
		}
		return sb.toString();
	}

}
