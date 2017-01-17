/*
 * Copyright 2012-2014 Brandon Beck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.isomorphism.util;

import org.junit.Test;

public class TokenBucketBuilderTest
{
  private final TokenBuckets.Builder builder = TokenBuckets.builder();

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeCapacity() {
    builder.withCapacity(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroCapacity() {
    builder.withCapacity(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeInitialTokens() {
    builder.withInitialTokens(-1);
  }
}
