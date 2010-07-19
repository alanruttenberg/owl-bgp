/* Copyright 2010 by the Oxford University Computing Laboratory
   
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
   along with OWL-BGP.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.semanticweb.sparql.owlbgp.model.classexpressions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.AbstractExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObject;
import org.semanticweb.sparql.owlbgp.model.ExtendedOWLObjectVisitorEx;
import org.semanticweb.sparql.owlbgp.model.InterningManager;
import org.semanticweb.sparql.owlbgp.model.OWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Prefixes;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;


public class ObjectMinCardinality extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -7384803571695668233L;

    protected static InterningManager<ObjectMinCardinality> s_interningManager=new InterningManager<ObjectMinCardinality>() {
        protected boolean equal(ObjectMinCardinality object1,ObjectMinCardinality object2) {
            return object1.m_cardinality==object2.m_cardinality&&object1.m_ope==object2.m_ope&&object1.m_classExpression==object2.m_classExpression;
        }
        protected int getHashCode(ObjectMinCardinality object) {
            return object.m_cardinality*7+object.m_ope.hashCode()*13+object.m_classExpression.hashCode()*17;
        }
    };
    
    protected final int m_cardinality;
    protected final ObjectPropertyExpression m_ope;
    protected final ClassExpression m_classExpression;
   
    protected ObjectMinCardinality(int cardinality,ObjectPropertyExpression ope,ClassExpression classExpression) {
        m_cardinality=cardinality;
        m_ope=ope;
        m_classExpression=classExpression;
    }
    public int getCardinality() {
        return m_cardinality;
    }
    public ObjectPropertyExpression getObjectPropertyExpression() {
        return m_ope;
    }
    public ClassExpression getClassExpression() {
        return m_classExpression;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectMinCardinality(");
        buffer.append(m_cardinality);
        buffer.append(" ");
        buffer.append(m_ope.toString(prefixes));
        buffer.append(" ");
        buffer.append(m_classExpression.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static ObjectMinCardinality create(int cardinality,ObjectPropertyExpression ope,ClassExpression classExpression) {
        return s_interningManager.intern(new ObjectMinCardinality(cardinality,ope,classExpression));
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_ope.getVariablesInSignature(varType));
        variables.addAll(m_classExpression.getVariablesInSignature(varType));
        return variables;
    }
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings) {
        return create(m_cardinality,(ObjectPropertyExpression)m_ope.getBoundVersion(variablesToBindings),(ClassExpression)m_classExpression.getBoundVersion(variablesToBindings));
    }
}
