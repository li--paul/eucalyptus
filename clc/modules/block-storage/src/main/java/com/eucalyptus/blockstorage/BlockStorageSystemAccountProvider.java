/*************************************************************************
 * Copyright 2009-2015 Ent. Services Development Corporation LP
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
package com.eucalyptus.blockstorage;

import java.util.List;
import com.eucalyptus.auth.principal.AccountIdentifiers;
import com.eucalyptus.auth.util.SystemAccountProvider;
import com.eucalyptus.blockstorage.util.StorageProperties;
import com.google.common.collect.ImmutableList;

/**
 *
 */
public class BlockStorageSystemAccountProvider implements SystemAccountProvider {

  @Override
  public String getAlias( ) {
    return AccountIdentifiers.BLOCKSTORAGE_SYSTEM_ACCOUNT;
  }

  @Override
  public boolean isCreateAdminAccessKey( ) {
    return false;
  }

  @Override
  public List<SystemAccountRole> getRoles( ) {
    return ImmutableList.<SystemAccountRole>of(
        new BasicSystemAccountRole(
            StorageProperties.EBS_ROLE_NAME,
            "/blockstorage",
            StorageProperties.DEFAULT_ASSUME_ROLE_POLICY,
            ImmutableList.<AttachedPolicy>of(
                new BasicAttachedPolicy(
                    StorageProperties.S3_BUCKET_ACCESS_POLICY_NAME,
                    StorageProperties.S3_SNAPSHOT_BUCKET_ACCESS_POLICY
                ),
                new BasicAttachedPolicy(
                    StorageProperties.S3_OBJECT_ACCESS_POLICY_NAME,
                    StorageProperties.S3_SNAPSHOT_OBJECT_ACCESS_POLICY
                )
            )
        )
    );
  }
}
