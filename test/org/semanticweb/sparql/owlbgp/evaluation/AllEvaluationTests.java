/* Copyright 2010-2012 by the developers of the OWL-BGP project. 

   This file is part of OWL-BGP.

   OWL-BGP is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   OWL-BGP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with OWL-BGP. If not, see <http://www.gnu.org/licenses/>.
 */

package  org.semanticweb.sparql.owlbgp.evaluation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllEvaluationTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite("Unit tests for the query evaluation.");
        // $JUnit-BEGIN$
        suite.addTestSuite(TestPizzaQueries.class);
        suite.addTestSuite(OntologyTests.class);
        suite.addTestSuite(QueryTests.class);
        suite.addTestSuite(TestGalenEntailment.class);
        suite.addTestSuite(TestPizzaQueries.class);
        suite.addTestSuite(TestSeveralComponents.class);
        // $JUnit-END$
        return suite;
    }
}
