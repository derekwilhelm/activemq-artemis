/*
 * Copyright 2005-2014 Red Hat, Inc.
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.apache.activemq.artemis.tests.compatibility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.activemq.artemis.tests.compatibility.GroovyRun.ONE_FIVE;
import static org.apache.activemq.artemis.tests.compatibility.GroovyRun.SNAPSHOT;

/**
 * To run this test on the IDE and debug it, run the compatibility-tests through a command line once:
 *
 * cd /compatibility-tests
 * mvn install -Ptests | tee output.log
 *
 * on the output.log you will see the output generated by {@link #getClasspathProperty(String)}
 *
 * On your IDE, edit the Run Configuration to your test and add those -D as parameters to your test.
 * On Idea you would do the following:
 *
 * Run->Edit Configuration->Add ArtemisMeshTest and add your properties.
 */
@RunWith(Parameterized.class)
public class SerializationTest extends VersionedBaseTest {

   // this will ensure that all tests in this class are run twice,
   // once with "true" passed to the class' constructor and once with "false"
   @Parameterized.Parameters(name = "server={0}, producer={1}, consumer={2}")
   public static Collection getParameters() {
      // we don't need every single version ever released..
      // if we keep testing current one against 2.4 and 1.4.. we are sure the wire and API won't change over time
      List<Object[]> combinations = new ArrayList<>();

      /*
      // during development sometimes is useful to comment out the combinations
      // and add the ones you are interested.. example:
       */
      //      combinations.add(new Object[]{SNAPSHOT, ONE_FIVE, ONE_FIVE});
      //      combinations.add(new Object[]{ONE_FIVE, ONE_FIVE, ONE_FIVE});

      combinations.addAll(combinatory(new Object[]{SNAPSHOT}, new Object[]{ONE_FIVE, SNAPSHOT}, new Object[]{ONE_FIVE, SNAPSHOT}));
      return combinations;
   }

   public SerializationTest(String server, String sender, String receiver) throws Exception {
      super(server, sender, receiver);
   }

   @Test
   public void testSerializeFactory() throws Throwable {
      File file = serverFolder.newFile("objects.ser");
      file.mkdirs();
      callMain(senderClassloader, GroovyRun.class.getName(), "serial/serial.groovy", file.getAbsolutePath(), "write");
      callMain(receiverClassloader, GroovyRun.class.getName(), "serial/serial.groovy", file.getAbsolutePath(), "read");
   }

}
