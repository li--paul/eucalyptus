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
package com.eucalyptus.autoscaling.policies

import static org.junit.Assert.*
import org.junit.Test

import static com.eucalyptus.autoscaling.policies.AdjustmentType.*

/**
 * 
 */
class AdjustmentTypeTest {
  
  @Test
  void testChangeInCapacity() {
    assertEquals( "Change in capacity (positive)", 3, ChangeInCapacity.adjustCapacity( 1, 2, 0, 1, 10 ) )
    assertEquals( "Change in capacity (negative)", 2, ChangeInCapacity.adjustCapacity( 4, -2, 0, 1, 10 ) )
  }

  @Test
  void testExactCapacity() {
    assertEquals( "Exact capacity", 2, ExactCapacity.adjustCapacity( 1, 2, 0, 1, 10 ) )
  }

  @Test
  void testPercentChangeInCapacity() {
    assertEquals( "Percent change in capacity (positive)", 110, PercentChangeInCapacity.adjustCapacity( 100, 10, 0, 1, 1000 ) )
    assertEquals( "Percent change in capacity (positive, min)", 120, PercentChangeInCapacity.adjustCapacity( 100, 10, 20, 1, 1000 ) )
    assertEquals( "Percent change in capacity (negative)", 50, PercentChangeInCapacity.adjustCapacity( 100, -50, 0, 1, 1000 ) )
    assertEquals( "Percent change in capacity (negative, min)", 40, PercentChangeInCapacity.adjustCapacity( 100, -50, -60, 1, 1000 ) )
  }

  @Test
  void testMinMaxEnforcement() {
    assertEquals( "Maximum limit reached", 10, ChangeInCapacity.adjustCapacity( 5, 6, 0, 1, 10 ) )
    assertEquals( "Minimum limit reached", 10, ChangeInCapacity.adjustCapacity( 14, -5, 0, 10, 100 ) )
  }
}
