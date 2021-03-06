#!/bin/sh

# Copyright 2009-2012 Ent. Services Development Corporation LP
#
# Redistribution and use of this software in source and binary forms,
# with or without modification, are permitted provided that the
# following conditions are met:
#
#   Redistributions of source code must retain the above copyright
#   notice, this list of conditions and the following disclaimer.
#
#   Redistributions in binary form must reproduce the above copyright
#   notice, this list of conditions and the following disclaimer
#   in the documentation and/or other materials provided with the
#   distribution.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
# "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
# LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
# FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
# COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
# INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
# BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
# LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
# CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
# LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
# ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
# POSSIBILITY OF SUCH DAMAGE.
#
# This file may incorporate work covered under the following copyright
# and permission notice:
#
#   Software License Agreement (BSD License)
#
#   Copyright (c) 2008, Regents of the University of California
#   All rights reserved.
#
#   Redistribution and use of this software in source and binary forms,
#   with or without modification, are permitted provided that the
#   following conditions are met:
#
#     Redistributions of source code must retain the above copyright
#     notice, this list of conditions and the following disclaimer.
#
#     Redistributions in binary form must reproduce the above copyright
#     notice, this list of conditions and the following disclaimer
#     in the documentation and/or other materials provided with the
#     distribution.
#
#   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
#   "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
#   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
#   FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
#   COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
#   INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
#   BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
#   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
#   CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
#   LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
#   ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
#   POSSIBILITY OF SUCH DAMAGE. USERS OF THIS SOFTWARE ACKNOWLEDGE
#   THE POSSIBLE PRESENCE OF OTHER OPEN SOURCE LICENSED MATERIAL,
#   COPYRIGHTED MATERIAL OR PATENTED MATERIAL IN THIS SOFTWARE,
#   AND IF ANY SUCH MATERIAL IS DISCOVERED THE PARTY DISCOVERING
#   IT MAY INFORM DR. RICH WOLSKI AT THE UNIVERSITY OF CALIFORNIA,
#   SANTA BARBARA WHO WILL THEN ASCERTAIN THE MOST APPROPRIATE REMEDY,
#   WHICH IN THE REGENTS' DISCRETION MAY INCLUDE, WITHOUT LIMITATION,
#   REPLACEMENT OF THE CODE SO IDENTIFIED, LICENSING OF THE CODE SO
#   IDENTIFIED, OR WITHDRAWAL OF THE CODE CAPABILITY TO THE EXTENT
#   NEEDED TO COMPLY WITH ANY SUCH LICENSES OR RIGHTS.

# Script to generate fake reporting data for testing of reporting.
#
# Usage: generate_reporting_data adminPassword (create|delete)
#
# This calls the FalseDataGenerator classes in a running Eucalyptus instance,
#  by using the CommandServlet. The FalseDataGenerator classes then generate
#  fake reporting data for testing. None of this is deployed in non-test
#  Eucalyptus installations.
#
# NOTE: You must first deploy the test classes by stopping Euca, then:
#   "cd $SRC/clc; ant build build-test install", then starting Euca again.

# Verify number of params, and display usage if wrong number

if [ "$#" -lt "2" ]
then
	echo "Usage: generate_false_data adminPassword (create|delete|instance-report-html|instance-report-csv|volume-report-html|snapshot-report-html|s3-report-html|elasticip-report-html)"
	exit 1
fi


# Parse password and command params, and get method name

password=$1
command=$2
shift 2

case "$command" in
	"create" )
		method="generateFalseData"
	;;
	"delete" )
		method="removeFalseData"
	;;
	"instance-report-html" )
		method="generateInstanceHtmlReport"
	;;
	"instance-report-csv" )
		method="generateInstanceCsvReport"
	;;
	"volume-report-html" )
		method="generateVolumeHtmlReport"
	;;
	"snapshot-report-html" )
		method="generateSnapshotHtmlReport"
	;;
	"s3-report-html" )
		method="generateS3HtmlReport"
	;;
	"elasticip-report-html" )
		method="generateElasticIpHtmlReport"
	;;
	* )
		echo "No such command:$command"
		echo "Usage: generate_false_data adminPassword (create|delete|instance-report-html|instance-report-csv|volume-report-html|snapshot-report-html|s3-report-html|elasticip-report-html)"
		exit 1
esac


# Login using LoginServlet

wget -O /tmp/sessionId --no-check-certificate "https://localhost:8443/loginservlet?adminPw=$password"
if [ "$?" -ne "0" ]
then
	echo "Login failed"
	exit 1
fi
export session=`cat /tmp/sessionId`
echo "session id:" $session


# Execute

wget --no-check-certificate -O /tmp/nothing "https://localhost:8443/commandservlet?sessionId=$session&className=com.eucalyptus.reporting.FalseDataGenerator&methodName=$method"
if [ "$?" -ne "0" ]
then
	echo "Command failed; session:$session method:$method"
	exit 1
 fi

