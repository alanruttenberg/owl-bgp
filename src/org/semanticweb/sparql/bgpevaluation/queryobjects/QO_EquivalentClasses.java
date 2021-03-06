/* Copyright 2010-2012 by the developers of the OWL-BGP project. 

   This file is part of the OWL-BGP project.

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


package  org.semanticweb.sparql.bgpevaluation.queryobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.FromOWLAPIConverter;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.axioms.EquivalentClasses;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;

public class QO_EquivalentClasses extends AbstractQueryObject<EquivalentClasses>  {

	public QO_EquivalentClasses(EquivalentClasses axiomTemplate, OWLOntologyGraph graph) {
        super(axiomTemplate, graph);
    }
	
    protected List<Atomic[]> addBindings(Atomic[] currentBinding, Map<Variable,Integer> bindingPositions) {
	    Map<Variable,Atomic> bindingMap=new HashMap<Variable, Atomic>();
        // apply bindings that are already computed from previous steps
        for (Variable var : bindingPositions.keySet())
            bindingMap.put(var, currentBinding[bindingPositions.get(var)]);
        EquivalentClasses axiom=(EquivalentClasses)m_axiomTemplate.getBoundVersion(bindingMap);
        int exprSize=axiom.getClassExpressions().size();
        assert exprSize==2; // should be rewritten accordingly
        Iterator<ClassExpression> classIt=axiom.getClassExpressions().iterator();
        ClassExpression cls1=classIt.next();
        ClassExpression cls2;
        if (exprSize==1)
            cls2=cls1;
        else
        	cls2=classIt.next();
        try {
            if (cls1.isVariable() && cls2.isVariable()) {
                int[] positions=new int[2];
                positions[0]=bindingPositions.get(cls1);
                positions[1]=bindingPositions.get(cls2);
                return computeAllEquivalentClassRelations(currentBinding,positions);
            } else if (cls1.isVariable() && !cls2.isVariable() && cls2.getBoundVersion(bindingMap).getVariablesInSignature().isEmpty()) {
                int position=bindingPositions.get(cls1);
                return computeEquivalentClasses(currentBinding,(OWLClassExpression)cls2.asOWLAPIObject(m_toOWLAPIConverter),position);
            } else if (cls2.isVariable() && !cls1.isVariable() && cls1.getBoundVersion(bindingMap).getVariablesInSignature().isEmpty()) {
                int position=bindingPositions.get(cls2);
                return computeEquivalentClasses(currentBinding,(OWLClassExpression)cls1.asOWLAPIObject(m_toOWLAPIConverter),position);
            } else if (cls1.getBoundVersion(bindingMap).getVariablesInSignature().isEmpty() && cls2.getBoundVersion(bindingMap).getVariablesInSignature().isEmpty())
                return checkEquivalence(currentBinding,(OWLClassExpression)cls1.asOWLAPIObject(m_toOWLAPIConverter),(OWLClassExpression)cls2.asOWLAPIObject(m_toOWLAPIConverter));
            else 
                return complex(currentBinding,axiom,bindingPositions);
        } catch (IllegalArgumentException e) {
            // current binding is incompatible will not add new bindings in newBindings
            return new ArrayList<Atomic[]>();
        }
	}
	protected List<Atomic[]> computeAllEquivalentClassRelations(Atomic[] currentBinding, int[] bindingPositions) {
        // EquivalentClasses(?x ?y)
	    Atomic[] binding;
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        for (Clazz clazz : m_graph.getClassesInSignature()) {
            OWLClass owlClass=(OWLClass)clazz.asOWLAPIObject(m_toOWLAPIConverter);
            Set<OWLClass> equivalentClasses=m_reasoner.getEquivalentClasses(owlClass).getEntities();
            for (OWLClass cls : equivalentClasses) {
                binding=currentBinding.clone();
                binding[bindingPositions[0]]=clazz;
                binding[bindingPositions[1]]=(Clazz)FromOWLAPIConverter.convert(cls);
                newBindings.add(binding);
            }
        }
        return newBindings;
	}
    protected List<Atomic[]> computeEquivalentClasses(Atomic[] currentBinding, OWLClassExpression cls, int bindingPosition) {
         // EquivalentClasses(?x :C)
	     Atomic[] binding;
	     List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
         Set<OWLClass> eqcs=m_reasoner.getEquivalentClasses(cls).getEntities();
         for (OWLClass sub : eqcs) {
             binding=currentBinding.clone();
             binding[bindingPosition]=(Clazz)FromOWLAPIConverter.convert(sub);
             newBindings.add(binding);
         }
	     return newBindings;
	 }
    protected List<Atomic[]> checkEquivalence(Atomic[] currentBinding, OWLClassExpression cls1, OWLClassExpression cls2) {
        // EquivalentClasses(:C :D)
        List<Atomic[]> newBindings=new ArrayList<Atomic[]>();
        if (m_reasoner.isEntailed(m_dataFactory.getOWLEquivalentClassesAxiom(cls1, cls2)))
            newBindings.add(currentBinding);
        return newBindings;
    }
    public <O> O accept(DynamicQueryObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

	@Override
	public <O> O accept(StaticQueryObjectVisitorEx<O> visitor, Set<Variable> bound) {
	    return visitor.visit(this, bound); 
	}
}
