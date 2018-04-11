/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package backtype.storm.task;

/**
 * A TopologyContext that is given to a spout is actually an instance
 * of SpoutTopologyContext. Currently spouts can store spout specific
 * information in this structure.
 */
public class SpoutTopologyContext extends TopologyContext {
  public SpoutTopologyContext(org.apache.heron.api.topology.TopologyContext delegate) {
    super(delegate);
  }

  /**
   * Gets the Maximum Spout Pending value for this instance of spout.
   */
  public Long getMaxSpoutPending() {
    throw new RuntimeException("Heron does not support Auto MSP");
  }

  /**
   * Sets the Maximum Spout Pending value for this instance of spout
   */
  public void setMaxSpoutPending(Long maxSpoutPending) {
    throw new RuntimeException("Heron does not support Auto MSP");
  }
}
