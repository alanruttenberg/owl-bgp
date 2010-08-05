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
package org.semanticweb.sparql.owlbgp.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;
    
public interface ExtendedOWLObject extends Serializable {
    public String toString(Prefixes prefixes);
    public String toTurtleString(Identifier mainNode);
    public String toTurtleString(Prefixes prefixes, Identifier mainNode);
    public ExtendedOWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings);
    public OWLObject getBoundVersion(Map<Variable,Atomic> variablesToBindings,OWLDataFactory dataFactory);
    public Iterable<ExtendedOWLObject> getAppliedBindingsIterator(Map<Variable,Set<Atomic>> variablesToBindings);
    public Iterable<OWLObject> getAppliedBindingsOWLAPIIterator(Map<Variable,Set<Atomic>> variablesToBindings,OWLDataFactory dataFactory);
    public Set<Variable> getVariablesInSignature();
    public Set<Variable> getVariablesInSignature(VarType varType);
    <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor);
    public OWLObject asOWLAPIObject(OWLDataFactory dataFactory);
}
